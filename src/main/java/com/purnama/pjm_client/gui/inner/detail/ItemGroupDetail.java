/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.ItemSearchPanel;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.ItemGroupEdit;
import com.purnama.pjm_client.gui.inner.home.ItemGroupHome;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
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
public class ItemGroupDetail extends DetailPanel{
    
    private ItemGroup itemgroup;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel;
    
    private final int id;
    
    private final ItemSearchPanel itemsearchpanel;
    
    public ItemGroupDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_ITEMGROUP_DETAIL"));
    
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        
        itemsearchpanel = new ItemSearchPanel(id);
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(lastmodifiedpanel);
        
        itemsearchpanel.setIndex(getIndex());
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_ITEM"), itemsearchpanel);
        
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
                
                response = RestClient.get("itemgroups/" + id);
               
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
                        itemgroup = mapper.readValue(output, ItemGroup.class);
                        
                        idpanel.setContentValue(String.valueOf(itemgroup.getId()));
                        codepanel.setContentValue(itemgroup.getCode());
                        namepanel.setContentValue(itemgroup.getName());
                        descriptionpanel.setContentValue(itemgroup.getDescription());
                        notepanel.setContentValue(itemgroup.getNote());
                        statuspanel.setContentValue(itemgroup.isStatus());
                        datecreatedpanel.setContentValue(itemgroup.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(itemgroup.getFormattedLastmodified());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ItemGroupHome());
    }

    @Override
    protected void edit() {
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new ItemGroupEdit(itemgroup.getId()));
        System.out.println(getIndex());
    }
    
}