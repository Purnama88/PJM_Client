/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemReturnSales;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemReturnSalesTableModel extends AbstractTableModel{
    private List<ItemReturnSales> itemreturnsaleslist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PRICE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PERCENTAGE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DISCOUNT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_BOX"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemReturnSalesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemreturnsaleslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemReturnSales itemreturnsales = itemreturnsaleslist.get(rowIndex);

        boolean status = itemreturnsales.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemreturnsales.getDescription(),
            itemreturnsales.getFormattedQuantity(),
            itemreturnsales.getFormattedPrice(),
            itemreturnsales.getFormattedDiscount_percentage(),
            itemreturnsales.getFormattedDiscount(),
            itemreturnsales.getFormattedTotal(),
            itemreturnsales.getBox(),
            status
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
    
    public List<ItemReturnSales> getItemReturnSalesList(){
        return itemreturnsaleslist;
    }
    
    public void setItemReturnSalesList(List<ItemReturnSales> itemreturnsaleslist){
        this.itemreturnsaleslist = itemreturnsaleslist;
        fireTableDataChanged();
    }
    
    public ItemReturnSales getItemReturnSales(int index){
        return itemreturnsaleslist.get(index);
    }
}