/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.table.ItemReturnSalesDraftTablePanel;
import com.purnama.pjm_client.gui.inner.detail.util.CurrencyComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NumberingComboBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.util.PartnerComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.ReturnSalesDraftHome;
import com.purnama.pjm_client.model.nontransactional.Menu;
import com.purnama.pjm_client.model.transactional.draft.ReturnSalesDraft;
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
public class ReturnSalesDraftDetail extends InvoiceDraftDetailPanel{

    private final PartnerComboBoxPanel partnerpanel;
    
    private final CurrencyComboBoxPanel currencypanel;
    
    private final NumberingComboBoxPanel numberingpanel;
    
    private final DatePanel duedatepanel;
    
    private final LabelDecimalTextFieldPanel ratepanel;
    
    private final ItemReturnSalesDraftTablePanel itemreturnsalesdrafttablepanel;
    
    private ReturnSalesDraft returnsalesdraft;
    
    private final int invoiceid;
    
    public ReturnSalesDraftDetail(int invoiceid){
        super(GlobalFields.PROPERTIES.getProperty("PANEL_RETURNSALESDRAFT_DETAIL"));
        
        this.invoiceid = invoiceid;
        
        partnerpanel = new PartnerComboBoxPanel(PartnerComboBoxPanel.CUSTOMER);
        numberingpanel = new NumberingComboBoxPanel(Menu.MENU_RETURNSALES);
        currencypanel = new CurrencyComboBoxPanel();
        ratepanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_RATE"), 1);
        duedatepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DUEDATE"));
        
        itemreturnsalesdrafttablepanel = new ItemReturnSalesDraftTablePanel(invoiceid, discountsubtotalpanel);
        
        init();
    }
    
    private void init(){
        iteminvoicedialog.setTablemodel(itemreturnsalesdrafttablepanel.getItemreturnsalesdrafttablemodel());
        
        leftdetailpanel.add(numberingpanel);
        
        middledetailpanel.add(duedatepanel);
        middledetailpanel.add(partnerpanel);
        
        rightdetailpanel.add(currencypanel);
        rightdetailpanel.add(ratepanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("LABEL_CONTENT"), 
                itemreturnsalesdrafttablepanel);
        
        savebutton.addActionListener((ActionEvent e) -> {
            save();
            itemreturnsalesdrafttablepanel.submitItemReturnSalesDraftList();
            itemreturnsalesdrafttablepanel.submitDeletedItemReturnSalesDraftList();
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
            itemreturnsalesdrafttablepanel.submitItemReturnSalesDraftList();
            itemreturnsalesdrafttablepanel.submitDeletedItemReturnSalesDraftList();
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
                
                response = RestClient.get("returnsalesdrafts/" + invoiceid);
                
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
                        returnsalesdraft = mapper.readValue(output, ReturnSalesDraft.class);
                        
                        notepanel.setNote(returnsalesdraft.getNote());
                        datepanel.setDate(returnsalesdraft.getInvoicedate());
                        warehousepanel.setTextFieldValue(returnsalesdraft.getWarehouse().getCode());
                        idpanel.setTextFieldValue(returnsalesdraft.getDraftid());
                        partnerpanel.setComboBoxValue(returnsalesdraft.getPartner());
                        duedatepanel.setDate(returnsalesdraft.getDuedate());
                        numberingpanel.setComboBoxValue(returnsalesdraft.getNumbering());
                        currencypanel.setComboBoxValue(returnsalesdraft.getCurrency());
                        ratepanel.setTextFieldValue(returnsalesdraft.getRate());
                        
                        expensespanel.setRounding(returnsalesdraft.getRounding());
                        expensespanel.setFreight(returnsalesdraft.getFreight());
                        expensespanel.setTax(returnsalesdraft.getTax());
                        
                        discountsubtotalpanel.setSubtotal(returnsalesdraft.getSubtotal());
                        discountsubtotalpanel.setDiscount(returnsalesdraft.getDiscount());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnSalesDraftHome());
    }

    @Override
    protected void save() {
        SwingWorker<Boolean, String> saveworker = new SwingWorker<Boolean, String>(){
            ClientResponse response;

            @Override
            protected Boolean doInBackground(){

                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));

                returnsalesdraft.setCurrency(currencypanel.getComboBoxValue());
                returnsalesdraft.setInvoicedate(datepanel.getDate());
                returnsalesdraft.setDiscount(discountsubtotalpanel.getDiscount());
                returnsalesdraft.setDuedate(duedatepanel.getDate());
                returnsalesdraft.setFreight(expensespanel.getFreight());
                returnsalesdraft.setNote(notepanel.getNote());
                returnsalesdraft.setNumbering(numberingpanel.getComboBoxValue());
                returnsalesdraft.setPartner(partnerpanel.getComboBoxValue());
                returnsalesdraft.setRate(ratepanel.getTextFieldValue());
                returnsalesdraft.setRounding(expensespanel.getRounding());
                returnsalesdraft.setStatus(true);
                returnsalesdraft.setSubtotal(discountsubtotalpanel.getSubtotal());
                returnsalesdraft.setTax(expensespanel.getTax());

                response = RestClient.put("returnsalesdrafts", returnsalesdraft);

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

                response = RestClient.post("returnsales/" + returnsalesdraft.getId(), "");

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
                    
                    GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ReturnSalesDetail(Integer.parseInt(output)));
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
        itemreturnsalesdrafttablepanel.refresh();
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

                response = RestClient.delete("returnsalesdrafts/" + returnsalesdraft.getId(), "");

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
