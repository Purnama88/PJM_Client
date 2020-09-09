/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.ReturnSalesDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ReturnSalesDraftTableModel extends AbstractTableModel{
    private List<ReturnSalesDraft> returnsalesdraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ReturnSalesDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return returnsalesdraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReturnSalesDraft returnsalesdraft = returnsalesdraftlist.get(rowIndex);

        String partner;
        
        if(returnsalesdraft.getPartner() != null){
            partner = returnsalesdraft.getPartner().getName();
        }
        else{
            partner = "";
        }
        
        Object[] values = new Object[]{
            returnsalesdraft.getDraftid(),
            returnsalesdraft.getFormattedInvoicedate(),
            partner,
            returnsalesdraft.getFormattedTotal_after_tax()
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
    
    public List<ReturnSalesDraft> getReturnSalesDraftList(){
        return returnsalesdraftlist;
    }
    
    public void setReturnSalesDraftList(List<ReturnSalesDraft> returnsalesdraftlist){
        this.returnsalesdraftlist = returnsalesdraftlist;
        fireTableDataChanged();
    }
    
    public ReturnSalesDraft getReturnSalesDraft(int index){
        return returnsalesdraftlist.get(index);
    }
}
