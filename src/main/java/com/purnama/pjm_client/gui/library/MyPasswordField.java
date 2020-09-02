/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.purnama.pjm_client.gui.library;

import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author Purnama
 */
public class MyPasswordField extends JPasswordField{
    private final JPopupMenu popupmenu;
    
    private final JMenuItem menuitemselect, menuitempaste;
    
    public MyPasswordField(String title, int width){
        super(title);
        
        popupmenu = new JPopupMenu();
        
        menuitemselect = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_SELECTALL"),
                new MyImageIcon().getImage("image/Select_16.png"));
        menuitempaste = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_PASTE"),
                new MyImageIcon().getImage("image/Paste_16.png"));
        
        init(width);
    }
    
    private void init(int width){
        setPreferredSize(new Dimension(width, 25));
        
        popupmenu.add(menuitemselect);
        popupmenu.addSeparator();
        popupmenu.add(menuitempaste);
        
        setComponentPopupMenu(popupmenu);
        
        menuitempaste.addActionListener((ActionEvent e) -> {
           paste(); 
        });
        
        menuitemselect.addActionListener((ActionEvent e) -> {
           selectAll(); 
        });
        
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    selectAll();
                });
            }
        });
    }
    
    @Override
    public String getText(){
        return String.copyValueOf(getPassword());
    }
    
    public boolean isEmpty(){
        return getText().matches("^\\s*$");
    }
    
    public boolean isLongLessThan(int lo){
        return getText().matches("[A-Za-z0-9/\\\\\\-]" + "{" + lo + "}");
    }
    
    public boolean isLongBetween(int min, int max){
        return getText().matches("[A-Za-z0-9/\\\\\\-]" + "{" + min + "," + max + "}");
    }
}
