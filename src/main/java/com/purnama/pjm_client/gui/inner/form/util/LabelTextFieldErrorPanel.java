/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class LabelTextFieldErrorPanel extends MyPanel{
    
    private final MyTextField textfield;
    
    private final MyLabel label, errorlabel;
    
    private Object actioncommand;
    
    public LabelTextFieldErrorPanel(String title, String value){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel(title, 150);
        errorlabel = new MyLabel("", 250);
        textfield = new MyTextField(value, 250);
        
        init();
    }
    
    private void init(){
        setMaximumSize(new Dimension(700, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(textfield);
        add(errorlabel);
    }
    
    public void setTextFieldEditable(boolean status){
        textfield.setEditable(status);
    }
    
    public void setDocumentListener(DocumentListener dl){
        textfield.getDocument().addDocumentListener(dl);
    }
    
    public void setTextFieldActionListener(ActionListener al){
        textfield.addActionListener(al);
    }
    
    public String getTextFieldValue(){
        return textfield.getText();
    }
    
    public void setTextFieldValue(String value){
        textfield.setText(value);
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
        textfield.setText("");
    }
    
    @Override
    public void requestFocus(){
        textfield.requestFocusInWindow();
    }
    
    public boolean isTextFieldEmpty(){
        return textfield.isEmpty();
    }
    
    public boolean isTextFieldLongBetween(int min, int max){
        return textfield.isLongBetween(min, max);
    }
    
    public boolean isTextFieldContainSpecialCharacter(){
        Pattern pattern = Pattern.compile("[-a-zA-Z0-9]*");
        
        Matcher matcher = pattern.matcher(textfield.getText());
        
        if(!matcher.matches()){
           return true;
        }
        else{
           return false;
        }
    }
}
