/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Date;

/**
 *
 * @author p_cor
 */
public class DatePanel extends MyPanel {
    
    private final MyLabel label;
    
    private final JDateChooser datechooser;
    
    public DatePanel(Date date, String labelvalue){
        
        super(new FlowLayout(FlowLayout.LEFT));
        
        label = new MyLabel(labelvalue, 100);
        
        datechooser = new JDateChooser(date);
        
        init();
    }
    
    private void init(){
        datechooser.setPreferredSize(new Dimension(200, 25));
        
        add(label);
        add(datechooser);
    }
    
    public Date getDate(){
        return datechooser.getDate();
    }
    
    @Override
    public void setEnabled(boolean value){
        datechooser.setEnabled(false);
    }
    
    public void setDate(Date date){
        datechooser.setDate(date);
    }
    
}
