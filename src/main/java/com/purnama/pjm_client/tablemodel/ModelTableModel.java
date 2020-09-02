/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Model;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ModelTableModel extends AbstractTableModel{
    private List<Model> modellist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_BRAND"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ModelTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return modellist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Model model = modellist.get(rowIndex);
        
        Object[] values = new Object[]{
            model.getCode(),
            model.getName(),
            model.getBrand().getName(),
            model.isStatus()
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
    
    public List<Model> getModelList(){
        return modellist;
    }
    
    public void setModelList(List<Model> modellist){
        this.modellist = modellist;
        fireTableDataChanged();
    }
    
    public Model getModel(int index){
        return modellist.get(index);
    }
}
