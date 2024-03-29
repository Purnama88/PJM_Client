/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemDeliveryTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.DestinationPanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelTextFieldPanel;
import com.purnama.pjm_client.gui.inner.home.DeliveryHome;
import com.purnama.pjm_client.model.transactional.Delivery;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class DeliveryDetail extends InvoiceDetailPanel{

    private final LabelTextFieldPanel cartypepanel, carnumberpanel;
    
    private final DestinationPanel destinationpanel;
    
    private final ItemDeliveryTablePanel itemdeliverytablepanel;
    
    private Delivery delivery;
    
    private final int deliveryid;
    
    public DeliveryDetail(int deliveryid) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_DELIVERY_DETAIL"));
        
        this.deliveryid = deliveryid;
        
        itemdeliverytablepanel = new ItemDeliveryTablePanel(deliveryid);
        
        destinationpanel = new DestinationPanel("");
        
        cartypepanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CARTYPE") ,"");
        carnumberpanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CARNUMBER"), "");
        
        init();
    }
    
    private void init(){
        
        cartypepanel.setTextFieldEnabled(false);
        carnumberpanel.setTextFieldEnabled(false);
        destinationpanel.setTextAreaEnabled(false);
        
        middledetailpanel.add(cartypepanel);
        middledetailpanel.add(carnumberpanel);
        
        rightdetailpanel.setLayout(new GridLayout(1,1));
        rightdetailpanel.add(destinationpanel);
        
        rightsummarypanel.removeAll();
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemdeliverytablepanel);
        
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
                
                response = RestClient.get("deliveries/" + deliveryid);
                
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
                        delivery = mapper.readValue(output, Delivery.class);
                        
                        notepanel.setNote(delivery.getNote());
                        datepanel.setDate(delivery.getInvoicedate());
                        warehousepanel.setTextFieldValue(delivery.getWarehouse().getCode());
                        idpanel.setTextFieldValue(delivery.getDraftid());
                        destinationpanel.setDestination(delivery.getDestination());
                        cartypepanel.setTextFieldValue(delivery.getVehicletype());
                        carnumberpanel.setTextFieldValue(delivery.getVehiclecode());
                        numberingpanel.setTextFieldValue(delivery.getNumber());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new DeliveryHome());
    }

    @Override
    protected void save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void cancel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void _import() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void export() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh() {
        load();
        itemdeliverytablepanel.refresh();
    }
}