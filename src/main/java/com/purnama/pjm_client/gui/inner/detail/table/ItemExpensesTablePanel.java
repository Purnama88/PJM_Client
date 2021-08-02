/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.model.transactional.ItemExpenses;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemExpensesTableModel;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemExpensesTablePanel extends TablePanel{

    private final ItemExpensesTableModel itemexpensestablemodel;
    
    private final int expensesid;
    
    public ItemExpensesTablePanel(int expensesid){
        
        this.expensesid = expensesid;
        
        itemexpensestablemodel = new ItemExpensesTableModel();
        
        table.setModel(itemexpensestablemodel);
        
        init();
    }
    
    private void init(){
        table.removeMenuItem(menuitemdelete);
        
        load();
    }
    
    @Override
    public void load() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                response = RestClient.get("itemexpenses?expensesid="+expensesid);
                
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
                                        ItemExpenses.class));
                        
                        itemexpensestablemodel.setItemExpensesList(list);
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
}
