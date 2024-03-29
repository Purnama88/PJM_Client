/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.PartnerGroupDetail;
import com.purnama.pjm_client.gui.inner.form.PartnerGroupAdd;
import com.purnama.pjm_client.gui.inner.form.PartnerGroupEdit;
import com.purnama.pjm_client.model.nontransactional.PartnerGroup;
import com.purnama.pjm_client.model.pagination.Pagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.PartnerGroupTableModel;
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
public class PartnerGroupHome extends HomePanel{
    
    private final PartnerGroupTableModel partnergrouptablemodel;
    
    public PartnerGroupHome() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNERGROUP_HOME"));
        
        partnergrouptablemodel = new PartnerGroupTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(partnergrouptablemodel);
        
//        table.setRowSorter(sorter);
    }

    @Override
    protected void load() {
        String keyword = upperpanel.getSearchKeyword();
        int partnerperpage = GlobalFields.ITEM_PER_PAGE;
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("partnergroups?itemperpage=" + partnerperpage + "&page=" + page +
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

                    try{
                        Pagination<PartnerGroup> partnergrouppagination = mapper.readValue(output, new TypeReference<Pagination<PartnerGroup>>() {});
                        
                        totalpages = partnergrouppagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        partnergrouptablemodel.setPartnerGroupList(partnergrouppagination.getList());
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
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new PartnerGroupAdd());
    }

    @Override
    protected void detail() {
        PartnerGroup partnergroup = partnergrouptablemodel.getPartnerGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new PartnerGroupDetail(partnergroup.getId()));
    }

    @Override
    protected void edit() {
        PartnerGroup partnergroup = partnergrouptablemodel.getPartnerGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(getIndex()+1, new PartnerGroupEdit(partnergroup.getId()));
    }
    
}
