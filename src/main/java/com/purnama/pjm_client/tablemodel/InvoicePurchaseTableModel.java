/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.InvoicePurchase;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class InvoicePurchaseTableModel extends AbstractTableModel{
    private List<InvoicePurchase> invoicepurchaselist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public InvoicePurchaseTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return invoicepurchaselist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoicePurchase invoicepurchase = invoicepurchaselist.get(rowIndex);

        String partner;
        
        if(invoicepurchase.getPartner() != null){
            partner = invoicepurchase.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            invoicepurchase.getNumber(),
            invoicepurchase.getFormattedInvoicedate(),
            partner,
            invoicepurchase.getFormattedTotal_after_tax()
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
    
    public List<InvoicePurchase> getInvoicePurchaseList(){
        return invoicepurchaselist;
    }
    
    public void setInvoicePurchaseList(List<InvoicePurchase> invoicepurchaselist){
        this.invoicepurchaselist = invoicepurchaselist;
        fireTableDataChanged();
    }
    
    public InvoicePurchase getInvoicePurchase(int index){
        return invoicepurchaselist.get(index);
    }
}
