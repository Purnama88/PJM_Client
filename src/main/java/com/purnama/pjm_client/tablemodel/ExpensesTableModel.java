/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.Expenses;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ExpensesTableModel extends AbstractTableModel{
    private List<Expenses> expenseslist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ExpensesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return expenseslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Expenses expenses = expenseslist.get(rowIndex);

        String partner;
        
        if(expenses.getPartner() != null){
            partner = expenses.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            expenses.getNumber(),
            expenses.getFormattedInvoicedate(),
            partner,
            expenses.getFormattedTotal_after_tax()
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
    
    public List<Expenses> getExpensesList(){
        return expenseslist;
    }
    
    public void setExpensesList(List<Expenses> expenseslist){
        this.expenseslist = expenseslist;
        fireTableDataChanged();
    }
    
    public Expenses getExpenses(int index){
        return expenseslist.get(index);
    }
}
