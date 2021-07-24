/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form;

import com.purnama.pjm_client.gui.inner.form.util.LabelPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextAreaPanel;
import com.purnama.pjm_client.gui.inner.form.util.StatusPanel;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.inner.form.util.UpperPanel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.main.MainPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 *
 * @author p_cor
 */
public abstract class FormPanel extends MainPanel implements ActionListener{

    protected final UpperPanel upperpanel;
    
    protected final MyScrollPane scrollpane;
    
    protected final MyPanel detailpanel;
    
    protected final SubmitPanel submitpanel;
    
    protected final StatusPanel statuspanel;
    
    protected final LabelTextAreaPanel notepanel;
    
    protected final LabelPanel remarkemptylabel, remarknoneditablelabel, remarknolimitlabel;
    
    public FormPanel(String name) {
        super(name);
        
        upperpanel = new UpperPanel();
        
        scrollpane = new MyScrollPane();
        
        detailpanel = new MyPanel();
        
        submitpanel = new SubmitPanel();
        
        statuspanel = new StatusPanel(GlobalFields.PROPERTIES.getProperty("LABEL_STATUS"));
        
        notepanel = new LabelTextAreaPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NOTE"), "");
        
        remarkemptylabel = new LabelPanel(GlobalFields.PROPERTIES.getProperty("REMARK_EMPTY"));
        remarknoneditablelabel = new LabelPanel(GlobalFields.PROPERTIES.getProperty("REMARK_NONEDITABLE"));
        remarknolimitlabel = new LabelPanel(GlobalFields.PROPERTIES.getProperty("REMARK_NOLIMIT"));
        
        init();
    }
    
    private void init(){
        detailpanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        detailpanel.setLayout(new BoxLayout(detailpanel, BoxLayout.Y_AXIS));
        
        scrollpane.getViewport().add(detailpanel);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        submitpanel.getSubmitButton().addActionListener(this);
        
        add(upperpanel);
        add(scrollpane);
        add(submitpanel);
        
        upperpanel.getHomeButton().addActionListener((ActionEvent e) -> {
            home();
        });
    }
    
    protected abstract boolean validateinput();
    protected abstract void submit();
    protected abstract void home();
    protected abstract void detail(int id);
    
    @Override
    public abstract void refresh();
    
    @Override
    public void actionPerformed(ActionEvent e) {
        submit();
    }
}
