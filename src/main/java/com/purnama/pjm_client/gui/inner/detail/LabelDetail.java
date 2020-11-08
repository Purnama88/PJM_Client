/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.LabelEdit;
import com.purnama.pjm_client.gui.inner.home.LabelHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Label;
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
public class LabelDetail extends DetailPanel{
    
    private Label label;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel;
    
    private final int id;
    
    public LabelDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_LABEL_DETAIL"));
        
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
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
                
                response = RestClient.get("labels/" + id);
               
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
                        label = mapper.readValue(output, Label.class);
                        
                        idpanel.setContentValue(String.valueOf(label.getId()));
                        codepanel.setContentValue(label.getCode());
                        namepanel.setContentValue(label.getName());
                        descriptionpanel.setContentValue(label.getDescription());
                        notepanel.setContentValue(label.getNote());
                        statuspanel.setContentValue(label.isStatus());
                        datecreatedpanel.setContentValue(label.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(label.getFormattedLastmodified());
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
        
        tabbedPane.changeTabPanel(getIndex(), new LabelHome());
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new LabelEdit(label.getId()));
    }
    
}
