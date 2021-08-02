/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerSearchPanel;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.PartnerGroupEdit;
import com.purnama.pjm_client.gui.inner.home.PartnerGroupHome;
import com.purnama.pjm_client.model.nontransactional.PartnerGroup;
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
public class PartnerGroupDetail extends DetailPanel{
    
    private PartnerGroup partnergroup;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel;
    
    private final int id;
    
    private final PartnerSearchPanel partnersearchpanel;
    
    public PartnerGroupDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNERGROUP_DETAIL"));
    
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        
        partnersearchpanel = new PartnerSearchPanel(id);
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNER"), partnersearchpanel);
        
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
                
                response = RestClient.get("partnergroups/" + id);
               
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
                        partnergroup = mapper.readValue(output, PartnerGroup.class);
                        
                        idpanel.setContentValue(String.valueOf(partnergroup.getId()));
                        codepanel.setContentValue(partnergroup.getCode());
                        namepanel.setContentValue(partnergroup.getName());
                        descriptionpanel.setContentValue(partnergroup.getDescription());
                        notepanel.setContentValue(partnergroup.getNote());
                        statuspanel.setContentValue(partnergroup.isStatus());
                        lastmodifiedpanel.setContentValue(partnergroup.getFormattedLastmodified());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new PartnerGroupHome());
    }

    @Override
    protected void edit() {
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new PartnerGroupEdit(partnergroup.getId()));
    }
    
}
