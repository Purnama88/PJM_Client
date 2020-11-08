/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.home.HomePanel;
import com.purnama.pjm_client.gui.inner.home.NumberingHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Menu;
import com.purnama.pjm_client.model.pagination.NumberingPagination;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.NumberingTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class NumberingDetail extends DetailPanel{
    
    private Menu menu;
    
    private final SelectableLabelContentPanel namepanel;
    
    private final NumberingTablePanel numberingtable;
    
    private final int id;
    
    public NumberingDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_NUMBERING_DETAIL"));
        
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        
        numberingtable = new NumberingTablePanel(id);
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(namepanel);
        detailpanel.add(notepanel);
        detailpanel.add(datecreatedpanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_NUMBERING"), numberingtable);
        
        load();
    }
    
    @Override
    protected void load() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("menus/" + id);
               
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
                        menu = mapper.readValue(output, Menu.class);
                        
                        idpanel.setContentValue(String.valueOf(menu.getId()));
                        namepanel.setContentValue(menu.getName());
                        notepanel.setContentValue(menu.getNote());
                        datecreatedpanel.setContentValue(menu.getFormattedCreateddate());
                        lastmodifiedpanel.setContentValue(menu.getFormattedLastmodified());
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }

    @Override
    protected void home() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new NumberingHome());
    }

    @Override
    protected void edit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class NumberingTablePanel extends HomePanel{

    private int menuid;
    
    private final NumberingTableModel numberingtablemodel;
    
    public NumberingTablePanel(int menuid) {
        super("");
        
        this.menuid = menuid;
        
        numberingtablemodel = new NumberingTableModel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        table.setModel(numberingtablemodel);
        
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
                
                response = RestClient.get("numberings?itemperpage=" + itemperpage + "&page=" + page +
                        "&keyword=" + keyword + "&menuid=" + menuid);
                
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

                    NumberingPagination numberingpagination;
                    
                    try{
                        numberingpagination = mapper.readValue(output, NumberingPagination.class);
                        
                        totalpages = numberingpagination.getTotalpages();
                        
                        upperpanel.setCurrentPageLabel(page + "");
                        upperpanel.setTotalPageLabel(totalpages + "");
                        
                        numberingtablemodel.setNumberingList(numberingpagination.getNumberings());
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        worker.execute();
    }

    @Override
    protected void add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void detail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void edit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
        
        
//        extends MainPanel{
//    
//    private final Menu menu;
//    
//    private final MyTable table;
//    
//    private final NumberingTableModel numberingtablemodel;
//    
//    protected final MyScrollPane numberingscrollpane;
//    
//    public NumberingTablePanel(){
//        super("");
//        
////        this.menu = menu;
//        
//        table = new MyTable();
//        
//        numberingtablemodel = new NumberingTableModel();
//        
//        numberingscrollpane = new MyScrollPane();
//        
//        init();
//        
//        menu = new Menu();
//    }
//    
//    private void init(){
//        numberingscrollpane.getViewport().add(table);
//        numberingscrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        table.setModel(numberingtablemodel);
//    }
//    
//    private void load(){
//        
//    }
//}
