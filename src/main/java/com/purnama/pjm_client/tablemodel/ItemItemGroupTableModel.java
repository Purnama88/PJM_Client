/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.combine.ItemItemGroup;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemItemGroupTableModel extends AbstractTableModel{
    private List<ItemItemGroup> itemitemgrouplist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_LABEL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemItemGroupTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemitemgrouplist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemItemGroup itemitemgroup = itemitemgrouplist.get(rowIndex);
        
        Object[] values = new Object[]{
            itemitemgroup.getItem().getCode(), 
            itemitemgroup.getItem().getName(),
            itemitemgroup.getItem().getLabel().toString(),
            itemitemgroup.getItem().isStatus()
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
    
    public List<ItemItemGroup> getItemItemGroupList(){
        return itemitemgrouplist;
    }
    
    public void setItemItemGroupList(List<ItemItemGroup> itemitemgrouplist){
        this.itemitemgrouplist = itemitemgrouplist;
        fireTableDataChanged();
    }
    
    public ItemItemGroup getItemItemGroup(int index){
        return itemitemgrouplist.get(index);
    }
    
    public void addRow(ItemItemGroup itemitemgroup) {
        itemitemgrouplist.add(itemitemgroup);
        fireTableDataChanged();
    }
    
    public void deleteRow(int rownum){
          
        itemitemgrouplist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
    }
}
