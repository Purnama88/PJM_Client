/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.detail.NumberingDetail;
import com.purnama.pjm_client.gui.inner.form.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.MenuComboBoxPanel;
import com.purnama.pjm_client.gui.inner.home.NumberingHome;
import com.purnama.pjm_client.model.nontransactional.Numbering;
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
public class NumberingAdd extends FormPanel{

    protected final LabelTextFieldErrorPanel namepanel, prefixpanel;
    
    protected final LabelDecimalTextFieldPanel startpanel, endpanel;
    
    protected final MenuComboBoxPanel menupanel;
    
    public NumberingAdd() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_NUMBERING_ADD"));
        
        namepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1, GlobalFields.PROPERTIES.getProperty("LABEL_NAME")), "");
        
        prefixpanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2, GlobalFields.PROPERTIES.getProperty("LABEL_PREFIX")), "");
    
        startpanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_2, 
                GlobalFields.PROPERTIES.getProperty("LABEL_START")), 1);
        
        endpanel = new LabelDecimalTextFieldPanel(
                GlobalFunctions.toSuperscript(GlobalFields.LABEL_REMARK_2, 
                GlobalFields.PROPERTIES.getProperty("LABEL_END")), 2);
        
        menupanel = new MenuComboBoxPanel();
        
        init();
    }
    
    private void init(){
        
        namepanel.setDocumentListener(this);
        prefixpanel.setDocumentListener(this);
        endpanel.setDocumentListener(this);
        notepanel.setDocumentListener(this);
        
        endpanel.setTextFieldActionListener(this);
        namepanel.setTextFieldActionListener(this);
        
        startpanel.setTextFieldEditable(false);
        
        detailpanel.add(menupanel);
        detailpanel.add(namepanel);
        
        detailpanel.add(prefixpanel);
        detailpanel.add(startpanel);
        
        detailpanel.add(endpanel);
        
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        
        detailpanel.add(remarkemptylabel);
        detailpanel.add(remarknoneditablelabel);
    }   

    @Override
    protected boolean validateinput() {
        boolean status = GlobalFields.SUCCESS;
        
        if(namepanel.isTextFieldContainSpecialCharacter() || !namepanel.isTextFieldLongBetween(3, 25)){
            status = GlobalFields.FAIL;
            namepanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_CODE"));
        }
        else{
            namepanel.setErrorLabel("");
        }
        
        if(prefixpanel.isTextFieldContainSpecialCharacter() || !prefixpanel.isTextFieldLongBetween(1, 10)){
            status = GlobalFields.FAIL;
            prefixpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_PREFIX"));
        }
        else{
            prefixpanel.setErrorLabel("");
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
                    
                    Numbering numbering = new Numbering();

                    numbering.setMenu(menupanel.getComboBoxValue());
                    numbering.setName(namepanel.getTextFieldValue());
                    numbering.setPrefix(prefixpanel.getTextFieldValue());
                    numbering.setEnd(endpanel.getTextFieldIntegerValue());
                    numbering.setNote(notepanel.getTextAreaValue());
                    numbering.setStatus(statuspanel.getSelectedValue());
                    
                    clientresponse = RestClient.post("numberings", numbering);
                    
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
                            Numbering numbering = mapper.readValue(output, Numbering.class);
                            detail(numbering.getId());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new NumberingHome());
    }

    @Override
    protected void detail(int id) {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new NumberingDetail(id));
    }

    @Override
    public void refresh() {
        menupanel.refresh();
    }
    
}
