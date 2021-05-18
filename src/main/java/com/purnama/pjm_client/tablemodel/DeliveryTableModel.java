/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.transactional.Delivery;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class DeliveryTableModel extends AbstractTableModel{
    private List<Delivery> deliverylist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NUMBER"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DATE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PARTNER")
    };
    
    public DeliveryTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return deliverylist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Delivery delivery = deliverylist.get(rowIndex);
        
        Object[] values = new Object[]{
            delivery.getNumber(),
            delivery.getFormattedInvoicedate(),
            delivery.getDestination()
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
    
    public List<Delivery> getDeliveryList(){
        return deliverylist;
    }
    
    public void setDeliveryList(List<Delivery> deliverylist){
        this.deliverylist = deliverylist;
        fireTableDataChanged();
    }
    
    public Delivery getDelivery(int index){
        return deliverylist.get(index);
    }
}
