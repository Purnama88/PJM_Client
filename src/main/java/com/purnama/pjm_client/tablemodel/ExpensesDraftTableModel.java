/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.ExpensesDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ExpensesDraftTableModel extends AbstractTableModel{
    private List<ExpensesDraft> expensesdraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ExpensesDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return expensesdraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpensesDraft expensesdraft = expensesdraftlist.get(rowIndex);
        
        String partner;
        
        if(expensesdraft.getPartner() != null){
            partner = expensesdraft.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            expensesdraft.getDraftid(),
            expensesdraft.getFormattedInvoicedate(),
            partner,
            expensesdraft.getFormattedTotal_after_tax()
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
    
    public List<ExpensesDraft> getExpensesDraftList(){
        return expensesdraftlist;
    }
    
    public void setExpensesDraftList(List<ExpensesDraft> expensesdraftlist){
        this.expensesdraftlist = expensesdraftlist;
        fireTableDataChanged();
    }
    
    public ExpensesDraft getExpensesDraft(int index){
        return expensesdraftlist.get(index);
    }
}
