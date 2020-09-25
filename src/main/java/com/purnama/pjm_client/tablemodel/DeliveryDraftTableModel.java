/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.draft.DeliveryDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class DeliveryDraftTableModel extends AbstractTableModel{
    private List<DeliveryDraft> deliverydraftlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_ID"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DESTINATION")
    };
    
    public DeliveryDraftTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return deliverydraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DeliveryDraft deliverydraft = deliverydraftlist.get(rowIndex);
        
        Object[] values = new Object[]{
            deliverydraft.getDraftid(),
            deliverydraft.getFormattedInvoicedate(),
            deliverydraft.getDestination()
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
    
    public List<DeliveryDraft> getDeliveryDraftList(){
        return deliverydraftlist;
    }
    
    public void setDeliveryDraftList(List<DeliveryDraft> deliverydraftlist){
        this.deliverydraftlist = deliverydraftlist;
        fireTableDataChanged();
    }
    
    public DeliveryDraft getDeliveryDraft(int index){
        return deliverydraftlist.get(index);
    }
}
