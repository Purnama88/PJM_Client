/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemExpensesDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.CurrencyComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.ExpensesDraftHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.transactional.draft.ExpensesDraft;
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
public class ExpensesDraftDetail extends InvoiceDraftDetailPanel{

    private final PartnerComboBoxPanel partnerpanel;
    
    private final CurrencyComboBoxPanel currencypanel;
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final DatePanel duedatepanel;
    
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final ItemExpensesDraftTablePanel itemexpensesdrafttablepanel;
    
    private ExpensesDraft expensesdraft;
    
    private final int invoiceid;
    
    public ExpensesDraftDetail(int invoiceid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_EXPENSESDRAFT_DETAIL"));
        
        this.invoiceid = invoiceid;
        
        partnerpanel = new PartnerComboBoxPanel(PartnerComboBoxPanel.CUSTOMER);
        numberingpanel = new NumberingComboBoxPanel(2);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        itemexpensesdrafttablepanel = new ItemExpensesDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemexpensesdrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            itemexpensesdrafttablepanel.submitItemExpensesDraftList();
            itemexpensesdrafttablepanel.submitDeletedItemExpensesDraftList();
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
                
                response = RestClient.get("expensesdrafts/" + invoiceid);
                
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
                        expensesdraft = mapper.readValue(output, ExpensesDraft.class);
                        
                        notepanel.setNote(expensesdraft.getNote());
                        datepanel.setDate(expensesdraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(expensesdraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(expensesdraft.getDraftid());
                        partnerpanel.setComboBoxValue(expensesdraft.getPartner());
                        duedatepanel.setDate(expensesdraft.getDuedate());
                        numberingpanel.setComboBoxValue(expensesdraft.getNumbering());
                        currencypanel.setComboBoxValue(expensesdraft.getCurrency());
                        ratepanel.setTextFieldValue(expensesdraft.getRate());
                        
                        expensespanel.setRounding(expensesdraft.getRounding());
                        expensespanel.setFreight(expensesdraft.getFreight());
                        expensespanel.setTax(expensesdraft.getTax());
                        
                        discountsubtotalpanel.setSubtotal(expensesdraft.getSubtotal());
                        discountsubtotalpanel.setDiscount(expensesdraft.getDiscount());
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
        
        tabbedPane.changeTabPanel(getIndex(), new ExpensesDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                expensesdraft.setCurrency(currencypanel.getComboBoxValue());
                expensesdraft.setInvoicedate(datepanel.getDate());
                expensesdraft.setDiscount(discountsubtotalpanel.getDiscount());
                expensesdraft.setDuedate(duedatepanel.getDate());
                expensesdraft.setFreight(expensespanel.getFreight());
                expensesdraft.setNote(notepanel.getNote());
                expensesdraft.setNumbering(numberingpanel.getComboBoxValue());
                expensesdraft.setPartner(partnerpanel.getComboBoxValue());
                expensesdraft.setRate(ratepanel.getTextFieldValue());
                expensesdraft.setRounding(expensespanel.getRounding());
                expensesdraft.setStatus(true);
                expensesdraft.setSubtotal(discountsubtotalpanel.getSubtotal());
                expensesdraft.setTax(expensespanel.getTax());

                response = RestClient.put("expensesdrafts", expensesdraft);

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
        
        numberingpanel.refresh();
        partnerpanel.refresh();
        currencypanel.refresh();
        
        load();
    }
}
