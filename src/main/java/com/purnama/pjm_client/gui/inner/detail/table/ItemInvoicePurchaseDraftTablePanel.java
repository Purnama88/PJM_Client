/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PercentageTableCellEditor;
import com.purnama.pjm_client.model.transactional.draft.ItemInvoicePurchaseDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemInvoicePurchaseDraftTableModel;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;

/**
 *
 * @author p_cor
 */
public class ItemInvoicePurchaseDraftTablePanel extends TablePanel{
    
    private final ItemInvoicePurchaseDraftTableModel iteminvoicepurchasedrafttablemodel;
    
    private final PercentageTableCellEditor percentagecelleditor;
    
    private final int invoiceid;
    
    public ItemInvoicePurchaseDraftTablePanel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        
        this.invoiceid = invoiceid;
        
        iteminvoicepurchasedrafttablemodel = new ItemInvoicePurchaseDraftTableModel(invoiceid, discountsubtotalpanel);
        
        percentagecelleditor = new PercentageTableCellEditor();
        
        table.setModel(iteminvoicepurchasedrafttablemodel);
        
        init();
    }
    
    private void init(){
        load();
        
        TableColumn column4 = table.getColumnModel().getColumn(4);
        column4.setCellEditor(percentagecelleditor);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        menuitemdelete.addActionListener((ActionEvent e) -> {
            iteminvoicepurchasedrafttablemodel.deleteRow(table.getSelectedRow());
        });
    }
    
    @Override
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.get("iteminvoicepurchasedrafts?invoiceid="+invoiceid);
                
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
                                        ItemInvoicePurchaseDraft.class));
                        
                        iteminvoicepurchasedrafttablemodel.setItemInvoicePurchaseDraftList(list);
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitItemInvoicePurchaseDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.put("iteminvoicepurchasedrafts", 
                        iteminvoicepurchasedrafttablemodel.getItemInvoicePurchaseDraftList());
                
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
                                        ItemInvoicePurchaseDraft.class));
                        
                        iteminvoicepurchasedrafttablemodel.setItemInvoicePurchaseDraftList(list);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitDeletedItemInvoicePurchaseDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.delete("iteminvoicepurchasedrafts",
                        iteminvoicepurchasedrafttablemodel.getDeletedItemInvoicePurchaseDraftList());
                
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
