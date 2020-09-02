/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyDecimalTextField;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import java.awt.FlowLayout;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class LabelDecimalTextFieldPanel extends MyPanel{
    
    private final MyDecimalTextField textfield;
    private final MyLabel label;
    
    public LabelDecimalTextFieldPanel(String labelvalue, double tfvalue){
        super(new FlowLayout(FlowLayout.LEFT));
        
        textfield = new MyDecimalTextField(tfvalue, 200);
        label = new MyLabel(labelvalue, 100);
        
        init();
    }
    
    private void init(){
        add(label);
        add(textfield);
    }
    
    public final void setDocumentListener(DocumentListener dl){
        textfield.getDocument().addDocumentListener(dl);
    }
    
    public final void setTextFieldEnabled(boolean value){
        textfield.setEditable(value);
    }
    
    public double getTextFieldValue(){
        try{
            return ((Number)textfield.getValue()).doubleValue();
        }
        catch(Exception e){
            return 0;
        }
    }
    
    public void setTextFieldValue(double value){
        textfield.setValue(value);
    }
}
