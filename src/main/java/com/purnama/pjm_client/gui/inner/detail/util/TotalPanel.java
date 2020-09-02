/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.FlowLayout;

/**
 *
 * @author p_cor
 */
public class TotalPanel extends MyPanel{
    
    
    private final LabelDecimalTextFieldPanel tpanel;
    
    public TotalPanel(){
        super(new FlowLayout(FlowLayout.RIGHT));
        
        tpanel = new LabelDecimalTextFieldPanel(GlobalFields.
                PROPERTIES.getProperty("LABEL_TOTAL"), 0);
        
        init();
    }
    
    private void init(){
        tpanel.setTextFieldEnabled(false);
        
        add(tpanel);
    }
    
    public double getTotal(){
        return tpanel.getTextFieldValue();
    }
    
    public void setTotal(double value){
        tpanel.setTextFieldValue(value);
    }
}