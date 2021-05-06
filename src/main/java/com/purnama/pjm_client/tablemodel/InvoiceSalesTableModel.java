/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.InvoiceSales;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class InvoiceSalesTableModel extends AbstractTableModel{
    private List<InvoiceSales> invoicesaleslist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public InvoiceSalesTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return invoicesaleslist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceSales invoicesales = invoicesaleslist.get(rowIndex);

        String partner;
        
        if(invoicesales.getPartner() != null){
            partner = invoicesales.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            invoicesales.getNumber(),
            invoicesales.getFormattedInvoicedate(),
            partner,
            invoicesales.getFormattedTotal_after_tax()
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
    
    public List<InvoiceSales> getInvoiceSalesList(){
        return invoicesaleslist;
    }
    
    public void setInvoiceSalesList(List<InvoiceSales> invoicesaleslist){
        this.invoicesaleslist = invoicesaleslist;
        fireTableDataChanged();
    }
    
    public InvoiceSales getInvoiceSales(int index){
        return invoicesaleslist.get(index);
    }
}
