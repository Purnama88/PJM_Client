/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.main;

import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyTextField;
import com.purnama.pjm_client.gui.library.MyToolBar;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.FlowLayout;

/**
 *
 * @author p_cor
 */
public class MainToolBar extends MyToolBar{
    
    private final MyButton newbutton, openbutton, savebutton, saveallbutton, printbutton, printpreviewbutton;
    
    private final MyTextField searchtextfield;
    
    private final MyPanel searchpanel;
    
    public MainToolBar(){
        newbutton = new MyButton(new MyImageIcon().getImage("image/NewFile_24.png"), 30, 30);
        newbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_NEWFILE"));
        
        openbutton = new MyButton(new MyImageIcon().getImage("image/OpenFile_24.png"), 30, 30);
        openbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_OPENFILE"));
        
        savebutton = new MyButton(new MyImageIcon().getImage("image/Save_24.png"), 30, 30);
        savebutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_SAVE"));
        
        saveallbutton = new MyButton(new MyImageIcon().getImage("image/SaveAll_24.png"), 30, 30);
        saveallbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_SAVEALL"));
        
        printbutton = new MyButton(new MyImageIcon().getImage("image/Print_24.png"), 30, 30);
        printbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_PRINT"));
        
        printpreviewbutton = new MyButton(new MyImageIcon().getImage("image/PrintPreview_24.png"), 30, 30);
        printpreviewbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_PRINTPREVIEW"));
        
        searchpanel = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        
        searchtextfield = new MyTextField("", 250);
        
        init();
    }
    
    private void init(){
        setFloatable(false);
        
        searchpanel.add(searchtextfield);
        
        add(newbutton);
        add(openbutton);
        this.addSeparator();
        add(savebutton);
        add(saveallbutton);
        this.addSeparator();
        add(printbutton);
        add(printpreviewbutton);
        this.addSeparator();
        add(searchpanel);
    }
}
