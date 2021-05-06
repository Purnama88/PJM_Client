/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.ModelDetail;
import com.purnama.pjm_client.gui.inner.form.ModelAdd;
import com.purnama.pjm_client.gui.inner.form.ModelEdit;
import com.purnama.pjm_client.model.nontransactional.Model;
import com.purnama.pjm_client.model.pagination.ModelPagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ModelTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ModelHome extends HomePanel{
    
    private final ModelTableModel modeltablemodel;
    
    public ModelHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_MODEL_HOME"));
         
        modeltablemodel = new ModelTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        
        table.setModel(modeltablemodel);
        
//        table.setRowSorter(sorter);
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
                
                response = RestClient.get("models?itemperpage=" + itemperpage + "&page=" + page +
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

                    ModelPagination model_pagination;
                    
                    try{
                        model_pagination = mapper.readValue(output, ModelPagination.class);
                        
                        totalpages = model_pagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        modeltablemodel.setModelList(model_pagination.getModels());
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
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new ModelAdd());
    }

    @Override
    protected void detail() {
        Model model = modeltablemodel.getModel(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new ModelDetail(model.getId()));
    }

    @Override
    protected void edit() {
        Model model = modeltablemodel.getModel(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new ModelEdit(model.getId()));
    }
    
}
