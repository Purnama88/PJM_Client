/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MySelectableLabel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 *
 * @author p_cor
 */
public class SelectableLabelContentPanel extends MyPanel{
    
    private final MySelectableLabel label, content;
    
    public SelectableLabelContentPanel(String labelvalue, String contentvalue){
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MySelectableLabel(labelvalue, 150);
        content = new MySelectableLabel(contentvalue, 500);
        
        init();
    }
    
    private void init(){
        add(label);
        add(new MyLabel(":", 10));
        add(content);
        
        setMaximumSize(new Dimension(700, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    public void setLabelValue(String value){
        label.setText(value);
    }
    
    public void setContentValue(boolean value){
        if(value){
            content.setText(GlobalFields.PROPERTIES.getProperty("LABEL_ACTIVE"));
        }
        else{
            content.setText(GlobalFields.PROPERTIES.getProperty("LABEL_INACTIVE"));
        }
    }
    
    public void setContentValue(String value){
        content.setText(value);
    }
}