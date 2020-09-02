/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 *
 * @author p_cor
 */
public class SubmitPanel extends MyPanel{
    
    private final MyButton submitbutton, cancelbutton;
    
    public SubmitPanel(){
        super(new FlowLayout(FlowLayout.CENTER));
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        submitbutton = new MyButton(GlobalFields.PROPERTIES.getProperty("LABEL_SUBMIT"));
        cancelbutton = new MyButton(GlobalFields.PROPERTIES.getProperty("LABEL_CANCEL"));
        
        add(submitbutton);
    }
    
    public void addCancelButton(){
        add(cancelbutton);
    }
    
    public void loading(){
        submitbutton.setIcon(new MyImageIcon().getImage("image/Loading_16.gif"));
        submitbutton.setText("");
        cancelbutton.setEnabled(false);
        disable();
    }
    
    public void finish(){
        submitbutton.setText(GlobalFields.PROPERTIES.getProperty("LABEL_SUBMIT"));
        submitbutton.setIcon(null);
        cancelbutton.setEnabled(true);
        enable();
    }
    
    public MyButton getCancelButton(){
        return cancelbutton;
    }
    
    public MyButton getSubmitButton(){
        return submitbutton;
    }
    
    @Override
    public void disable(){
        submitbutton.setEnabled(false);
        cancelbutton.setEnabled(false);
    }
    
    @Override
    public void enable(){
        submitbutton.setEnabled(true);
        cancelbutton.setEnabled(true);
    }
}
