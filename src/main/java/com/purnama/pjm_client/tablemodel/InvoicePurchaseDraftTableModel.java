/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.InvoicePurchaseDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class InvoicePurchaseDraftTableModel extends AbstractTableModel{
    private List<InvoicePurchaseDraft> invoicepurchasedraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public InvoicePurchaseDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return invoicepurchasedraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoicePurchaseDraft invoicepurchasedraft = invoicepurchasedraftlist.get(rowIndex);

        String partner;
        
        if(invoicepurchasedraft.getPartner() != null){
            partner = invoicepurchasedraft.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            invoicepurchasedraft.getDraftid(),
            invoicepurchasedraft.getFormattedInvoicedate(),
            partner,
            invoicepurchasedraft.getFormattedTotal_after_tax()
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
    
    public List<InvoicePurchaseDraft> getInvoicePurchaseDraftList(){
        return invoicepurchasedraftlist;
    }
    
    public void setInvoicePurchaseDraftList(List<InvoicePurchaseDraft> invoicepurchasedraftlist){
        this.invoicepurchasedraftlist = invoicepurchasedraftlist;
        fireTableDataChanged();
    }
    
    public InvoicePurchaseDraft getInvoicePurchaseDraft(int index){
        return invoicepurchasedraftlist.get(index);
    }
}
