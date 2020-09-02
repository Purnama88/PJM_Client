/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.form.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextAreaPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.detail.PartnerDetail;
import com.purnama.pjm_client.gui.inner.form.util.StatusPanel;
import com.purnama.pjm_client.gui.inner.home.PartnerHome;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class PartnerAdd extends FormPanel{

    protected final LabelTextFieldErrorPanel namepanel, contactnamepanel, codepanel,
            phonenumberpanel, phonenumber2panel, faxnumberpanel, faxnumber2panel, mobilenumberpanel, mobilenumber2panel,
            emailpanel, email2panel;
    
    protected final LabelDecimalTextFieldPanel maximumdiscountpanel, maximumbalancepanel,
            paymentduepanel;
    
    protected final LabelTextAreaPanel addresspanel;
    
    protected final StatusPanel customerpanel, supplierpanel, nontradepanel;
    
    protected Partner partner;
    
    public PartnerAdd() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_PARTNER_ADD"));
        
        contactnamepanel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_CONTACTNAME"), "");
        
        namepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1, GlobalFields.PROPERTIES.getProperty("LABEL_NAME")), "");
        
        phonenumberpanel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_PHONENUMBER"), "");
        
        phonenumber2panel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_PHONENUMBER"), "");
        
        mobilenumberpanel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_MOBILENUMBER"), "");
        
        mobilenumber2panel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_MOBILENUMBER"), "");
        
        faxnumberpanel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_FAXNUMBER"), "");
        
        faxnumber2panel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_FAXNUMBER"), "");
        
        emailpanel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_EMAIL"), "");
        
        email2panel = new LabelTextFieldErrorPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_EMAIL"), "");
        
        codepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                        GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2,
                GlobalFields.PROPERTIES.getProperty("LABEL_CODE")), "");
        
        addresspanel = new LabelTextAreaPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ADDRESS"), "");
    
        maximumdiscountpanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_3, 
                GlobalFields.PROPERTIES.getProperty("LABEL_MAXDISCOUNT")), 0);
        
        maximumbalancepanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_3, 
                GlobalFields.PROPERTIES.getProperty("LABEL_MAXBALANCE")), 0);
        
        paymentduepanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_3, 
                GlobalFields.PROPERTIES.getProperty("LABEL_DUE")), 0);
        
        customerpanel = new StatusPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CUSTOMER"));
        
        supplierpanel = new StatusPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SUPPLIER"));
        
        nontradepanel = new StatusPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NONTRADE"));
        
        partner = new Partner();
        
        init();
    }
    
    private void init(){
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        
        detailpanel.add(contactnamepanel);
        detailpanel.add(addresspanel);
        
        detailpanel.add(phonenumberpanel);
        detailpanel.add(phonenumber2panel);
        detailpanel.add(mobilenumberpanel);
        detailpanel.add(mobilenumber2panel);
        detailpanel.add(faxnumberpanel);
        detailpanel.add(faxnumber2panel);
        
        detailpanel.add(emailpanel);
        detailpanel.add(email2panel);
        
        detailpanel.add(maximumdiscountpanel);
        detailpanel.add(maximumbalancepanel);
        detailpanel.add(paymentduepanel);
        
        detailpanel.add(customerpanel);
        detailpanel.add(supplierpanel);
        detailpanel.add(nontradepanel);
        
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        
        detailpanel.add(remarkemptylabel);
        detailpanel.add(remarknoneditablelabel);
        detailpanel.add(remarknolimitlabel);
    }

    @Override
    protected boolean validateinput() {
        boolean status = GlobalFields.SUCCESS;
        
        if(namepanel.isTextFieldEmpty()){
            status = GlobalFields.FAIL;
            namepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
        }
        else{
            namepanel.setErrorLabel("");
        }
        
        if(codepanel.isTextFieldContainSpecialCharacter() || !codepanel.isTextFieldLongBetween(3, 25)){
            status = GlobalFields.FAIL;
            codepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_CODE"));
        }
        else{
            codepanel.setErrorLabel("");
        }
        
        return status;
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
                    partner.setCode(codepanel.getTextFieldValue());
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
                    
                    clientresponse = RestClient.post("partners", partner);
                    
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
    protected void home() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new PartnerHome());
    }

    @Override
    protected void detail(int id) {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new PartnerDetail(id));
    }

    @Override
    public void refresh() {
    }
    
}
