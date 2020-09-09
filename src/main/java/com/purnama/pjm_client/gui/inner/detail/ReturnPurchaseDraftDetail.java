/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemReturnPurchaseDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.CurrencyComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.ReturnPurchaseDraftHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.transactional.draft.ReturnPurchaseDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ReturnPurchaseDraftDetail extends InvoiceDraftDetailPanel{

    private final PartnerComboBoxPanel partnerpanel;
    
    private final CurrencyComboBoxPanel currencypanel;
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final DatePanel duedatepanel;
    
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final ItemReturnPurchaseDraftTablePanel itemreturnpurchasedrafttablepanel;
    
    private ReturnPurchaseDraft returnpurchasedraft;
    
    private final int invoiceid;
    
    public ReturnPurchaseDraftDetail(int invoiceid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_RETURNPURCHASEDRAFT_DETAIL"));
        
        this.invoiceid = invoiceid;
        
        partnerpanel = new PartnerComboBoxPanel(PartnerComboBoxPanel.CUSTOMER);
        numberingpanel = new NumberingComboBoxPanel(2);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        itemreturnpurchasedrafttablepanel = new ItemReturnPurchaseDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemreturnpurchasedrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            itemreturnpurchasedrafttablepanel.submitItemReturnPurchaseDraftList();
            itemreturnpurchasedrafttablepanel.submitDeletedItemReturnPurchaseDraftList();
            JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                    GlobalFields.PROPERTIES.getProperty("NOTIFICATION_SAVED"),
                "", JOptionPane.INFORMATION_MESSAGE);
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
                
                response = RestClient.get("returnpurchasedrafts/" + invoiceid);
                
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
                        returnpurchasedraft = mapper.readValue(output, ReturnPurchaseDraft.class);
                        
                        notepanel.setNote(returnpurchasedraft.getNote());
                        datepanel.setDate(returnpurchasedraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(returnpurchasedraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(returnpurchasedraft.getDraftid());
                        partnerpanel.setComboBoxValue(returnpurchasedraft.getPartner());
                        duedatepanel.setDate(returnpurchasedraft.getDuedate());
                        numberingpanel.setComboBoxValue(returnpurchasedraft.getNumbering());
                        currencypanel.setComboBoxValue(returnpurchasedraft.getCurrency());
                        ratepanel.setTextFieldValue(returnpurchasedraft.getRate());
                        
                        expensespanel.setRounding(returnpurchasedraft.getRounding());
                        expensespanel.setFreight(returnpurchasedraft.getFreight());
                        expensespanel.setTax(returnpurchasedraft.getTax());
                        
                        discountsubtotalpanel.setSubtotal(returnpurchasedraft.getSubtotal());
                        discountsubtotalpanel.setDiscount(returnpurchasedraft.getDiscount());
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
        
        tabbedPane.changeTabPanel(getIndex(), new ReturnPurchaseDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                returnpurchasedraft.setCurrency(currencypanel.getComboBoxValue());
                returnpurchasedraft.setInvoicedate(datepanel.getDate());
                returnpurchasedraft.setDiscount(discountsubtotalpanel.getDiscount());
                returnpurchasedraft.setDuedate(duedatepanel.getDate());
                returnpurchasedraft.setFreight(expensespanel.getFreight());
                returnpurchasedraft.setNote(notepanel.getNote());
                returnpurchasedraft.setNumbering(numberingpanel.getComboBoxValue());
                returnpurchasedraft.setPartner(partnerpanel.getComboBoxValue());
                returnpurchasedraft.setRate(ratepanel.getTextFieldValue());
                returnpurchasedraft.setRounding(expensespanel.getRounding());
                returnpurchasedraft.setStatus(true);
                returnpurchasedraft.setSubtotal(discountsubtotalpanel.getSubtotal());
                returnpurchasedraft.setTax(expensespanel.getTax());

                response = RestClient.put("returnpurchasedrafts", returnpurchasedraft);

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void refresh(){
        
        partnerpanel.refresh();
        currencypanel.refresh();
        
        load();
    }
}
