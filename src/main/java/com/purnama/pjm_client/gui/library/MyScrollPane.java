/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

/**
 *
 * @author p_cor
 */
public class MyScrollPane extends JScrollPane{
    
    public MyScrollPane(Component view){
        super(view);
        
        init();
    }
    
    public MyScrollPane(){
        super();
        
        init();
    }
    
    private void init(){
        setBorder(null);
    }
    
}
