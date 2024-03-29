/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.RatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.CurrencyEdit;
import com.purnama.pjm_client.gui.inner.home.CurrencyHome;
import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class CurrencyDetail extends DetailPanel{
    
    private Currency currency;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel, defaultpanel;
    
    private final int id;
    
    private final RatePanel ratepanel;
    
    public CurrencyDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_CURRENCY_DETAIL"));
        
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        defaultpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DEFAULT"),
                "");
        
        ratepanel = new RatePanel();
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(defaultpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(datecreatedpanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_RATE"), ratepanel);
        
        load();
    }

    @Override
    protected void load() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("currencies/" + id);
               
                return true;
                
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String number : chunks) {
                upperpanel.setNotifLabel(number);
                }
            }
            
            @Override
            protected void done() {
                upperpanel.hideProgressBar();
                
                if(response == null){
                    upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(response.getStatus() != 200) {
                    upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                    + response.getStatus());
                }
                else{
                    upperpanel.setNotifLabel("");
                    
                    String output = response.getEntity(String.class);
                    
                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        currency = mapper.readValue(output, Currency.class);
                        
                        idpanel.setContentValue(String.valueOf(currency.getId()));
                        codepanel.setContentValue(currency.getCode());
                        namepanel.setContentValue(currency.getName());
                        descriptionpanel.setContentValue(currency.getDescription());
                        notepanel.setContentValue(currency.getNote());
                        defaultpanel.setContentValue(currency.isDefaultcurrency());
                        statuspanel.setContentValue(currency.isStatus());
                        datecreatedpanel.setContentValue(currency.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(currency.getFormattedLastmodified());
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }

    @Override
    protected void home() {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new CurrencyHome());
    }

    @Override
    protected void edit() {
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new CurrencyEdit(currency.getId()));
    }
    
}