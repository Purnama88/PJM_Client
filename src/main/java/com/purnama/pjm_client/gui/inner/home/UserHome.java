/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.UserDetail;
import com.purnama.pjm_client.gui.inner.form.UserAdd;
import com.purnama.pjm_client.gui.inner.form.UserEdit;
import com.purnama.pjm_client.model.nontransactional.User;
import com.purnama.pjm_client.model.pagination.UserPagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.UserTableModel;
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
public class UserHome extends HomePanel{
    
    private final UserTableModel usertablemodel;
    
    public UserHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_USER_HOME"));
         
        usertablemodel = new UserTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(usertablemodel);
        
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
                
                response = RestClient.get("users?itemperpage=" + itemperpage + "&page=" + page +
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

                    UserPagination user_pagination;
                    
                    try{
                        user_pagination = mapper.readValue(output, UserPagination.class);
                        
                        totalpages = user_pagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        usertablemodel.setUserList(user_pagination.getUsers());
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
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new UserAdd());
    }

    @Override
    protected void detail() {
        User user = usertablemodel.getUser(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new UserDetail(user.getId()));
    }

    @Override
    protected void edit() {
        User user = usertablemodel.getUser(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new UserEdit(user.getId()));
    }
    
}
