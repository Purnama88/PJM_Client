/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.detail.util.UpperPanel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTabbedPane;
import com.purnama.pjm_client.gui.main.MainPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 *
 * @author p_cor
 */
public abstract class DetailPanel extends MainPanel{
    
    protected final UpperPanel upperpanel;
    protected final MyTabbedPane tabbedpane;

    protected final MyScrollPane scrollpane;
    
    protected final MyPanel detailpanel;
    
    protected final SelectableLabelContentPanel idpanel, statuspanel, lastmodifiedpanel, notepanel;
    
    public DetailPanel(String name) {
        super(name);
        
        upperpanel = new UpperPanel();
        
        tabbedpane = new MyTabbedPane();
        
        scrollpane = new MyScrollPane();
        
        detailpanel = new MyPanel();
        
        idpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ID"),
                "");
        
        statuspanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_STATUS"),
                "");
        
        lastmodifiedpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_LASTMODIFIED"),
                "");
        
        notepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NOTE"),
                "");
        
        init();
    }
    
    private void init(){
        
        add(upperpanel);
        add(tabbedpane);
        
        upperpanel.addEditButton();
        
        scrollpane.setBorder(null);
        
        tabbedpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_CONTENT"), scrollpane);
        
        detailpanel.setLayout(new BoxLayout(detailpanel, BoxLayout.Y_AXIS));
        detailpanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        scrollpane.getViewport().add(detailpanel);
        
        detailpanel.add(idpanel);
        
        upperpanel.getHomeButton().addActionListener((ActionEvent e) -> {
            home();
        });
        
        upperpanel.getEditButton().addActionListener((ActionEvent e) -> {
            edit();
        });
    }
    
    protected abstract void load();
    protected abstract void home();
    protected abstract void edit();
    
    @Override
    public void refresh(){
        load();
    }
}
