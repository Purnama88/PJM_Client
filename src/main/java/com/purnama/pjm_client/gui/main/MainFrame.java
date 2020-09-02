/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.main;

import com.purnama.pjm_client.gui.library.MyFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

/**
 *
 * @author p_cor
 */
public class MainFrame extends MyFrame{
    
    private final MainMenuBar menubar;
    
//    private final MainPanel mainpanel;
    
    private final MainTabbedPane maintabbedpane;
    
    private final MainToolBar maintoolbar;
    
    private final MainNavigation navigationpanel;
    
    private final JSplitPane splitpane;
    
    public MainFrame(){
        super("");
        
        menubar = new MainMenuBar();
//        mainpanel = new MyPanel();
        maintoolbar = new MainToolBar();
        maintabbedpane = new MainTabbedPane();
        navigationpanel = new MainNavigation(maintabbedpane);
        
        splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationpanel, maintabbedpane);
        splitpane.setBorder(BorderFactory.createEmptyBorder());
        
        init();
        
    }
    
    private void init(){
        setJMenuBar(menubar);
        
        setMinimumSize(new Dimension(1240,550));
        
        add(maintoolbar, BorderLayout.NORTH);
        add(splitpane, BorderLayout.CENTER);
//        add(mainpanel, BorderLayout.SOUTH);
        
        maximize();
    }
}
