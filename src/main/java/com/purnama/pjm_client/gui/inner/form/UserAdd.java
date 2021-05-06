/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.form.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelPasswordFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.RoleComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.WarehouseCheckBoxPanel;
import com.purnama.pjm_client.gui.inner.detail.UserDetail;
import com.purnama.pjm_client.gui.inner.home.UserHome;
import com.purnama.pjm_client.model.nontransactional.User;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class UserAdd extends FormPanel{
    
    protected final LabelTextFieldErrorPanel namepanel, usernamepanel, codepanel;
    
    protected final LabelPasswordFieldErrorPanel passwordpanel, confirmpanel;
    
    protected final LabelDecimalTextFieldPanel discountpanel;
    
    protected final RoleComboBoxPanel rolepanel;
    
    protected final WarehouseCheckBoxPanel warehousepanel;
    
    protected User user;
    
    public UserAdd() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_USER_ADD"));
        
        namepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1, GlobalFields.PROPERTIES.getProperty("LABEL_NAME")), "");
        
        codepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                        GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2,
                GlobalFields.PROPERTIES.getProperty("LABEL_CODE")), "");
        
        usernamepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                        GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2,
                GlobalFields.PROPERTIES.getProperty("LABEL_USERNAME")), "");
        
        passwordpanel = new LabelPasswordFieldErrorPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_1, 
                GlobalFields.PROPERTIES.getProperty("LABEL_PASSWORD")), "");
        
        confirmpanel = new LabelPasswordFieldErrorPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_1, 
                GlobalFields.PROPERTIES.getProperty("LABEL_CONFIRM")), "");
        
        discountpanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_3, 
                GlobalFields.PROPERTIES.getProperty("LABEL_MAXDISCOUNT")), 0);
        
        rolepanel = new RoleComboBoxPanel();
        
        warehousepanel = new WarehouseCheckBoxPanel();
        
        user = new User();
        
        init();
    }
    
    private void init(){
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        
        detailpanel.add(usernamepanel);
        
        detailpanel.add(passwordpanel);
        detailpanel.add(confirmpanel);
        
        detailpanel.add(discountpanel);
        
        detailpanel.add(rolepanel);
        detailpanel.add(warehousepanel);
        
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
        
        if(usernamepanel.isTextFieldContainSpecialCharacter() || !codepanel.isTextFieldLongBetween(3, 25)){
            status = GlobalFields.FAIL;
            usernamepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_CODE"));
        }
        else{
            usernamepanel.setErrorLabel("");
        }
        
        if(passwordpanel.isPasswordFieldEmpty()){
            status = GlobalFields.FAIL;
            passwordpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
        }
        else if(!confirmpanel.getPasswordFieldValue()
                .equals(passwordpanel.getPasswordFieldValue())){
            status = GlobalFields.FAIL;
            passwordpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_PASSWORDMISMATCH"));
        }
        else{
            passwordpanel.setErrorLabel("");
        }
        
        if(confirmpanel.isPasswordFieldEmpty()){
            status = GlobalFields.FAIL;
            confirmpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
        }
        else if(!confirmpanel.getPasswordFieldValue()
                .equals(passwordpanel.getPasswordFieldValue())){
            status = GlobalFields.FAIL;
            confirmpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_PASSWORDMISMATCH"));
        }
        else{
            confirmpanel.setErrorLabel("");
        }
        
        if(rolepanel.getComboBoxValue() == null){
            status = GlobalFields.FAIL;
            rolepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_COMBOBOXEMPTY"));
        }
        else{
            rolepanel.setErrorLabel("");
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
                    
                    user.setName(namepanel.getTextFieldValue());
                    user.setCode(codepanel.getTextFieldValue());
                    user.setUsername(usernamepanel.getTextFieldValue());
                    user.setPassword(passwordpanel.getPasswordFieldValue());
                    user.setRole(rolepanel.getComboBoxValue());
                    user.setMaximumdiscount(discountpanel.getTextFieldDecimalValue());
                    user.setStatus(statuspanel.getSelectedValue());
                    user.setNote(notepanel.getTextAreaValue());
                    user.setWarehouses(warehousepanel.getCheckBoxValues());
                    
                    clientresponse = RestClient.post("users", user);
                    
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
                            User user = mapper.readValue(output, User.class);
                            detail(user.getId());
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            
            worker.execute();
        
        }
    }
    
    @Override
    public void refresh(){
        rolepanel.refresh();
        warehousepanel.refresh();
    }

    @Override
    protected void home() {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new UserHome());
    }

    @Override
    protected void detail(int id) {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new UserDetail(id));
    }
}
