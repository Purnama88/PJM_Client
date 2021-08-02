/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemReturnPurchase;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemReturnPurchaseTableModel extends AbstractTableModel{
    private List<ItemReturnPurchase> itemreturnpurchaselist = new ArrayList<>();
    
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
    
    public ItemReturnPurchaseTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemreturnpurchaselist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemReturnPurchase itemreturnpurchase = itemreturnpurchaselist.get(rowIndex);

        boolean status = itemreturnpurchase.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemreturnpurchase.getDescription(),
            itemreturnpurchase.getFormattedQuantity(),
            itemreturnpurchase.getFormattedPrice(),
            itemreturnpurchase.getFormattedDiscount_percentage(),
            itemreturnpurchase.getFormattedDiscount(),
            itemreturnpurchase.getFormattedTotal(),
            itemreturnpurchase.getBox(),
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
    
    public List<ItemReturnPurchase> getItemReturnPurchaseList(){
        return itemreturnpurchaselist;
    }
    
    public void setItemReturnPurchaseList(List<ItemReturnPurchase> itemreturnpurchaselist){
        this.itemreturnpurchaselist = itemreturnpurchaselist;
        fireTableDataChanged();
    }
    
    public ItemReturnPurchase getItemReturnPurchase(int index){
        return itemreturnpurchaselist.get(index);
    }
}