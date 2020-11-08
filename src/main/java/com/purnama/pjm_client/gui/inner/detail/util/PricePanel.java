/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyDecimalTextField;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 *
 * @author p_cor
 */
public class PricePanel extends MyPanel{
    
    private final MyScrollPane sellpricescrollpane, buypricescrollpane;
    
    private final MyPanel sellpricepanel, buypricepanel;
    
    private final QuantityPricePanel testpanel, testpanel2, testpanel3, testpanel4, testpanel5, 
            testpanel6, testpanel7, testpanel8, testpanel9, testpanel10;
    
    public PricePanel(){
        super(new GridLayout(1, 3, 5, 5));
        
        sellpricescrollpane = new MyScrollPane();
        buypricescrollpane = new MyScrollPane();
        
        sellpricepanel = new MyPanel();
        buypricepanel = new MyPanel();
       
        testpanel = new QuantityPricePanel(1, 1);
        testpanel2 = new QuantityPricePanel(1, 1);
        testpanel3 = new QuantityPricePanel(1, 1);
        testpanel4 = new QuantityPricePanel(1, 1);
        testpanel5 = new QuantityPricePanel(1, 1);
        testpanel6 = new QuantityPricePanel(1, 1);
        testpanel7 = new QuantityPricePanel(1, 1);
        testpanel8 = new QuantityPricePanel(1, 1);
        testpanel9 = new QuantityPricePanel(1, 1);
        testpanel10 = new QuantityPricePanel(1, 1);
        
        init();
    }
    
    private void init(){
        
        sellpricescrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), GlobalFields.PROPERTIES.getProperty("LABEL_SELLPRICE")));
        buypricescrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), GlobalFields.PROPERTIES.getProperty("LABEL_BUYPRICE")));
        
        sellpricepanel.setLayout(new BoxLayout(sellpricepanel, BoxLayout.Y_AXIS));
        buypricepanel.setLayout(new BoxLayout(buypricepanel, BoxLayout.Y_AXIS));
        
        sellpricescrollpane.getViewport().add(sellpricepanel);
        buypricescrollpane.getViewport().add(buypricepanel);
        
        sellpricepanel.add(testpanel);
        sellpricepanel.add(testpanel2);
        sellpricepanel.add(testpanel3);
        sellpricepanel.add(testpanel4);
        sellpricepanel.add(testpanel5);
        buypricepanel.add(testpanel6);
        buypricepanel.add(testpanel7);
        buypricepanel.add(testpanel8);
        buypricepanel.add(testpanel9);
        buypricepanel.add(testpanel10);
        
       testpanel.setErrorLabel("The value must be bigger than");
        
        add(sellpricescrollpane);
        add(buypricescrollpane);
    }
}

class QuantityPricePanel extends MyPanel{
    
    private final MyDecimalTextField quantitytextfield, pricetextfield;
    
    private final MyLabel label, errorlabel;
    
    private Object actioncommand;
    
    public QuantityPricePanel(double quantity, double price){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel("Min", 25);
        errorlabel = new MyLabel("", 250);
        
        quantitytextfield = new MyDecimalTextField(quantity, 100);
        pricetextfield = new MyDecimalTextField(price, 200);
        
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
        add(errorlabel);
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
}