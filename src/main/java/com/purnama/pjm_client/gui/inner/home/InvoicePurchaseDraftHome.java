/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.InvoicePurchaseDraftDetail;
import com.purnama.pjm_client.model.transactional.draft.InvoicePurchaseDraft;
import com.purnama.pjm_client.model.pagination.InvoicePurchaseDraftPagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.InvoicePurchaseDraftTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class InvoicePurchaseDraftHome extends HomePanel{
    
    private final InvoicePurchaseDraftTableModel invoicepurchasedrafttablemodel;
    
    public InvoicePurchaseDraftHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_INVOICEPURCHASEDRAFT_HOME"));
         
        invoicepurchasedrafttablemodel = new InvoicePurchaseDraftTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(invoicepurchasedrafttablemodel);
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
                
                response = RestClient.get("invoicepurchasedrafts?itemperpage=" + itemperpage + "&page=" + page +
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

                    InvoicePurchaseDraftPagination invoicepurchasedraft_pagination;
                    
                    try{
                        invoicepurchasedraft_pagination = mapper.readValue(output, InvoicePurchaseDraftPagination.class);
                        
                        totalpages = invoicepurchasedraft_pagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        invoicepurchasedrafttablemodel.setInvoicePurchaseDraftList(invoicepurchasedraft_pagination.getInvoicepurchasedrafts());
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
                
                response = RestClient.post("invoicepurchasedrafts", "");
                
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
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new InvoicePurchaseDraftDetail(id));
    }
    
    @Override
    protected void detail() {
        InvoicePurchaseDraft invoicepurchasedraft = invoicepurchasedrafttablemodel.getInvoicePurchaseDraft(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new InvoicePurchaseDraftDetail(invoicepurchasedraft.getId()));
    }

    @Override
    protected void edit() {
        InvoicePurchaseDraft invoicepurchasedraft = invoicepurchasedrafttablemodel.getInvoicePurchaseDraft(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new InvoicePurchaseDraftDetail(invoicepurchasedraft.getId()));
    }
    
}
