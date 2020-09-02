/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class CurrencyTableModel extends AbstractTableModel{
    private List<Currency> currencylist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DEFAULT"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public CurrencyTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return currencylist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Currency currency = currencylist.get(rowIndex);
        
        Object[] values = new Object[]{
            currency.getCode(), 
            currency.getName(),
            currency.isDefaultcurrency(),
            currency.isStatus()
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
    
    public List<Currency> getCurrencyList(){
        return currencylist;
    }
    
    public void setCurrencyList(List<Currency> currencylist){
        this.currencylist = currencylist;
        fireTableDataChanged();
    }
    
    public Currency getCurrency(int index){
        return currencylist.get(index);
    }
}
