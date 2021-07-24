/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.form.util.BrandComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextAreaPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.detail.ModelDetail;
import com.purnama.pjm_client.gui.inner.home.ModelHome;
import com.purnama.pjm_client.model.nontransactional.Model;
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
public class ModelAdd extends FormPanel{

    protected final LabelTextFieldErrorPanel codepanel, namepanel;
    
    protected final LabelTextAreaPanel descriptionpanel;
    
    protected final BrandComboBoxPanel brandpanel;
    
    protected Model model;
    
    public ModelAdd() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_MODEL_ADD"));
        
        codepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                        GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2,
                GlobalFields.PROPERTIES.getProperty("LABEL_CODE")), "");
        
        namepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1, GlobalFields.PROPERTIES.getProperty("LABEL_NAME")), "");
        
        descriptionpanel = new LabelTextAreaPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"), "");
    
        brandpanel = new BrandComboBoxPanel();
        
        model = new Model();
        
        init();
    }
    
    private void init(){
        
        codepanel.setDocumentListener(this);
        namepanel.setDocumentListener(this);
        descriptionpanel.setDocumentListener(this);
        notepanel.setDocumentListener(this);
        
        codepanel.setTextFieldActionListener(this);
        namepanel.setTextFieldActionListener(this);
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(brandpanel);
        detailpanel.add(descriptionpanel);
        
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        
        detailpanel.add(remarkemptylabel);
        detailpanel.add(remarknoneditablelabel);
        
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
        
        if(brandpanel.getComboBoxSize() == 0){
            brandpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
            status = GlobalFields.FAIL;
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
                    
                    model.setCode(codepanel.getTextFieldValue());
                    model.setName(namepanel.getTextFieldValue());
                    model.setDescription(descriptionpanel.getTextAreaValue());
                    model.setNote(notepanel.getTextAreaValue());
                    model.setBrand(brandpanel.getComboBoxValue());
                    model.setStatus(statuspanel.getSelectedValue());
                    
                    clientresponse = RestClient.post("models", model);
                    
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
                            Model model = mapper.readValue(output, Model.class);
                            detail(model.getId());
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
    protected void home() {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ModelHome());
    }

    @Override
    protected void detail(int id) {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ModelDetail(id));
    }

    @Override
    public void refresh() {
        brandpanel.refresh();
    }
    
}
