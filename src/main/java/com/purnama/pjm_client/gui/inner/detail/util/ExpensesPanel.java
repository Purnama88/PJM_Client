/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyDialog;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class ExpensesPanel extends MyPanel{
    
    private final LabelDecimalTextFieldPanel roundingpanel, freightpanel, taxpanel, totalexpenses, ttotalexpenses;
    
    private final MyButton menubutton;
    
    public ExpensesPanel(){
        
        super(new FlowLayout(FlowLayout.RIGHT));
        
        totalexpenses = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_OTHERS"), 0);
        
        ttotalexpenses = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_TOTAL"), 0);
        
        roundingpanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ROUNDING"), 0);
        
        freightpanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_FREIGHT"), 0);
        
        taxpanel = new LabelDecimalTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_TAX"),0);
        
        menubutton = new MyButton(new MyImageIcon().getImage("image/Menu_16.png"), 
                20, 20);
        
        init();
    }
    
    private void init(){
        totalexpenses.setTextFieldEnabled(false);
        ttotalexpenses.setTextFieldEnabled(false);
        
        add(menubutton);
        add(totalexpenses);
        
        menubutton.addActionListener((ActionEvent e) -> {
            new ExpensesDialog(roundingpanel, freightpanel, taxpanel, ttotalexpenses).showDialog();
        });
        
        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calculatetotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculatetotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculatetotal();
            }
            
            private void calculatetotal(){
                double rounding = getRounding();
                double freight = getFreight();
                double  tax = getTax();

                double total = freight + tax - rounding;

                setTotalexpenses(total);
            }
        };
        
        roundingpanel.setDocumentListener(documentListener);
        freightpanel.setDocumentListener(documentListener);
        taxpanel.setDocumentListener(documentListener);
    }
    
    public double getRounding(){
        return roundingpanel.getTextFieldValue();
    }
    
    public double getFreight(){
        return freightpanel.getTextFieldValue();
    }
    
    public double getTax(){
        return taxpanel.getTextFieldValue();
    }
    
    public double getTotalExpenses(){
        return totalexpenses.getTextFieldValue();
    }
    
    public void setRounding(double value){
        roundingpanel.setTextFieldValue(value);
    }
    
    public void setFreight(double value){
        freightpanel.setTextFieldValue(value);
    }
    
    public void setTax(double value){
        taxpanel.setTextFieldValue(value);
    }
    
    public void setTotalexpenses(double value){
        totalexpenses.setTextFieldValue(value);
        ttotalexpenses.setTextFieldValue(value);
    }
    
    public void setDocumentListener(DocumentListener dl){
        totalexpenses.setDocumentListener(dl);
    }
    
    public void setTextFieldEnabled(boolean status){
        roundingpanel.setTextFieldEnabled(status);
        freightpanel.setTextFieldEnabled(status);
        taxpanel.setTextFieldEnabled(status);
    }
}

class ExpensesDialog extends MyDialog{
    
    public ExpensesDialog(LabelDecimalTextFieldPanel roundingpanel, LabelDecimalTextFieldPanel freightpanel,
            LabelDecimalTextFieldPanel taxpanel, LabelDecimalTextFieldPanel totalpanel){
        super(GlobalFields.PROPERTIES.getProperty("LABEL_OTHERS"), 450, 250);
        
        box.add(roundingpanel);
        box.add(freightpanel);
        box.add(taxpanel);
        
        totalpanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        
        box.add(totalpanel);
        box.add(submitpanel);
        
        submitpanel.getSubmitButton().addActionListener((ActionEvent e) -> {
           dispose();
        });
    }
    
    public void showDialog(){
        setVisible(true);
    }
}
