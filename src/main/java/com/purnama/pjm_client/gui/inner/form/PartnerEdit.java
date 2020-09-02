/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class PartnerEdit extends PartnerAdd{
    
    private final int id;
    
    public PartnerEdit(int id){
        this.id = id;
        
        init();
    }
    
    private void init(){
        setName(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNER_EDIT"));
        
        load();
    }
    
    
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("partners/" + id);
                
                return true;
                
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String number : chunks) {
                upperpanel.setNotifLabel(number);
                }
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
                        partner = mapper.readValue(output, Partner.class);
                        
                        codepanel.setTextFieldEditable(false);
                        
                        codepanel.setTextFieldValue(partner.getCode());
                        namepanel.setTextFieldValue(partner.getName());
                        contactnamepanel.setTextFieldValue(partner.getContactname());
                        addresspanel.setTextAreaValue(partner.getAddress());
                        phonenumberpanel.setTextFieldValue(partner.getPhonenumber());
                        phonenumber2panel.setTextFieldValue(partner.getPhonenumber2());
                        mobilenumberpanel.setTextFieldValue(partner.getMobilenumber());
                        mobilenumber2panel.setTextFieldValue(partner.getMobilenumber2());
                        faxnumberpanel.setTextFieldValue(partner.getFaxnumber());
                        faxnumber2panel.setTextFieldValue(partner.getFaxnumber2());
                        emailpanel.setTextFieldValue(partner.getEmail());
                        email2panel.setTextFieldValue(partner.getEmail2());
                        maximumdiscountpanel.setTextFieldValue(partner.getMaximumdiscount());
                        maximumbalancepanel.setTextFieldValue(partner.getMaximumbalance());
                        paymentduepanel.setTextFieldValue(partner.getPaymentdue());
                        customerpanel.setSelectedValue(partner.isCustomer());
                        supplierpanel.setSelectedValue(partner.isSupplier());
                        nontradepanel.setSelectedValue(partner.isNontrade());
                        statuspanel.setSelectedValue(partner.isStatus());
                        notepanel.setTextAreaValue(partner.getNote());
                        
//                        setState(MyPanel.SAVED);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    @Override
    protected void submit() {
        if(validateinput()){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse clientresponse;
                
                @Override
                protected Boolean doInBackground(){
                
                    submitpanel.loading();
                    
                    partner.setName(namepanel.getTextFieldValue());
                    partner.setContactname(contactnamepanel.getTextFieldValue());
                    partner.setAddress(addresspanel.getTextAreaValue());
                    partner.setPhonenumber(phonenumberpanel.getTextFieldValue());
                    partner.setPhonenumber2(phonenumber2panel.getTextFieldValue());
                    partner.setMobilenumber(mobilenumberpanel.getTextFieldValue());
                    partner.setMobilenumber2(mobilenumber2panel.getTextFieldValue());
                    partner.setFaxnumber(faxnumberpanel.getTextFieldValue());
                    partner.setFaxnumber2(faxnumber2panel.getTextFieldValue());
                    partner.setEmail(emailpanel.getTextFieldValue());
                    partner.setEmail2(email2panel.getTextFieldValue());
                    partner.setMaximumdiscount(maximumdiscountpanel.getTextFieldDecimalValue());
                    partner.setMaximumbalance(maximumbalancepanel.getTextFieldDecimalValue());
                    partner.setPaymentdue((int)paymentduepanel.getTextFieldDecimalValue());
                    partner.setCustomer(customerpanel.getSelectedValue());
                    partner.setSupplier(supplierpanel.getSelectedValue());
                    partner.setNontrade(nontradepanel.getSelectedValue());
                    partner.setNote(notepanel.getTextAreaValue());
                    partner.setStatus(statuspanel.getSelectedValue());
                    
                    clientresponse = RestClient.put("partners", partner);
                    
                    return true;
                }
                
                @Override
                protected void done() {
                    submitpanel.finish();
                    
                    if(clientresponse == null){
                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                    }
                    else if(clientresponse.getStatus() != 200) {
                        upperpanel.setNotifLabel("");
                        
                        String output = clientresponse.getEntity(String.class);
                        
                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                        + clientresponse.getStatus(), 
                        JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        String output = clientresponse.getEntity(String.class);
                        
                        ObjectMapper mapper = new ObjectMapper();
                        
                        try{
                            Partner partner = mapper.readValue(output, Partner.class);
                            detail(partner.getId());
                        }
                        catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
            };
            
            worker.execute();
        
        }
    }
    
    @Override
    public void refresh() {
        load();
    }
}
