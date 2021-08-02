/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ItemExpenses;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemExpensesTableModel extends AbstractTableModel{
    private List<ItemExpenses> itemexpenseslist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PRICE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PERCENTAGE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DISCOUNT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_BOX")
    };
    
    public ItemExpensesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemexpenseslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemExpenses itemexpenses = itemexpenseslist.get(rowIndex);

        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemexpenses.getDescription(),
            itemexpenses.getFormattedQuantity(),
            itemexpenses.getFormattedPrice(),
            itemexpenses.getFormattedDiscount_percentage(),
            itemexpenses.getFormattedDiscount(),
            itemexpenses.getFormattedTotal(),
            itemexpenses.getBox()
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
    
    public List<ItemExpenses> getItemExpensesList(){
        return itemexpenseslist;
    }
    
    public void setItemExpensesList(List<ItemExpenses> itemexpenseslist){
        this.itemexpenseslist = itemexpenseslist;
        fireTableDataChanged();
    }
    
    public ItemExpenses getItemExpenses(int index){
        return itemexpenseslist.get(index);
    }
}