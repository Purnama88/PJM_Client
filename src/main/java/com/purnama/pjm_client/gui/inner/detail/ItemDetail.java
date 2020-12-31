/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.ItemGroupPanel;
import com.purnama.pjm_client.gui.inner.detail.util.ModelPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PricePanel;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.ItemEdit;
import com.purnama.pjm_client.gui.inner.home.ItemHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemDetail extends DetailPanel{
    
    private Item item;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel, costpanel, labelpanel;
    
    private final ItemGroupPanel itemgrouppanel;
    
    private final ModelPanel modelpanel;
    
    private final PricePanel pricepanel;
    
    private final int id;
    
    public ItemDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_ITEM_DETAIL"));
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        costpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_COST"),
                "");
        labelpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_LABEL"),
                "");
        
        itemgrouppanel = new ItemGroupPanel();
        modelpanel = new ModelPanel();
        pricepanel = new PricePanel();
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(costpanel);
        detailpanel.add(labelpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(datecreatedpanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_PRICE"), pricepanel);
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_MODEL"), modelpanel);
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_ITEMGROUP"), itemgrouppanel);
        
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
                
                response = RestClient.get("items/" + id);
               
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
                        item = mapper.readValue(output, Item.class);
                        
                        idpanel.setContentValue(String.valueOf(item.getId()));
                        codepanel.setContentValue(item.getCode());
                        namepanel.setContentValue(item.getName());
                        descriptionpanel.setContentValue(item.getDescription());
                        costpanel.setContentValue(item.getFormattedCost());
                        labelpanel.setContentValue(item.getLabel().getName());
                        notepanel.setContentValue(item.getNote());
                        statuspanel.setContentValue(item.isStatus());
                        datecreatedpanel.setContentValue(item.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(item.getFormattedLastmodified());
                        
                        pricepanel.load(item);
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
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new ItemHome());
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new ItemEdit(item.getId()));
    }
    
}

