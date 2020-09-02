/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.InvoiceSalesDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class InvoiceSalesDraftTableModel extends AbstractTableModel{
    private List<InvoiceSalesDraft> invoicesalesdraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public InvoiceSalesDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return invoicesalesdraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceSalesDraft invoicesalesdraft = invoicesalesdraftlist.get(rowIndex);

        String partner;
        
        if(invoicesalesdraft.getPartner() != null){
            partner = invoicesalesdraft.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            invoicesalesdraft.getDraftid(),
            invoicesalesdraft.getFormattedInvoicedate(),
            partner,
            invoicesalesdraft.getFormattedTotal_after_tax()
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
    
    public List<InvoiceSalesDraft> getInvoiceSalesDraftList(){
        return invoicesalesdraftlist;
    }
    
    public void setInvoiceSalesDraftList(List<InvoiceSalesDraft> invoicesalesdraftlist){
        this.invoicesalesdraftlist = invoicesalesdraftlist;
        fireTableDataChanged();
    }
    
    public InvoiceSalesDraft getInvoiceSalesDraft(int index){
        return invoicesalesdraftlist.get(index);
    }
}
