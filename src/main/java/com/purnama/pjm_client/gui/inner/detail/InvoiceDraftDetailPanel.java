/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.purnama.pjm_client.gui.inner.detail.util.DatePanel;
import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.gui.inner.detail.util.ExpensesPanel;
import com.purnama.pjm_client.gui.inner.detail.util.LabelTextFieldPanel;
import com.purnama.pjm_client.gui.inner.detail.util.NotePanel;
import com.purnama.pjm_client.gui.inner.detail.util.TotalPanel;
import com.purnama.pjm_client.gui.inner.detail.util.UpperPanel;
import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyTabbedPane;
import com.purnama.pjm_client.gui.main.MainPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author p_cor
 */
public abstract class InvoiceDraftDetailPanel extends MainPanel implements DocumentListener{
    
    protected final UpperPanel upperpanel;
    
    protected final MyPanel detailpanel, leftdetailpanel, middledetailpanel, rightdetailpanel,
            summarypanel, leftsummarypanel, rightsummarypanel,
            buttonpanel, leftbuttonpanel, rightbuttonpanel;
    
    protected final LabelTextFieldPanel warehousepanel, idpanel;
    
    protected final NotePanel notepanel;
    
    protected final DatePanel datepanel;
    
    protected final MyTabbedPane tabbedpane;
    
    protected final DiscountSubtotalPanel discountsubtotalpanel;
    
    protected final ExpensesPanel expensespanel;
    
    protected final TotalPanel totalpanel;
    
    protected final MyButton closebutton, savebutton, deletebutton, discardbutton;
    
    
    public InvoiceDraftDetailPanel(String name) {
        super(name);
        
        upperpanel = new UpperPanel();
        
        detailpanel = new MyPanel(new GridLayout(1, 3, 5, 0));
        
        leftdetailpanel = new MyPanel(new GridLayout(3, 1));
        middledetailpanel = new MyPanel(new GridLayout(3, 1));
        rightdetailpanel = new MyPanel(new GridLayout(3, 1));
        
        warehousepanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_WAREHOUSE"),
            "", false, this);
        
        idpanel = new LabelTextFieldPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ID"),
            "", false, this);
        
        datepanel = new DatePanel(new Date(), GlobalFields.PROPERTIES.getProperty("LABEL_DATE"));
        
        tabbedpane = new MyTabbedPane();
        tabbedpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        summarypanel = new MyPanel(new GridLayout(1, 2, 5, 0));
        
        leftsummarypanel = new MyPanel(new BorderLayout());
        rightsummarypanel = new MyPanel(new GridLayout(3, 1));
        
        discountsubtotalpanel = new DiscountSubtotalPanel();
        expensespanel = new ExpensesPanel();
        totalpanel = new TotalPanel();
        
        notepanel = new NotePanel();
        
        buttonpanel = new MyPanel(new GridLayout(1, 2, 5, 5));
        
        leftbuttonpanel = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        rightbuttonpanel = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        
        deletebutton  = new MyButton(new MyImageIcon().getImage("image/Delete_16.png"), 
                GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"), 100, 24);
        savebutton  = new MyButton(new MyImageIcon().getImage("image/Save_16.png"), 
                GlobalFields.PROPERTIES.getProperty("LABEL_SAVE"), 100, 24);
        closebutton = new MyButton(new MyImageIcon().getImage("image/Close_16.png"),
                GlobalFields.PROPERTIES.getProperty("LABEL_CLOSE"), 100, 24);
        discardbutton = new MyButton(new MyImageIcon().getImage("image/Discard_16.png"),
                GlobalFields.PROPERTIES.getProperty("LABEL_DISCARD"), 100, 24);
        
        init();
    }
    
    private void init(){
        detailpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        detailpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftdetailpanel.add(idpanel);
        leftdetailpanel.add(warehousepanel);
        
        middledetailpanel.add(datepanel);
        
        detailpanel.add(leftdetailpanel);
        detailpanel.add(middledetailpanel);
        detailpanel.add(rightdetailpanel);
        
        summarypanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        summarypanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 110));
        summarypanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 110));
        summarypanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightsummarypanel.add(discountsubtotalpanel);
        rightsummarypanel.add(expensespanel);
        rightsummarypanel.add(totalpanel);
        
        leftsummarypanel.add(new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_NOTE")), BorderLayout.NORTH);
        leftsummarypanel.add(notepanel, BorderLayout.CENTER);
        
        summarypanel.add(leftsummarypanel);
        summarypanel.add(rightsummarypanel);
        
        buttonpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonpanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonpanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftbuttonpanel.add(savebutton);
        leftbuttonpanel.add(discardbutton);
        
        rightbuttonpanel.add(deletebutton);
        rightbuttonpanel.add(closebutton);
        
        buttonpanel.add(leftbuttonpanel);
        buttonpanel.add(rightbuttonpanel);
        
        add(upperpanel);
        add(detailpanel);
        add(tabbedpane);
        add(summarypanel);
        add(buttonpanel);
        
        upperpanel.getHomeButton().addActionListener((ActionEvent e) -> {
            home();
        });
        
        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calculatetotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculatetotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculatetotal();
            }
            
            private void calculatetotal(){
                double subtotal = discountsubtotalpanel.getSubtotal();
                double discount = discountsubtotalpanel.getDiscount();
                double expenses = expensespanel.getTotalExpenses();

                double total = subtotal - discount + expenses;
                
                totalpanel.setTotal(
                        total
                );
            }
        };
        
        discountsubtotalpanel.setDocumentListener(documentListener);
        expensespanel.setDocumentListener(documentListener);
    }
    
    @Override
    public void changedUpdate(DocumentEvent e) {
//        JTabbedPane tabbedPane = (JTabbedPane)SwingUtilities.
//                getAncestorOfClass(JTabbedPane.class, this);
//        tabbedPane.setTitleAt(getIndex(), getPendingName());
//        setState(MyPanel.PENDING);
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
//        JTabbedPane tabbedPane = (JTabbedPane)SwingUtilities.
//                getAncestorOfClass(JTabbedPane.class, this);
//        tabbedPane.setTitleAt(getIndex(), getPendingName());
//        setState(MyPanel.PENDING);
    }
    
    @Override
    public void removeUpdate(DocumentEvent e) {
//        JTabbedPane tabbedPane = (JTabbedPane)SwingUtilities.
//                getAncestorOfClass(JTabbedPane.class, this);
//        tabbedPane.setTitleAt(getIndex(), getPendingName());
//        setState(MyPanel.PENDING);
    }
    
    protected abstract void load();
    protected abstract void home();
    protected abstract void save();
    protected abstract void close();
    
    @Override
    public abstract void refresh();
    
}
