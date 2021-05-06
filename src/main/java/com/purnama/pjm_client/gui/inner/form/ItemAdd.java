/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.inner.form.util.LabelComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelDecimalTextFieldPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextAreaPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.detail.ItemDetail;
import com.purnama.pjm_client.gui.inner.home.ItemHome;
import com.purnama.pjm_client.model.nontransactional.Item;
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
public class ItemAdd extends FormPanel{

    protected final LabelTextFieldErrorPanel namepanel, codepanel;
    
    protected final LabelDecimalTextFieldPanel costpanel;
    
    protected final LabelComboBoxPanel labelpanel;
    
    protected final LabelTextAreaPanel descriptionpanel;
    
    protected Item item;
    
    public ItemAdd() {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_ITEM_ADD"));
        
        namepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                GlobalFields.LABEL_REMARK_1, GlobalFields.PROPERTIES.getProperty("LABEL_NAME")), "");
        
        codepanel = new LabelTextFieldErrorPanel(
                GlobalFunctions.toSuperscript(
                        GlobalFields.LABEL_REMARK_1 + GlobalFields.LABEL_REMARK_2,
                GlobalFields.PROPERTIES.getProperty("LABEL_CODE")), "");
        
        costpanel = new LabelDecimalTextFieldPanel(
                GlobalFields.PROPERTIES.getProperty("LABEL_COST"), 0);
        
        descriptionpanel = new LabelTextAreaPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"), "");
    
        labelpanel = new LabelComboBoxPanel();
        
        item = new Item();
        
        init();
    }
    
    private void init(){
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        
        detailpanel.add(descriptionpanel);
        
        detailpanel.add(costpanel);
        detailpanel.add(labelpanel);
        
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
        
        if(labelpanel.getComboBoxSize() == 0){
            labelpanel.setErrorLabel(GlobalFields.PROPERTIES.getProperty("ERROR_EMPTY"));
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
                    
                    item.setName(namepanel.getTextFieldValue());
                    item.setCode(codepanel.getTextFieldValue());
                    item.setDescription(descriptionpanel.getTextAreaValue());
                    item.setCost(costpanel.getTextFieldDecimalValue());
                    item.setLabel(labelpanel.getComboBoxValue());
                    item.setNote(notepanel.getTextAreaValue());
                    item.setStatus(statuspanel.getSelectedValue());
                    
                    clientresponse = RestClient.post("items", item);
                    
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
                            Item item = mapper.readValue(output, Item.class);
                            detail(item.getId());
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
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ItemHome());
    }

    @Override
    protected void detail(int id) {
        GlobalFields.MAINTABBEDPANE.changeTabPanel(getIndex(), new ItemDetail(id));
    }

    @Override
    public void refresh() {
        labelpanel.refresh();
    }
    
}
