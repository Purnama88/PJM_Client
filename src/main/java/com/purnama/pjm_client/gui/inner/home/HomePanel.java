/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home;

import com.purnama.pjm_client.gui.inner.home.util.UpperPanel;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.gui.main.MainPanel;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public abstract class HomePanel extends MainPanel{
    
    protected UpperPanel upperpanel;
    
    protected final MyTable table;
    
    protected final MyScrollPane scrollpane;
    
    protected final JMenuItem menuitemedit, menuitemdetail;
    
    protected TableRowSorter<TableModel> sorter;
    
    protected int totalpages , page = 1;
    
    public HomePanel(String name) {
        super(name);
        
        upperpanel = new UpperPanel();
        
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        menuitemedit = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_EDIT"),
                new MyImageIcon().getImage("image/Edit_16.png"));
        menuitemdetail = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DETAIL"),
                new MyImageIcon().getImage("image/Detail_16.png"));
        
        init();
    }
    
    private void init(){
        scrollpane.getViewport().add(table);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        upperpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(upperpanel);
        add(scrollpane);
        
        table.addMenuItemSeparator();
        table.addMenuItem(menuitemdetail);
        table.addMenuItemSeparator();
        table.addMenuItem(menuitemedit);
        
        load();
        
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                Point p = e.getPoint();
		if(SwingUtilities.isLeftMouseButton(e)){
                    if(e.getClickCount() ==2){
                        detail();
                    }
		}
		else if(SwingUtilities.isRightMouseButton(e)){
                    int rowNumber = table.rowAtPoint( p );
                    ListSelectionModel model = table.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
		}
            }
        });
        
        upperpanel.getSearchTextField().getDocument().addDocumentListener(
        new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                load();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                load();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                load();
            }
        });
        
        upperpanel.getAddButton().addActionListener((ActionEvent e) -> {
            add();
        });
        
        menuitemdetail.addActionListener((ActionEvent e) -> {
            detail();
        });
        
        menuitemedit.addActionListener((ActionEvent e) -> {
            edit();
        });
        
        upperpanel.getFirstPageButton().addActionListener((ActionEvent e) -> {
            firstpage();
        });
        
        upperpanel.getPreviousPageButton().addActionListener((ActionEvent e) -> {
            previouspage();
        });
        
        upperpanel.getNextPageButton().addActionListener((ActionEvent e) -> {
            nextpage();
        });
        
        upperpanel.getLastPageButton().addActionListener((ActionEvent e) -> {
            lastpage();
        });
    }
    
    protected abstract void load();
    protected abstract void add();
    protected abstract void detail();
    protected abstract void edit();
    
    @Override
    public void refresh(){
        load();
    }
    
    protected final void firstpage() {
        if(page != 1){
            page = 1;
            upperpanel.setCurrentPageLabel(page + "");
            load();
        }
    }

    protected final void previouspage() {
        if(page != 1){
            page -= 1;
            upperpanel.setCurrentPageLabel(page + "");
            load();
        }
    }

    protected final void nextpage() {
        if(page != totalpages){
            page += 1;
            upperpanel.setCurrentPageLabel(page + "");
            load();
        }
    }

    protected final void lastpage() {
        if(page != totalpages){
            page = totalpages;
            upperpanel.setCurrentPageLabel(page + "");
            load();
        }
    }
    
}
