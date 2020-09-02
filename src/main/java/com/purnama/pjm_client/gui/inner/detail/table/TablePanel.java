/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.table;

import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author p_cor
 */
public abstract class TablePanel extends MyScrollPane{
    
    protected final MyTable table;
    
    protected final JPopupMenu popupmenu;
    
    protected final JMenuItem menuitemdelete, menuitemcopy;
    
    public TablePanel(){
        super();
        
        table = new MyTable();
        
        popupmenu = new JPopupMenu();
        
        menuitemdelete = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"),
                new MyImageIcon().getImage("image/Delete_16.png"));
        menuitemcopy = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_COPY"),
                new MyImageIcon().getImage("image/Copy_16.png"));
        
        init();
    }
    
    private void init(){
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        setBorder(null);
        getViewport().add(table);
        
        popupmenu.add(menuitemcopy);
        popupmenu.addSeparator();
        popupmenu.add(menuitemdelete);
        
        table.setComponentPopupMenu(popupmenu);
        
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                Point p = e.getPoint();
		if(SwingUtilities.isRightMouseButton(e)){
                    int rowNumber = table.rowAtPoint( p );
                    ListSelectionModel model = table.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
		}
            }
        });
        
        menuitemcopy.addActionListener((ActionEvent e) -> {
            try{
                StringSelection stringselection = new StringSelection(table.
                        getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString());

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringselection,
                        null);
            }
            catch(HeadlessException exp){
                exp.printStackTrace();
            }
        });
    }
    
    public MyTable getTable(){
        return table;
    }
    
    public abstract void load();
}
