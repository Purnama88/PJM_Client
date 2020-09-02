/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 *
 * @author p_cor
 */
public class LabelPanel extends MyPanel{
    
    private final MyLabel label;
    
    public LabelPanel(String labelcontent){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel(labelcontent, 500);
        
        init();
    }
    
    public void init(){
        setMaximumSize(new Dimension(700, 25));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
    }
    
    public MyLabel getLabelValue(){
        return label;
    }
    
    public void setLabelValue(String value){
        label.setText(value);
    }
}
