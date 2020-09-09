/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.ReturnPurchaseDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ReturnPurchaseDraftTableModel extends AbstractTableModel{
    private List<ReturnPurchaseDraft> returnpurchasedraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ReturnPurchaseDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return returnpurchasedraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReturnPurchaseDraft returnpurchasedraft = returnpurchasedraftlist.get(rowIndex);

        String partner;
        
        if(returnpurchasedraft.getPartner() != null){
            partner = returnpurchasedraft.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            returnpurchasedraft.getDraftid(),
            returnpurchasedraft.getFormattedInvoicedate(),
            partner,
            returnpurchasedraft.getFormattedTotal_after_tax()
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
    
    public List<ReturnPurchaseDraft> getReturnPurchaseDraftList(){
        return returnpurchasedraftlist;
    }
    
    public void setReturnPurchaseDraftList(List<ReturnPurchaseDraft> returnpurchasedraftlist){
        this.returnpurchasedraftlist = returnpurchasedraftlist;
        fireTableDataChanged();
    }
    
    public ReturnPurchaseDraft getReturnPurchaseDraft(int index){
        return returnpurchasedraftlist.get(index);
    }
}
