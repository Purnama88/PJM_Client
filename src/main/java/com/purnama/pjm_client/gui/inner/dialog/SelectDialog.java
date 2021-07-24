/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.dialog;

import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.library.MyDialog;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public abstract class SelectDialog extends MyDialog{
    
    protected final LabelTextFieldErrorPanel dbsearchpanel, tablesearchpanel;
    
    protected final MyScrollPane scrollpane;
    
    protected final TableRowSorter<TableModel> sorter;
    
    protected final MyTable table;
    
    public SelectDialog(String title) {
        super(title, GlobalFields.DIALOG_WIDTH, GlobalFields.DIALOG_HEIGHT);
        
        dbsearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHDATABASE"), "");
        tablesearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHTABLE"), "");
        
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        init();
    }
    
    private void init(){
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
              load();
            }
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
              load();
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
              load();
            }
        };
        dbsearchpanel.setDocumentListener(documentListener);
        
        scrollpane.getViewport().add(table);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box.add(dbsearchpanel);
        
        box.add(tablesearchpanel);
        box.add(scrollpane);
        box.add(submitpanel);
        
        submitpanel.getCancelButton().addActionListener(e -> {
            table.clearSelection();
            dispose();
        });
        
        submitpanel.getSubmitButton().addActionListener(e -> {
            dispose();
        });
        
        this.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
              table.clearSelection();
            }
        });
    }
    
    protected String getSearchKeyword(){
        String text = dbsearchpanel.getTextFieldValue();
        String newtext = text.replaceAll(" ", "%20");
        return newtext;
    }
    
    protected abstract void load();
    public abstract Object showDialog();
}
