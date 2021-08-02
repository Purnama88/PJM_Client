/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemDeliveryDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.DestinationPanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.DeliveryDraftHome;
import com.purnama.pjm_client.model.nontransactional.Menu;
import com.purnama.pjm_client.model.transactional.draft.DeliveryDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class DeliveryDraftDetail extends InvoiceDraftDetailPanel{
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final LabelTextFieldPanel cartypepanel, carnumberpanel;
    
    private final DestinationPanel destinationpanel;
    
    private final ItemDeliveryDraftTablePanel itemdeliverydrafttablepanel;
    
    private DeliveryDraft deliverydraft;
    
    private final int deliverydraftid;
    
    public DeliveryDraftDetail(int deliverydraftid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_DELIVERYDRAFT_DETAIL"));
        
        this.deliverydraftid = deliverydraftid;
        
        itemdeliverydrafttablepanel = new ItemDeliveryDraftTablePanel(deliverydraftid);
        
        destinationpanel = new DestinationPanel("");
        
        numberingpanel = new NumberingComboBoxPanel(Menu.MENU_DELIVERY);
        cartypepanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CARTYPE") ,"");
        carnumberpanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CARNUMBER"), "");
        
        init();
    }
    
    private void init(){
        iteminvoicedialog.setTablemodel(itemdeliverydrafttablepanel.getItemdeliverydrafttablemodel());
        
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(cartypepanel);
        middledetailpanel.add(carnumberpanel);
        
        rightdetailpanel.setLayout(new GridLayout(1,1));
        rightdetailpanel.add(destinationpanel);
        
        rightsummarypanel.removeAll();
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemdeliverydrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            itemdeliverydrafttablepanel.submitItemDeliveryDraftList();
            itemdeliverydrafttablepanel.submitDeletedItemDeliveryDraftList();
            JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                    GlobalFields.PROPERTIES.getProperty("NOTIFICATION_SAVED"),
                "", JOptionPane.INFORMATION_MESSAGE);
        });
        
        deletebutton.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, GlobalFields.
                PROPERTIES.getProperty("QUESTION_DELETEINVOICE"),
                "", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    delete();
                }
        });
        
        closebutton.addActionListener((ActionEvent e) -> {
            save();
            itemdeliverydrafttablepanel.submitItemDeliveryDraftList();
            itemdeliverydrafttablepanel.submitDeletedItemDeliveryDraftList();
            int result = JOptionPane.showConfirmDialog(null, GlobalFields.
                PROPERTIES.getProperty("QUESTION_CLOSEINVOICE"),
                "", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    close();
                }
        });
        
        load();
    }

    @Override
    protected void load() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("deliverydrafts/" + deliverydraftid);
                
                return true;
            }
            
            @Override
            protected void process(List<String> chunks) {
                chunks.stream().forEach((number) -> {
                    upperpanel.setNotifLabel(number);
                });
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
                        deliverydraft = mapper.readValue(output, DeliveryDraft.class);
                        
                        notepanel.setNote(deliverydraft.getNote());
                        datepanel.setDate(deliverydraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(deliverydraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(deliverydraft.getDraftid());
                        destinationpanel.setDestination(deliverydraft.getDestination());
                        notepanel.setNote(deliverydraft.getNote());
                        cartypepanel.setTextFieldValue(deliverydraft.getVehicletype());
                        carnumberpanel.setTextFieldValue(deliverydraft.getVehiclecode());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new DeliveryDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                deliverydraft.setInvoicedate(datepanel.getDate());
                deliverydraft.setNote(notepanel.getNote());
                deliverydraft.setNumbering(numberingpanel.getComboBoxValue());
                deliverydraft.setDestination(destinationpanel.getDestination());
                deliverydraft.setVehiclecode(carnumberpanel.getTextFieldValue());
                deliverydraft.setVehicletype(cartypepanel.getTextFieldValue());

                response = RestClient.put("deliverydrafts", deliverydraft);

                return true;
            }

            @Override
            protected void process(List<String> chunks) {
                chunks.stream().forEach((number) -> {
                    upperpanel.setNotifLabel(number);
                });
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
                }
            }
        };
        saveworker.execute();
    }

    @Override
    protected void close() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

//                response = RestClient.post("deliveries/" + deliverydraft.getId(), "");

                return true;
            }

            @Override
            protected void process(List<String> chunks) {
                chunks.stream().forEach((number) -> {
                    upperpanel.setNotifLabel(number);
                });
            }
            
            @Override
            protected void done() {
                upperpanel.hideProgressBar();
                
                if(response == null){
                    upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                    
                    System.out.println(getIndex());
                    GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new DeliveryDetail(1));
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
                    
                    GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new DeliveryDetail(Integer.parseInt(output)));
                }
            }
        };
        saveworker.execute();
    }

    @Override
    public void refresh() {
        numberingpanel.refresh();
        itemdeliverydrafttablepanel.refresh();
        load();
    }

    @Override
    protected void delete() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                response = RestClient.delete("deliverydrafts/" + deliverydraft.getId(), "");

                return true;
            }

            @Override
            protected void process(List<String> chunks) {
                chunks.stream().forEach((number) -> {
                    upperpanel.setNotifLabel(number);
                });
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
                    home();
                }
            }
        };
        saveworker.execute();
    }

    @Override
    protected void additem() {
        iteminvoicedialog.showDialog();
    }

    @Override
    protected void _import() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void export() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
