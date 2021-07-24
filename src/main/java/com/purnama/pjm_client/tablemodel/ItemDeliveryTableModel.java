/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemDelivery;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemDeliveryTableModel extends AbstractTableModel{
    private List<ItemDelivery> itemdeliverylist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_REMARK"),
    };
    
    public ItemDeliveryTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemdeliverylist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemDelivery itemdelivery = itemdeliverylist.get(rowIndex);

        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemdelivery.getDescription(),
            itemdelivery.getQuantity(),
            itemdelivery.getRemark()
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
    
    public List<ItemDelivery> getItemDeliveryList(){
        return itemdeliverylist;
    }
    
    public void setItemDeliveryList(List<ItemDelivery> itemdeliverylist){
        this.itemdeliverylist = itemdeliverylist;
        fireTableDataChanged();
    }
    
    public ItemDelivery getItemDelivery(int index){
        return itemdeliverylist.get(index);
    }
}
