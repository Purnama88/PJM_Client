/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.ExpensesDraftDetail;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.transactional.draft.ExpensesDraft;
import com.purnama.pjm_client.model.pagination.ExpensesDraftPagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ExpensesDraftTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ExpensesDraftHome extends HomePanel{
    
    private final ExpensesDraftTableModel expensesdrafttablemodel;
    
    public ExpensesDraftHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_EXPENSESDRAFT_HOME"));
         
        expensesdrafttablemodel = new ExpensesDraftTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(expensesdrafttablemodel);
    }

    @Override
    protected void load() {
        String keyword = upperpanel.getSearchKeyword();
        int itemperpage = GlobalFields.ITEM_PER_PAGE;
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("expensesdrafts?itemperpage=" + itemperpage + "&page=" + page +
                        "&keyword=" + keyword);
                
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

                    ExpensesDraftPagination expensesdraft_pagination;
                    
                    try{
                        expensesdraft_pagination = mapper.readValue(output, ExpensesDraftPagination.class);
                        
                        totalpages = expensesdraft_pagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        expensesdrafttablemodel.setExpensesDraftList(expensesdraft_pagination.getExpensesdrafts());
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
            }
        };
        worker.execute();
    }

    @Override
    protected void add() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.post("expensesdrafts", "");
                
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
                    upperpanel.setNotifLabel("");
                    
                    String output = response.getEntity(String.class);
                        
                    JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                    output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                    + response.getStatus(), 
                    JOptionPane.ERROR_MESSAGE);
                }
                else{
                    upperpanel.setNotifLabel("");
                    
                    String output = response.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();
                        
                    try{
                        int id  = mapper.readValue(output, Integer.class);
                        detail(id);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        
        worker.execute();
    }

    protected void detail(int id){
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new ExpensesDraftDetail(id));
    }
    
    @Override
    protected void detail() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        ExpensesDraft expensesdraft = expensesdrafttablemodel.getExpensesDraft(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        tabbedPane.insertTab(this.getIndex()+1, new ExpensesDraftDetail(expensesdraft.getId()));
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        ExpensesDraft expensesdraft = expensesdrafttablemodel.getExpensesDraft(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        tabbedPane.insertTab(this.getIndex()+1, new ExpensesDraftDetail(expensesdraft.getId()));
    }
    
}
