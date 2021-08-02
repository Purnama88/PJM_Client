/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemReturnPurchaseTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelTextFieldPanel;
import com.purnama.pjm_client.gui.inner.home.ReturnPurchaseHome;
import com.purnama.pjm_client.model.transactional.ReturnPurchase;
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
public class ReturnPurchaseDetail extends InvoiceDetailPanel{

    private final LabelTextFieldPanel partnerpanel, currencypanel;
            
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final DatePanel duedatepanel;
    
    private final ItemReturnPurchaseTablePanel itemreturnpurchasetablepanel;
    
    private ReturnPurchase returnpurchase;
    
    private final int returnpurchaseid;
    
    public ReturnPurchaseDetail(int returnpurchaseid) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_RETURNPURCHASE_DETAIL"));
        
        this.returnpurchaseid = returnpurchaseid;
        
        partnerpanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_PARTNER"), "");
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        middledetailpanel.add(partnerpanel);
        middledetailpanel.add(duedatepanel);
        
        currencypanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CURRENCY"), "");
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        itemreturnpurchasetablepanel = new ItemReturnPurchaseTablePanel(returnpurchaseid);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"),
                itemreturnpurchasetablepanel);
        
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
                itemreturnpurchasetablepanel);
        
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
                
                response = RestClient.get("returnpurchases/" + returnpurchaseid);
                
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
                        returnpurchase = mapper.readValue(output, ReturnPurchase.class);
                        
                        notepanel.setNote(returnpurchase.getNote());
                        datepanel.setDate(returnpurchase.getInvoicedate());
                        duedatepanel.setDate(returnpurchase.getDuedate());
                        warehousepanel.setTextFieldValue(returnpurchase.getWarehouse().getCode());
                        idpanel.setTextFieldValue(returnpurchase.getDraftid());
                        partnerpanel.setTextFieldValue(returnpurchase.getPartnername()+ " (" + returnpurchase.getPartnercode()+")");
                        currencypanel.setTextFieldValue(returnpurchase.getCurrencycode());
                        numberingpanel.setTextFieldValue(returnpurchase.getNumber());
                        
                        expensespanel.setRounding(returnpurchase.getRounding());
                        expensespanel.setFreight(returnpurchase.getFreight());
                        expensespanel.setTax(returnpurchase.getTax());
                        
                        discountsubtotalpanel.setSubtotal(returnpurchase.getSubtotal());
                        discountsubtotalpanel.setDiscount(returnpurchase.getDiscount());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnPurchaseHome());
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
        itemreturnpurchasetablepanel.refresh();
    }

    
}