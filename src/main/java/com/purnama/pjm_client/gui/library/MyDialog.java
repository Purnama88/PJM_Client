/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Dialog;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 *
 * @author p_cor
 */
public class MyDialog extends JDialog{
    
    protected final Box box;
    
    protected final SubmitPanel submitpanel;
    
    private final int width, height;
    
    public MyDialog(String title, int width, int height){
        super(GlobalFields.MAINFRAME, title, Dialog.ModalityType.DOCUMENT_MODAL);
        
        this.width = width;
        this.height = height;
        
        box = Box.createVerticalBox();
        
        submitpanel = new SubmitPanel();
        
        init();
    }
    
    private void init(){
        
        setPreferredSize(new Dimension(width, height));
        
        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        submitpanel.addCancelButton();
        
        add(box);
        
        pack();
        
        setLocationRelativeTo(GlobalFields.MAINFRAME);
        
        submitpanel.getCancelButton().addActionListener( e -> {
           dispose();
        });
        
        submitpanel.getSubmitButton().addActionListener( e -> {
           dispose();
        });
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
}