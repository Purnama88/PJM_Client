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
import com.purnama.pjm_client.model.nontransactional.Menu;
import com.purnama.pjm_client.model.transactional.draft.ReturnPurchaseDraft;
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
        numberingpanel = new NumberingComboBoxPanel(Menu.MENU_RETURNPURCHASE);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        itemreturnpurchasedrafttablepanel = new ItemReturnPurchaseDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        iteminvoicedialog.setTablemodel(itemreturnpurchasedrafttablepanel.getItemreturnpurchasedrafttablemodel());
        
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
            itemreturnpurchasedrafttablepanel.submitItemReturnPurchaseDraftList();
            itemreturnpurchasedrafttablepanel.submitDeletedItemReturnPurchaseDraftList();
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnPurchaseDraftHome());
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
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                response = RestClient.post("returnpurchases/" + returnpurchasedraft.getId(), "");

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
                    
                    GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnPurchaseDetail(Integer.parseInt(output)));
                }
            }
        };
        saveworker.execute();
    }
    
    @Override
    public void refresh(){
        
        partnerpanel.refresh();
        currencypanel.refresh();
        numberingpanel.refresh();
        itemreturnpurchasedrafttablepanel.refresh();
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

                response = RestClient.delete("returnpurchasedrafts/" + returnpurchasedraft.getId(), "");

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
