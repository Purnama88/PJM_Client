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
import com.purnama.pjm_client.model.transactional.draft.ItemInvoiceSalesDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemInvoiceSalesDraftTableModel;
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
public class ItemInvoiceSalesDraftTablePanel extends TablePanel{
    
    private final ItemInvoiceSalesDraftTableModel iteminvoicesalesdrafttablemodel;
    
    private final PercentageTableCellEditor percentagecelleditor;
    
    private final int invoiceid;
    
    public ItemInvoiceSalesDraftTablePanel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        
        this.invoiceid = invoiceid;
        
        iteminvoicesalesdrafttablemodel = new ItemInvoiceSalesDraftTableModel(invoiceid, discountsubtotalpanel);
        
        percentagecelleditor = new PercentageTableCellEditor();
        
        table.setModel(iteminvoicesalesdrafttablemodel);
        
        init();
    }
    
    private void init(){
        load();
        
        TableColumn column4 = table.getColumnModel().getColumn(4);
        column4.setCellEditor(percentagecelleditor);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        menuitemdelete.addActionListener((ActionEvent e) -> {
            iteminvoicesalesdrafttablemodel.deleteRow(table.getSelectedRow());
        });
        
//        menuitemedit.addActionListener((ActionEvent e) -> {
//            iteminvoicedialog.showDialog();
//        });
    }
    
    @Override
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.get("iteminvoicesalesdrafts?invoiceid="+invoiceid);
                
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
                                        ItemInvoiceSalesDraft.class));
                        
                        iteminvoicesalesdrafttablemodel.setItemInvoiceSalesDraftList(list);
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitItemInvoiceSalesDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.put("iteminvoicesalesdrafts", 
                        iteminvoicesalesdrafttablemodel.getItemInvoiceSalesDraftList());
                
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
                                        ItemInvoiceSalesDraft.class));
                        
                        iteminvoicesalesdrafttablemodel.setItemInvoiceSalesDraftList(list);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public void submitDeletedItemInvoiceSalesDraftList(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.delete("iteminvoicesalesdrafts",
                        iteminvoicesalesdrafttablemodel.getDeletedItemInvoiceSalesDraftList());
                
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

    public ItemInvoiceSalesDraftTableModel getIteminvoicesalesdrafttablemodel() {
        return iteminvoicesalesdrafttablemodel;
    }
    
}
