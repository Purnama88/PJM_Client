/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.WarehouseDetail;
import com.purnama.pjm_client.gui.inner.form.WarehouseAdd;
import com.purnama.pjm_client.gui.inner.form.WarehouseEdit;
import com.purnama.pjm_client.model.nontransactional.Warehouse;
import com.purnama.pjm_client.model.pagination.WarehousePagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.WarehouseTableModel;
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
public class WarehouseHome extends HomePanel{
    
    private final WarehouseTableModel warehousetablemodel;
    
    public WarehouseHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_WAREHOUSE_HOME"));
        
        warehousetablemodel = new WarehouseTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(warehousetablemodel);
        
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
                
                response = RestClient.get("warehouses?itemperpage=" + itemperpage + "&page=" + page +
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

                    WarehousePagination warehouse_pagination;
                    
                    try{
                        warehouse_pagination = mapper.readValue(output, WarehousePagination.class);
                        
                        totalpages = warehouse_pagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        warehousetablemodel.setWarehouseList(warehouse_pagination.getWarehouses());
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
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new WarehouseAdd());
    }

    @Override
    protected void detail() {
        Warehouse warehouse = warehousetablemodel.getWarehouse(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new WarehouseDetail(warehouse.getId()));
    }

    @Override
    protected void edit() {
        Warehouse warehouse = warehousetablemodel.getWarehouse(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new WarehouseEdit(warehouse.getId()));
    }
    
}
