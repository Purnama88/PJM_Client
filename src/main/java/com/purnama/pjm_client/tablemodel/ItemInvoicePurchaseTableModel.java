/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemInvoicePurchase;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemInvoicePurchaseTableModel extends AbstractTableModel{
    private List<ItemInvoicePurchase> iteminvoicepurchaselist = new ArrayList<>();
    
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
    
    public ItemInvoicePurchaseTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return iteminvoicepurchaselist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemInvoicePurchase iteminvoicepurchase = iteminvoicepurchaselist.get(rowIndex);

        boolean status = iteminvoicepurchase.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            iteminvoicepurchase.getDescription(),
            iteminvoicepurchase.getFormattedQuantity(),
            iteminvoicepurchase.getFormattedPrice(),
            iteminvoicepurchase.getFormattedDiscount_percentage(),
            iteminvoicepurchase.getFormattedDiscount(),
            iteminvoicepurchase.getFormattedTotal(),
            iteminvoicepurchase.getBox(),
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
    
    public List<ItemInvoicePurchase> getItemInvoicePurchaseList(){
        return iteminvoicepurchaselist;
    }
    
    public void setItemInvoicePurchaseList(List<ItemInvoicePurchase> iteminvoicepurchaselist){
        this.iteminvoicepurchaselist = iteminvoicepurchaselist;
        fireTableDataChanged();
    }
    
    public ItemInvoicePurchase getItemInvoicePurchase(int index){
        return iteminvoicepurchaselist.get(index);
    }
}