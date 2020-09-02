/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemTableModel extends AbstractTableModel{
    private List<Item> itemlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_LABEL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item item = itemlist.get(rowIndex);
        
        Object[] values = new Object[]{
            item.getCode(), 
            item.getName(),
            item.getLabel().toString(),
            item.isStatus()
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
    
    public List<Item> getItemList(){
        return itemlist;
    }
    
    public void setItemList(List<Item> itemlist){
        this.itemlist = itemlist;
        fireTableDataChanged();
    }
    
    public Item getItem(int index){
        return itemlist.get(index);
    }
}
