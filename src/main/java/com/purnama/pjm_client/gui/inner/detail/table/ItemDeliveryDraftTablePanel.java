/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.model.transactional.draft.ItemDeliveryDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemDeliveryDraftTableModel;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemDeliveryDraftTablePanel extends TablePanel{
    
    private final ItemDeliveryDraftTableModel itemdeliverydrafttablemodel;
    
    private final int deliveryid;
    
    public ItemDeliveryDraftTablePanel(int deliveryid){
        
        this.deliveryid = deliveryid;
        
        itemdeliverydrafttablemodel = new ItemDeliveryDraftTableModel(deliveryid);
        
        table.setModel(itemdeliverydrafttablemodel);
        
        init();
    }
    
    private void init(){
        load();
        
        menuitemdelete.addActionListener((ActionEvent e) -> {
            itemdeliverydrafttablemodel.deleteRow(table.getSelectedRow());
        });
    }
    
    @Override
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.get("itemdeliverydrafts?deliveryid="+deliveryid);
                
                return true;
            }
            
            @Override
            protected void done() {
                if(response == null){
                }
                else if(response.getStatus() != 200) {
                }
                else{
                    String output = response.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        ArrayList list = mapper.readValue(output,
                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                        ItemDeliveryDraft.class));
                        
                        itemdeliverydrafttablemodel.setItemDeliveryDraftList(list);
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitItemDeliveryDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.put("itemdeliverydrafts", 
                        itemdeliverydrafttablemodel.getItemDeliveryDraftList());
                
                return true;
            }
            
            @Override
            protected void done() {
                
                if(response == null){
                }
                else if(response.getStatus() != 200) {
                }
                else{
                    String output = response.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        ArrayList list = mapper.readValue(output,
                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                        ItemDeliveryDraft.class));
                        
                        itemdeliverydrafttablemodel.setItemDeliveryDraftList(list);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitDeletedItemDeliveryDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.delete("itemdeliverydrafts",
                        itemdeliverydrafttablemodel.getDeletedItemDeliveryDraftList());
                
                return true;
            }
            
            @Override
            protected void done() {
                
                if(response == null){
                }
                else if(response.getStatus() != 200) {
                }
                else{
                }
            }
        };
        
        worker.execute();
    }
    
}
