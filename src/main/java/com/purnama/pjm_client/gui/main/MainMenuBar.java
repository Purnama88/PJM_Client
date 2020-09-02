/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.main;

import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author p_cor
 */
public class MainMenuBar extends JMenuBar{
    
    private final JMenu file, help;
    
    private final JMenuItem newfile, openfile, save, saveall, print, printpreview, about, exit;
    
    public MainMenuBar(){
        super();
        
        file = new JMenu(GlobalFields.PROPERTIES.getProperty("LABEL_FILE"));
        file.setMnemonic(KeyEvent.VK_F);
        
        help = new JMenu(GlobalFields.PROPERTIES.getProperty("LABEL_HELP"));
        help.setMnemonic(KeyEvent.VK_H);
        
        newfile = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_NEWFILE"), new MyImageIcon().getImage("image/NewFile_16.png"));
        openfile = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_OPENFILE"), new MyImageIcon().getImage("image/OpenFile_16.png"));
        save = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_SAVE"), new MyImageIcon().getImage("image/Save_16.png"));
        saveall = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_SAVEALL"), new MyImageIcon().getImage("image/SaveAll_16.png"));
        print = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_PRINT"), new MyImageIcon().getImage("image/Print_16.png"));
        printpreview = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_PRINTPREVIEW"), new MyImageIcon().getImage("image/PrintPreview_16.png"));
        exit = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_EXIT"));
        about = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_ABOUT"));
        
        init();
        
    }
    
    private void init(){
        file.add(newfile);
        file.add(openfile);
        file.addSeparator();
        file.add(save);
        file.add(saveall);
        file.addSeparator();
        file.add(print);
        file.add(printpreview);
        file.addSeparator();
        file.add(exit);
        
        help.add(about);
        
        add(file);
        add(help);
    }
}
