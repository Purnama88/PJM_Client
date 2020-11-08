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
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemGroupDetail extends DetailPanel{
    
    private ItemGroup itemgroup;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel;
    
    private final int id;
    
    private final ItemSelectPanel itemselectpanel;
    
    public ItemGroupDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_ITEMGROUP_DETAIL"));
    
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        
        itemselectpanel = new ItemSelectPanel();
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_ITEM"), itemselectpanel);
        
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
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new ItemGroupHome());
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new ItemGroupEdit(itemgroup.getId()));
    }
    
}

class ItemSelectPanel extends MyPanel{
    
    private final ItemSearchPanel itemsearchpanel;
    
    public ItemSelectPanel(){
        super(new BorderLayout());
        
        itemsearchpanel = new ItemSearchPanel();
        
        init();
    }
    
    private void init(){
        
        add(itemsearchpanel, BorderLayout.CENTER);
        
    }
}