/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ReturnSales;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ReturnSalesTableModel extends AbstractTableModel{
    private List<ReturnSales> returnsaleslist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ReturnSalesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return returnsaleslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReturnSales returnsales = returnsaleslist.get(rowIndex);

        String partner;
        
        if(returnsales.getPartner() != null){
            partner = returnsales.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            returnsales.getNumber(),
            returnsales.getFormattedInvoicedate(),
            partner,
            returnsales.getFormattedTotal_after_tax()
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
    
    public List<ReturnSales> getReturnSalesList(){
        return returnsaleslist;
    }
    
    public void setReturnSalesList(List<ReturnSales> returnsaleslist){
        this.returnsaleslist = returnsaleslist;
        fireTableDataChanged();
    }
    
    public ReturnSales getReturnSales(int index){
        return returnsaleslist.get(index);
    }
}
