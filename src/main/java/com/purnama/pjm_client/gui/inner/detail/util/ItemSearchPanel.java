/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.purnama.pjm_client.gui.inner.form.util.LabelComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyTextField;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 *
 * @author p_cor
 */
public class ItemSearchPanel extends MyPanel{

    private final MyButton searchbutton;
    
    private final LabelTextFieldErrorPanel itempanel, modelpanel;
    
    private final LabelComboBoxPanel labelcombobox;
    
    public ItemSearchPanel(){
        super();
    
        searchbutton = new MyButton(new MyImageIcon().getImage("image/Search_16.png"), 24, 24);
        
        itempanel = new LabelTextFieldErrorPanel("Item", "");
        
        modelpanel = new LabelTextFieldErrorPanel("Model", "");
        
        labelcombobox = new LabelComboBoxPanel();
        
        init();
    }
    
    private void init(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), "Search"));
        
        add(itempanel);
        add(modelpanel);
        add(labelcombobox);
    }
}
