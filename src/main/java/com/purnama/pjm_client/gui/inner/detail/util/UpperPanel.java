/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JProgressBar;

/**
 *
 * @author p_cor
 */
public class UpperPanel extends MyPanel{
    
    private final MyPanel leftpanel, middlepanel, rightpanel;
    
    private final MyButton homebutton, editbutton;
    
    private final JProgressBar progressbar;
    
    private final MyLabel notiflabel, statuslabel;
    
    public UpperPanel(){
        super(new GridLayout(1, 3));
        
        leftpanel = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        middlepanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        rightpanel = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        
        homebutton = new MyButton(new MyImageIcon().getImage("image/Home_16.png"), 24, 24);
        homebutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_HOME"));
        editbutton = new MyButton(new MyImageIcon().getImage("image/Edit_16.png"), 24, 24);
        editbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_EDIT"));
        
        statuslabel = new MyLabel("");
        
        progressbar = new JProgressBar();
        progressbar.setIndeterminate(true);
        progressbar.setVisible(false);
        
        notiflabel = new MyLabel("");
        
        
        init();
    }
    
    private void init(){
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        leftpanel.add(homebutton);
        
        middlepanel.add(statuslabel);
        
        rightpanel.add(notiflabel);
        rightpanel.add(progressbar);
        
        add(leftpanel);
        add(middlepanel);
        add(rightpanel);
    }
    
    public MyPanel getMiddlePanel(){
        return middlepanel;
    }
        
    public void setStatusLabel(String value){
        statuslabel.setText(value);
    }
    
    public void setNotifLabel(String value){
        notiflabel.setText(value);
    }
    
    public void hideProgressBar(){
        progressbar.setVisible(false);
    }
    
    public void showProgressBar(){
        progressbar.setVisible(true);
    }
    
    public MyButton getHomeButton(){
        return homebutton;
    }
    
    public MyButton getEditButton(){
        return editbutton;
    }
    
    public void addEditButton(){
        leftpanel.add(editbutton);
    }
    
    public void removeEditButton(){
        leftpanel.remove(editbutton);
    }
}
