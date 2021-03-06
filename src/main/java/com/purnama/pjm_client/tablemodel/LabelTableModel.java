/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Label;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class LabelTableModel extends AbstractTableModel{
    private List<Label> labellist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public LabelTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return labellist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Label label = labellist.get(rowIndex);
        
        Object[] values = new Object[]{
            label.getCode(), 
            label.getName(),
            label.isStatus()
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
    
    public List<Label> getLabelList(){
        return labellist;
    }
    
    public void setLabelList(List<Label> labellist){
        this.labellist = labellist;
        fireTableDataChanged();
    }
    
    public Label getLabel(int index){
        return labellist.get(index);
    }
}
