/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import com.purnama.pjm_client.util.GlobalFields;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

/**
 *
 * @author p_cor
 */
public class MyTextArea extends JTextArea{
    
    private final JPopupMenu popupmenu;
    
    private final JMenuItem menuitemcopy, menuitemselect, menuitempaste, menuitemcut;
    
    public MyTextArea(String content, int width, int height){
        super(content, width, height);
        
        
        popupmenu = new JPopupMenu();
        
        menuitemcopy = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_COPY"),
                new MyImageIcon().getImage("image/Copy_16.png"));
        menuitemselect = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_SELECTALL"),
                new MyImageIcon().getImage("image/Select_16.png"));
        menuitempaste = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_PASTE"),
                new MyImageIcon().getImage("image/Paste_16.png"));
        menuitemcut = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_CUT"),
                new MyImageIcon().getImage("image/Cut_16.png"));
        
        init();
    }
    
    public MyTextArea(String content){
        super(content);
        
        popupmenu = new JPopupMenu();
        
        menuitemcopy = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_COPY"),
                new MyImageIcon().getImage("image/Copy_16.png"));
        menuitemselect = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_SELECTALL"),
                new MyImageIcon().getImage("image/Select_16.png"));
        menuitempaste = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_PASTE"),
                new MyImageIcon().getImage("image/Paste_16.png"));
        menuitemcut = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_CUT"),
                new MyImageIcon().getImage("image/Cut_16.png"));
        
        init();
    }
    
    private void init(){
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLineWrap(true);
        setWrapStyleWord(true);
        
        popupmenu.add(menuitemselect);
        popupmenu.addSeparator();
        popupmenu.add(menuitemcopy);
        popupmenu.add(menuitemcut);
        popupmenu.addSeparator();
        popupmenu.add(menuitempaste);
        
        setComponentPopupMenu(popupmenu);
        
        menuitemcut.addActionListener((ActionEvent e) -> {
            cut();
        });
        
        menuitemcopy.addActionListener((ActionEvent e) -> {
            copy();
        });
        
        menuitempaste.addActionListener((ActionEvent e) -> {
           paste(); 
        });
        
        menuitemselect.addActionListener((ActionEvent e) -> {
           selectAll(); 
        });
        
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll();
            }
        });
    }
}
