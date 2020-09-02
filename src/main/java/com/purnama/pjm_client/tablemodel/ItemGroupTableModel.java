/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemGroupTableModel extends AbstractTableModel{
    private List<ItemGroup> itemgrouplist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemGroupTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemgrouplist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemGroup itemgroup = itemgrouplist.get(rowIndex);
        
        Object[] values = new Object[]{
            itemgroup.getCode(), 
            itemgroup.getName(),
            itemgroup.isStatus()
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
    
    public List<ItemGroup> getItemGroupList(){
        return itemgrouplist;
    }
    
    public void setItemGroupList(List<ItemGroup> itemgrouplist){
        this.itemgrouplist = itemgrouplist;
        fireTableDataChanged();
    }
    
    public ItemGroup getItemGroup(int index){
        return itemgrouplist.get(index);
    }
}
