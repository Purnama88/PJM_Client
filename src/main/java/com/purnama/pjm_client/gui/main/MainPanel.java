/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.main;

import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public class MainPanel extends MyPanel implements DocumentListener{
    
    public static final boolean SAVED      = true;
    public static final boolean PENDING    = false;
    
    private int index;
    
    private boolean state = SAVED;
    
    public MainPanel(String name){
        super();
        
        init(name);
    }
    
    private void init(String name){
        setName(name);
        
        setMinimumSize(new Dimension(950, Integer.MAX_VALUE));
        
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    }
    
    public void refresh(){
        
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
        if(state){
            GlobalFields.MAINTABBEDPANE.setTitleAt(getIndex(), getName());
        }
        else{
            GlobalFields.MAINTABBEDPANE.setTitleAt(getIndex(), getPendingName());
        }
    }
    
    public String getPendingName(){
        return getName() + "*";
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        setState(MainPanel.PENDING);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setState(MainPanel.PENDING);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setState(MainPanel.PENDING);
    }
}
