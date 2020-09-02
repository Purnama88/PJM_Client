/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Warehouse;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class WarehouseTableModel extends AbstractTableModel{
    private List<Warehouse> warehouselist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public WarehouseTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return warehouselist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Warehouse warehouse = warehouselist.get(rowIndex);
        
        Object[] values = new Object[]{
            warehouse.getCode(), 
            warehouse.getName(),
            warehouse.isStatus()
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
    
    public List<Warehouse> getWarehouseList(){
        return warehouselist;
    }
    
    public void setWarehouseList(List<Warehouse> warehouselist){
        this.warehouselist = warehouselist;
        fireTableDataChanged();
    }
    
    public Warehouse getWarehouse(int index){
        return warehouselist.get(index);
    }
}
