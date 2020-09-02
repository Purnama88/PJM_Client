/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTextArea;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class NotePanel extends MyScrollPane{
    
    private final MyTextArea textarea;
    
    public NotePanel(){
        super();
        
        textarea = new MyTextArea("");
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textarea.setOpaque(false);
        
        init();
    }
    
    private void init(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        getViewport().add(textarea, null);
        getViewport().setOpaque(false);
        setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    
    public String getNote(){
        return textarea.getText();
    }
    
    public void setNote(String note){
        textarea.setText(note);
    }
    
    public final void setDocumentListener(DocumentListener dl){
        textarea.getDocument().addDocumentListener(dl);
    }
    
    public final void setTextAreaEnabled(boolean value){
        textarea.setEditable(value);
    }
}
