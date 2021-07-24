/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemInvoicePurchaseDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.CurrencyComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.InvoicePurchaseDraftHome;
import com.purnama.pjm_client.model.transactional.draft.InvoicePurchaseDraft;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class InvoicePurchaseDraftDetail extends InvoiceDraftDetailPanel{

    private final PartnerComboBoxPanel partnerpanel;
    
    private final CurrencyComboBoxPanel currencypanel;
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final DatePanel duedatepanel;
    
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final ItemInvoicePurchaseDraftTablePanel iteminvoicepurchasedrafttablepanel;
    
    private InvoicePurchaseDraft invoicepurchasedraft;
    
    private final int invoiceid;
    
    public InvoicePurchaseDraftDetail(int invoiceid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_INVOICEPURCHASEDRAFT_DETAIL"));
        
        this.invoiceid = invoiceid;
        
        partnerpanel = new PartnerComboBoxPanel(PartnerComboBoxPanel.CUSTOMER);
        numberingpanel = new NumberingComboBoxPanel(2);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        iteminvoicepurchasedrafttablepanel = new ItemInvoicePurchaseDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        iteminvoicedialog.setTablemodel(iteminvoicepurchasedrafttablepanel.getIteminvoicepurchasedrafttablemodel());
        
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                iteminvoicepurchasedrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            iteminvoicepurchasedrafttablepanel.submitItemInvoicePurchaseDraftList();
            iteminvoicepurchasedrafttablepanel.submitDeletedItemInvoicePurchaseDraftList();
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
                
                response = RestClient.get("invoicepurchasedrafts/" + invoiceid);
                
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
                        invoicepurchasedraft = mapper.readValue(output, InvoicePurchaseDraft.class);
                        
                        notepanel.setNote(invoicepurchasedraft.getNote());
                        datepanel.setDate(invoicepurchasedraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(invoicepurchasedraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(invoicepurchasedraft.getDraftid());
                        partnerpanel.setComboBoxValue(invoicepurchasedraft.getPartner());
                        duedatepanel.setDate(invoicepurchasedraft.getDuedate());
                        numberingpanel.setComboBoxValue(invoicepurchasedraft.getNumbering());
                        currencypanel.setComboBoxValue(invoicepurchasedraft.getCurrency());
                        ratepanel.setTextFieldValue(invoicepurchasedraft.getRate());
                        
                        expensespanel.setRounding(invoicepurchasedraft.getRounding());
                        expensespanel.setFreight(invoicepurchasedraft.getFreight());
                        expensespanel.setTax(invoicepurchasedraft.getTax());
                        
                        discountsubtotalpanel.setSubtotal(invoicepurchasedraft.getSubtotal());
                        discountsubtotalpanel.setDiscount(invoicepurchasedraft.getDiscount());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new InvoicePurchaseDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                invoicepurchasedraft.setCurrency(currencypanel.getComboBoxValue());
                invoicepurchasedraft.setInvoicedate(datepanel.getDate());
                invoicepurchasedraft.setDiscount(discountsubtotalpanel.getDiscount());
                invoicepurchasedraft.setDuedate(duedatepanel.getDate());
                invoicepurchasedraft.setFreight(expensespanel.getFreight());
                invoicepurchasedraft.setNote(notepanel.getNote());
                invoicepurchasedraft.setNumbering(numberingpanel.getComboBoxValue());
                invoicepurchasedraft.setPartner(partnerpanel.getComboBoxValue());
                invoicepurchasedraft.setRate(ratepanel.getTextFieldValue());
                invoicepurchasedraft.setRounding(expensespanel.getRounding());
                invoicepurchasedraft.setStatus(true);
                invoicepurchasedraft.setSubtotal(discountsubtotalpanel.getSubtotal());
                invoicepurchasedraft.setTax(expensespanel.getTax());

                response = RestClient.put("invoicepurchasedrafts", invoicepurchasedraft);

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

    @Override
    protected void delete() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                response = RestClient.delete("invoicepurchasedrafts/" + invoicepurchasedraft.getId(), "");

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
