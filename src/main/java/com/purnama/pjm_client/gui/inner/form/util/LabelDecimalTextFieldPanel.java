/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.purnama.pjm_client.gui.library.MyDecimalTextField;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class LabelDecimalTextFieldPanel extends MyPanel{
    
    private final MyLabel label;
    private final MyDecimalTextField textfield;
    
    private Object actioncommand;
    
    public LabelDecimalTextFieldPanel(String title, Object value){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel(title, 150);
        
        textfield = new MyDecimalTextField(value, 250);
        
        init();
    }
    
    private void init(){
        setMaximumSize(new Dimension(700, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(textfield);
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
    
    public double getTextFieldDecimalValue(){
        return GlobalFunctions.convertToDouble(textfield.getText());
    }
    
    public int getTextFieldIntegerValue(){
        return GlobalFunctions.convertToInteger(textfield.getText());
    }
    
    public void setTextFieldValue(Object value){
        textfield.setValue(value);
    }
    
    public String getLabelValue(){
        return label.getText();
    }
    
    public void setLabelValue(String value){
        label.setText(value);
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
}
