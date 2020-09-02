/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Brand;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class BrandTableModel extends AbstractTableModel{
    private List<Brand> brandlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public BrandTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return brandlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Brand brand = brandlist.get(rowIndex);

        Object[] values = new Object[]{
            brand.getCode(),
            brand.getName(),
            brand.isStatus()
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
    
    public List<Brand> getBrandList(){
        return brandlist;
    }
    
    public void setBrandList(List<Brand> brandlist){
        this.brandlist = brandlist;
        fireTableDataChanged();
    }
    
    public Brand getBrand(int index){
        return brandlist.get(index);
    }
}
