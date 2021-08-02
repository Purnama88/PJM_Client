/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemInvoiceSales;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemInvoiceSalesTableModel extends AbstractTableModel{
    private List<ItemInvoiceSales> iteminvoicesaleslist = new ArrayList<>();
    
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
    
    public ItemInvoiceSalesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return iteminvoicesaleslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemInvoiceSales iteminvoicesales = iteminvoicesaleslist.get(rowIndex);

        boolean status = iteminvoicesales.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            iteminvoicesales.getDescription(),
            iteminvoicesales.getFormattedQuantity(),
            iteminvoicesales.getFormattedPrice(),
            iteminvoicesales.getFormattedDiscount_percentage(),
            iteminvoicesales.getFormattedDiscount(),
            iteminvoicesales.getFormattedTotal(),
            iteminvoicesales.getBox(),
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
    
    public List<ItemInvoiceSales> getItemInvoiceSalesList(){
        return iteminvoicesaleslist;
    }
    
    public void setItemInvoiceSalesList(List<ItemInvoiceSales> iteminvoicesaleslist){
        this.iteminvoicesaleslist = iteminvoicesaleslist;
        fireTableDataChanged();
    }
    
    public ItemInvoiceSales getItemInvoiceSales(int index){
        return iteminvoicesaleslist.get(index);
    }
}