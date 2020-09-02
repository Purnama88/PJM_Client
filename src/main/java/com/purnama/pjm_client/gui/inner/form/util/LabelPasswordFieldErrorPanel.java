/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyPasswordField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class LabelPasswordFieldErrorPanel extends MyPanel{
    
    private final MyPasswordField passwordfield;
    
    private final MyLabel label, errorlabel;
    
    private Object actioncommand;
    
    public LabelPasswordFieldErrorPanel(String title, String value){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel(title, 150);
        errorlabel = new MyLabel("", 200);
        passwordfield = new MyPasswordField(value, 250);
        
        init();
    }
    
    private void init(){
        setMaximumSize(new Dimension(700, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(passwordfield);
        add(errorlabel);
    }
    
    public void setPasswordFieldEditable(boolean status){
        passwordfield.setEditable(status);
    }
    
    public void setDocumentListener(DocumentListener dl){
        passwordfield.getDocument().addDocumentListener(dl);
    }
    
    public void setPasswordFieldActionListener(ActionListener al){
        passwordfield.addActionListener(al);
    }
    
    public String getPasswordFieldValue(){
        return passwordfield.getText();
    }
    
    public void setPasswordFieldValue(String value){
        passwordfield.setText(value);
    }
    
    public String getLabelValue(){
        return label.getText();
    }
    
    public void setLabelValue(String value){
        label.setText(value);
    }
    
    public String getErrorLabel(){
        return errorlabel.getText();
    }
    
    public void setErrorLabel(String value){
        errorlabel.setText("<HTML><FONT COLOR=RED>" + value + "</FONT></HTML>");
    }
    
    public Object getActioncommand() {
        return actioncommand;
    }

    public void setActioncommand(Object actioncommand) {
        this.actioncommand = actioncommand;
    }
    
    public void reset(){
        passwordfield.setText("");
    }
    
    @Override
    public void requestFocus(){
        passwordfield.requestFocusInWindow();
    }
    
    public boolean isPasswordFieldEmpty(){
        return passwordfield.isEmpty();
    }
    
    public boolean isPasswordFieldLong(int stringlong){
        return passwordfield.isLongLessThan(stringlong);
    }
    
    public boolean isPasswordFieldLongBetween(int min, int max){
        return passwordfield.isLongBetween(min, max);
    }
}
