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
import javax.swing.JMenuItem;

/**
 *
 * @author p_cor
 */
public abstract class TablePanel extends MyScrollPane{
    
    protected final MyTable table;

    protected final JMenuItem menuitemdelete;
    
    public TablePanel(){
        super();
        
        table = new MyTable();
        
        menuitemdelete = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"),
                new MyImageIcon().getImage("image/Delete_16.png"));
        
        init();
    }
    
    private void init(){
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        setBorder(null);
        getViewport().add(table);
        
        table.addMenuItemSeparator();
        table.addMenuItem(menuitemdelete);
        
    }
    
    public MyTable getTable(){
        return table;
    }
    
    public abstract void load();
}
