/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Menu;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class MenuTableModel extends AbstractTableModel{
    private List<Menu> menulist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public MenuTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return menulist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Menu menu = menulist.get(rowIndex);

        Object[] values = new Object[]{
            menu.getName(),
            menu.isStatus()
        };
        return values[columnIndex];
    }
    
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public List<Menu> getMenuList(){
        return menulist;
    }
    
    public void setMenuList(List<Menu> menulist){
        this.menulist = menulist;
        fireTableDataChanged();
    }
    
    public Menu getMenu(int index){
        return menulist.get(index);
    }
}
