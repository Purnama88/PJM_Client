/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.ReturnPurchase;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ReturnPurchaseTableModel extends AbstractTableModel{
    private List<ReturnPurchase> returnpurchaselist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ReturnPurchaseTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return returnpurchaselist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReturnPurchase returnpurchase = returnpurchaselist.get(rowIndex);

        String partner;
        
        if(returnpurchase.getPartner() != null){
            partner = returnpurchase.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            returnpurchase.getNumber(),
            returnpurchase.getFormattedInvoicedate(),
            partner,
            returnpurchase.getFormattedTotal_after_tax()
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
    
    public List<ReturnPurchase> getReturnPurchaseList(){
        return returnpurchaselist;
    }
    
    public void setReturnPurchaseList(List<ReturnPurchase> returnpurchaselist){
        this.returnpurchaselist = returnpurchaselist;
        fireTableDataChanged();
    }
    
    public ReturnPurchase getReturnPurchase(int index){
        return returnpurchaselist.get(index);
    }
}
