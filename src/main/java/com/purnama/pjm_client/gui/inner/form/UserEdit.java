/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.model.nontransactional.User;
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
public class UserEdit extends UserAdd{
    
    private final int id;
    
    public UserEdit(int id){
        this.id = id;
        
        init();
    }
    
    private void init(){
        setName(GlobalFields.PROPERTIES.getProperty("PANEL_USER_EDIT"));
        
        detailpanel.remove(passwordpanel);
        detailpanel.remove(confirmpanel);
        
        load();
    }
    
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("users/" + id);
                
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
                        user = mapper.readValue(output, User.class);
                        
                        codepanel.setTextFieldEditable(false);
                        usernamepanel.setTextFieldEditable(false);
                        
                        codepanel.setTextFieldValue(user.getCode());
                        namepanel.setTextFieldValue(user.getName());
                        usernamepanel.setTextFieldValue(user.getUsername());
                        discountpanel.setTextFieldValue(user.getMaximumdiscount());
                        rolepanel.setComboBoxValue(user.getRole());
                        warehousepanel.setCheckBoxValues(user.getWarehouses());
                        statuspanel.setSelectedValue(user.isStatus());
                        notepanel.setTextAreaValue(user.getNote());
                        
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
    protected boolean validateinput() {
        boolean status = GlobalFields.SUCCESS;
        
        if(namepanel.isTextFieldEmpty()){
            status = GlobalFields.FAIL;
            namepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
        }
        else{
            namepanel.setErrorLabel("");
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
                    user.setRole(rolepanel.getComboBoxValue());
                    user.setMaximumdiscount(discountpanel.getTextFieldDecimalValue());
                    user.setStatus(statuspanel.getSelectedValue());
                    user.setNote(notepanel.getTextAreaValue());
                    user.setStatus(statuspanel.getSelectedValue());
                    user.setWarehouses(warehousepanel.getCheckBoxValues());
                    
                    clientresponse = RestClient.put("users", user);
                    
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
        load();
    }
}
