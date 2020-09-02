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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
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
    
    protected final MyPanel okpanel;
    
    protected final MyButton okbutton;
    
    private final int width, height;
    
    public MyDialog(String title, int width, int height){
        super(GlobalFields.MAINFRAME, title, Dialog.ModalityType.DOCUMENT_MODAL);
        
        this.width = width;
        this.height = height;
        
        box = Box.createVerticalBox();
        
        submitpanel = new SubmitPanel();
        
        okpanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        
        okbutton = new MyButton(GlobalFields.PROPERTIES.getProperty("LABEL_OK"));
        
        init();
    }
    
    private void init(){
        
        setPreferredSize(new Dimension(width, height));
        
        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        submitpanel.addCancelButton();
        
        okpanel.add(okbutton);
        
        add(box);
        
        pack();
        
        setLocationRelativeTo(GlobalFields.MAINFRAME);
        
        submitpanel.getCancelButton().addActionListener((ActionEvent e) -> {
           dispose();
        });
        
        okbutton.addActionListener((ActionEvent e) -> {
           dispose();
        });
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}