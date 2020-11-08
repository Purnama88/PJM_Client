/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.PartnerEdit;
import com.purnama.pjm_client.gui.inner.home.PartnerHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Partner;
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
public class PartnerDetail extends DetailPanel{
    
    private Partner partner;
    
    private final SelectableLabelContentPanel codepanel, namepanel, contactnamepanel, addresspanel, 
            phonenumberpanel, phonenumber2panel, mobilenumberpanel, mobilenumber2panel,
            faxnumberpanel, faxnumber2panel, emailpanel, email2panel, customerpanel, supplierpanel, nontradepanel;
    
    private final int id;
    
    public PartnerDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNER_DETAIL"));
        
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        contactnamepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CONTACTNAME"),
                "");
        addresspanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ADDRESS"),
                "");
        phonenumberpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_PHONENUMBER"),
                "");
        phonenumber2panel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_PHONENUMBER"),
                "");
        mobilenumberpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_MOBILENUMBER"),
                "");
        mobilenumber2panel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_MOBILENUMBER"),
                "");
        faxnumberpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_FAXNUMBER"),
                "");
        faxnumber2panel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_FAXNUMBER"),
                "");
        emailpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_EMAIL"),
                "");
        email2panel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_EMAIL"),
                "");
        customerpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CUSTOMER"),
                "");
        supplierpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SUPPLIER"),
                "");
        nontradepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NONTRADE"),
                "");
        
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(contactnamepanel);
        detailpanel.add(addresspanel);
        detailpanel.add( phonenumberpanel);
        detailpanel.add(phonenumber2panel);
        detailpanel.add(mobilenumberpanel);
        detailpanel.add(mobilenumber2panel);
        detailpanel.add(faxnumberpanel);
        detailpanel.add(faxnumber2panel);
        detailpanel.add(emailpanel);
        detailpanel.add(email2panel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(customerpanel);
        detailpanel.add(supplierpanel);
        detailpanel.add(nontradepanel);
        detailpanel.add(datecreatedpanel);
        detailpanel.add(lastmodifiedpanel);
        
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
                
                response = RestClient.get("partners/" + id);
               
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
                        partner = mapper.readValue(output, Partner.class);
                        
                        idpanel.setContentValue(String.valueOf(partner.getId()));
                        codepanel.setContentValue(partner.getCode());
                        namepanel.setContentValue(partner.getName());
                        contactnamepanel.setContentValue(partner.getContactname());
                        addresspanel.setContentValue(partner.getAddress());
                        phonenumberpanel.setContentValue(partner.getPhonenumber());
                        phonenumber2panel.setContentValue(partner.getPhonenumber2());
                        mobilenumberpanel.setContentValue(partner.getMobilenumber());
                        mobilenumber2panel.setContentValue(partner.getMobilenumber2());
                        faxnumberpanel.setContentValue(partner.getFaxnumber());
                        faxnumber2panel.setContentValue(partner.getFaxnumber2());
                        emailpanel.setContentValue(partner.getEmail());
                        email2panel.setContentValue(partner.getEmail2());
                        notepanel.setContentValue(partner.getNote());
                        customerpanel.setContentValue(partner.isNontrade());
                        supplierpanel.setContentValue(partner.isSupplier());
                        nontradepanel.setContentValue(partner.isNontrade());
                        statuspanel.setContentValue(partner.isStatus());
                        datecreatedpanel.setContentValue(partner.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(partner.getFormattedLastmodified());
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
        
        tabbedPane.changeTabPanel(getIndex(), new PartnerHome());
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new PartnerEdit(partner.getId()));
    }
    
}
