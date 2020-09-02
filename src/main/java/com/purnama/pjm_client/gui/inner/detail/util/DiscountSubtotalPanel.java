/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.FlowLayout;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class DiscountSubtotalPanel extends MyPanel{
    private double subtotalval;
    
    private final LabelDecimalTextFieldPanel discountpanel, subtotalpanel;
    
    public DiscountSubtotalPanel(){
        
        super(new FlowLayout(FlowLayout.RIGHT));
        
        discountpanel = new LabelDecimalTextFieldPanel(GlobalFields.
                    PROPERTIES.getProperty("LABEL_DISCOUNT"), 0);
        
        subtotalpanel = new LabelDecimalTextFieldPanel(GlobalFields.
                    PROPERTIES.getProperty("LABEL_SUBTOTAL"), 0);
        
        init();
    }
    
    private void init(){
        discountpanel.setTextFieldEnabled(false);
        subtotalpanel.setTextFieldEnabled(false);
        
        add(discountpanel);
        add(subtotalpanel);
    }
    
    public double getSubtotal(){
        return subtotalval;
    }
    
    public void setSubtotal(double value){
        subtotalval = value;
        setSubtotalminDiscount(getSubtotalminDiscount());
    }
    
    public double getDiscount(){
        return discountpanel.getTextFieldValue();
    }
    
    public void setDiscount(double value){
        discountpanel.setTextFieldValue(value);
        setSubtotalminDiscount(getSubtotalminDiscount());
    }
    
    public void setSubtotalminDiscount(double value){
        subtotalpanel.setTextFieldValue(value);
    }
    
    public double getSubtotalminDiscount(){
        return getSubtotal()-getDiscount();
    }
    
    public void setDocumentListener(DocumentListener dl){
        discountpanel.setDocumentListener(dl);
        subtotalpanel.setDocumentListener(dl);
    }
}
