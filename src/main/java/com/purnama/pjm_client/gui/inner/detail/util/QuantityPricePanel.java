/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyCheckBox;
import com.purnama.pjm_client.gui.library.MyDecimalTextField;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;

/**
 *
 * @author p_cor
 */
public class QuantityPricePanel extends MyPanel{
    
    private final MyDecimalTextField quantitytextfield, pricetextfield;
    
    private final MyCheckBox checkbox;
    
    private final MyLabel label, errorlabel;
    
    private QuantityPricePanel previous, next;
    
    private Object actioncommand;
   
    private boolean state = false;
    
    public QuantityPricePanel(double quantity, double price){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel("<HTML>&#x2264;</HTML>", 25);
        errorlabel = new MyLabel("", 250);
        
        quantitytextfield = new MyDecimalTextField(quantity, 100);
        pricetextfield = new MyDecimalTextField(price, 175);
        
        checkbox = new MyCheckBox("");
        
        init();
    }
    
    private void init(){
        
        setMinimumSize(new Dimension(350, 65));
        setPreferredSize(new Dimension(350, 65));
        setMaximumSize(new Dimension(350, 65));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(quantitytextfield);
        add(pricetextfield);
        add(checkbox);
        add(errorlabel);
        
        setPriceTextFieldEditable(false);
        setQuantityTextFieldEditable(false);
        
        checkbox.addItemListener((ItemEvent e) -> {
            if(checkbox.isSelected()){
                setPriceTextFieldEditable(true);
                setQuantityTextFieldEditable(true);
                setState(true);
            }
            else{
                setPriceTextFieldEditable(false);
                setQuantityTextFieldEditable(false);

                setPriceTextFieldValue(1);
                setQuantityTextFieldValue(1);

                setErrorLabel("");
                setState(false);

                try{
                    next.setCheckBoxValue(false);
                }
                catch(NullPointerException ex){

                }
            }
        });
    }
    
    public void removeCheckBox(){
        remove(checkbox);
    }

    public void setCheckBoxValue(boolean value){
        checkbox.setSelected(value);
    }
    
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public QuantityPricePanel getPrevious() {
        return previous;
    }

    public void setPrevious(QuantityPricePanel previous) {
        this.previous = previous;
    }

    public QuantityPricePanel getNext() {
        return next;
    }

    public void setNext(QuantityPricePanel next) {
        this.next = next;
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
    
    public void setPriceTextFieldEditable(boolean status){
        pricetextfield.setEditable(status);
    }
    
    public void setQuantityTextFieldEditable(boolean status){
        quantitytextfield.setEditable(status);
    }

    public void setPriceTextFieldValue(Object value){
        pricetextfield.setValue(value);
    }
    
    public void setQuantityTextFieldValue(Object value){
        quantitytextfield.setValue(value);
    }
    
    public double getPriceTextFieldDecimalValue(){
        return GlobalFunctions.convertToDouble(pricetextfield.getText());
    }
    
    public int getPriceTextFieldIntegerValue(){
        return GlobalFunctions.convertToInteger(pricetextfield.getText());
    }
    
    public double getQuantityTextFieldDecimalValue(){
        return GlobalFunctions.convertToDouble(quantitytextfield.getText());
    }
    
    public int getQuantityTextFieldIntegerValue(){
        return GlobalFunctions.convertToInteger(quantitytextfield.getText());
    }
    
    public boolean verify(){

        if(isState()){
            if(previous.getQuantityTextFieldIntegerValue() < getQuantityTextFieldIntegerValue() && 
                    previous.getPriceTextFieldDecimalValue() > getPriceTextFieldDecimalValue()){

                setErrorLabel("");

                return true;
            }
            else{
                setErrorLabel("Error");

                return false;
            }
        }
        else{
            return true;
        }
    }
}