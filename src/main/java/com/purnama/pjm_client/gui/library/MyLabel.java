/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author p_cor
 */
public class MyLabel extends JLabel{
    public MyLabel(){
        
    }
    
    public MyLabel(String title){
        super(title);
    }
    
    public MyLabel(String title, int width){
        super(title);
        setPreferredSize(new Dimension(width, 25));
    }
    
    public MyLabel(ImageIcon imageicon){
        super(imageicon);
    }
    
    public MyLabel(ImageIcon imageicon, int width, int height){
        super(imageicon);
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 10 ,10 ,10));
        setPreferredSize(new Dimension(width, height));
    }
}