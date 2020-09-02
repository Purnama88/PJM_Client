/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemInvoiceSalesDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.CurrencyComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.InvoiceSalesDraftHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.transactional.draft.InvoiceSalesDraft;
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
public class InvoiceSalesDraftDetail extends InvoiceDraftDetailPanel{

    private final PartnerComboBoxPanel partnerpanel;
    
    private final CurrencyComboBoxPanel currencypanel;
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final DatePanel duedatepanel;
    
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final ItemInvoiceSalesDraftTablePanel iteminvoicesalesdrafttablepanel;
    
    private InvoiceSalesDraft invoicesalesdraft;
    
    private final int invoiceid;
    
    public InvoiceSalesDraftDetail(int invoiceid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_INVOICESALESDRAFT_DETAIL"));
        
        this.invoiceid = invoiceid;
        
        partnerpanel = new PartnerComboBoxPanel(PartnerComboBoxPanel.CUSTOMER);
        numberingpanel = new NumberingComboBoxPanel(2);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        iteminvoicesalesdrafttablepanel = new ItemInvoiceSalesDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                iteminvoicesalesdrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            iteminvoicesalesdrafttablepanel.submitItemInvoiceSalesDraftList();
            iteminvoicesalesdrafttablepanel.submitDeletedItemInvoiceSalesDraftList();
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
                
                response = RestClient.get("invoicesalesdrafts/" + invoiceid);
                
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
                        invoicesalesdraft = mapper.readValue(output, InvoiceSalesDraft.class);
                        
                        notepanel.setNote(invoicesalesdraft.getNote());
                        datepanel.setDate(invoicesalesdraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(invoicesalesdraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(invoicesalesdraft.getDraftid());
                        partnerpanel.setComboBoxValue(invoicesalesdraft.getPartner());
                        duedatepanel.setDate(invoicesalesdraft.getDuedate());
                        numberingpanel.setComboBoxValue(invoicesalesdraft.getNumbering());
                        currencypanel.setComboBoxValue(invoicesalesdraft.getCurrency());
                        ratepanel.setTextFieldValue(invoicesalesdraft.getRate());
                        
                        expensespanel.setRounding(invoicesalesdraft.getRounding());
                        expensespanel.setFreight(invoicesalesdraft.getFreight());
                        expensespanel.setTax(invoicesalesdraft.getTax());
                        
                        discountsubtotalpanel.setSubtotal(invoicesalesdraft.getSubtotal());
                        discountsubtotalpanel.setDiscount(invoicesalesdraft.getDiscount());
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
        
        tabbedPane.changeTabPanel(getIndex(), new InvoiceSalesDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                invoicesalesdraft.setCurrency(currencypanel.getComboBoxValue());
                invoicesalesdraft.setInvoicedate(datepanel.getDate());
                invoicesalesdraft.setDiscount(discountsubtotalpanel.getDiscount());
                invoicesalesdraft.setDuedate(duedatepanel.getDate());
                invoicesalesdraft.setFreight(expensespanel.getFreight());
                invoicesalesdraft.setNote(notepanel.getNote());
                invoicesalesdraft.setNumbering(numberingpanel.getComboBoxValue());
                invoicesalesdraft.setPartner(partnerpanel.getComboBoxValue());
                invoicesalesdraft.setRate(ratepanel.getTextFieldValue());
                invoicesalesdraft.setRounding(expensespanel.getRounding());
                invoicesalesdraft.setStatus(true);
                invoicesalesdraft.setSubtotal(discountsubtotalpanel.getSubtotal());
                invoicesalesdraft.setTax(expensespanel.getTax());

                response = RestClient.put("invoicesalesdrafts", invoicesalesdraft);

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
