/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTextArea;
import com.purnama.pjm_client.util.GlobalFields;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class DestinationPanel extends MyScrollPane{
    
    private final MyTextArea textarea;
    
    public DestinationPanel(String destination){
        
        textarea = new MyTextArea(destination);
        
        init();
    }
    
    private void init(){
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
            GlobalFields.PROPERTIES.getProperty("LABEL_DESTINATION")));
        
        textarea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        getViewport().add(textarea, null);
        getViewport().setOpaque(false);
        
    }
    
    public String getDestination(){
        return textarea.getText();
    }
    
    public void setDestination(String destination){
        textarea.setText(destination);
    }
    
    public final void setDocumentListener(DocumentListener dl){
        textarea.getDocument().addDocumentListener(dl);
    }
    
    public final void setTextAreaEnabled(boolean value){
        textarea.setEditable(value);
    }
}
