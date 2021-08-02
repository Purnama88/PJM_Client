/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemReturnSalesTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelTextFieldPanel;
import com.purnama.pjm_client.gui.inner.home.ReturnSalesHome;
import com.purnama.pjm_client.model.transactional.ReturnSales;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ReturnSalesDetail extends InvoiceDetailPanel{

    private final LabelTextFieldPanel partnerpanel, currencypanel;
            
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final DatePanel duedatepanel;
    
    private final ItemReturnSalesTablePanel itemreturnsalestablepanel;
    
    private ReturnSales returnsales;
    
    private final int returnsalesid;
    
    public ReturnSalesDetail(int returnsalesid) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_RETURNSALES_DETAIL"));
        
        this.returnsalesid = returnsalesid;
        
        partnerpanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_PARTNER"), "");
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        middledetailpanel.add(partnerpanel);
        middledetailpanel.add(duedatepanel);
        
        currencypanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CURRENCY"), "");
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        itemreturnsalestablepanel = new ItemReturnSalesTablePanel(returnsalesid);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"),
                itemreturnsalestablepanel);
        
        init();
    }
    
    private void init(){
        duedatepanel.setEnabled(false);
        partnerpanel.setTextFieldEnabled(false);
        currencypanel.setTextFieldEnabled(false);
        ratepanel.setTextFieldEnabled(false);
        
        expensespanel.setTextFieldEnabled(false);
        
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemreturnsalestablepanel);
        
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
                
                response = RestClient.get("returnsales/" + returnsalesid);
                
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
                        returnsales = mapper.readValue(output, ReturnSales.class);
                        
                        notepanel.setNote(returnsales.getNote());
                        datepanel.setDate(returnsales.getInvoicedate());
                        duedatepanel.setDate(returnsales.getDuedate());
                        warehousepanel.setTextFieldValue(returnsales.getWarehouse().getCode());
                        idpanel.setTextFieldValue(returnsales.getDraftid());
                        partnerpanel.setTextFieldValue(returnsales.getPartnername()+ " (" + returnsales.getPartnercode()+")");
                        currencypanel.setTextFieldValue(returnsales.getCurrencycode());
                        numberingpanel.setTextFieldValue(returnsales.getNumber());
                        
                        expensespanel.setRounding(returnsales.getRounding());
                        expensespanel.setFreight(returnsales.getFreight());
                        expensespanel.setTax(returnsales.getTax());
                        
                        discountsubtotalpanel.setSubtotal(returnsales.getSubtotal());
                        discountsubtotalpanel.setDiscount(returnsales.getDiscount());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnSalesHome());
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
        itemreturnsalestablepanel.refresh();
    }
}