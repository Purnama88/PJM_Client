/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Numbering;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class NumberingTableModel extends AbstractTableModel{
    private List<Numbering> numberinglist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PREFIX"),
        GlobalFields.PROPERTIES.getProperty("TABLE_START"),
        GlobalFields.PROPERTIES.getProperty("TABLE_END"),
        GlobalFields.PROPERTIES.getProperty("TABLE_CURRENT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public NumberingTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return numberinglist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Numbering numbering = numberinglist.get(rowIndex);

        Object[] values = new Object[]{
            numbering.getName(),
            numbering.getPrefix(),
            numbering.getFormattedStart(),
            numbering.getFormattedEnd(),
            numbering.getFormattedCurrent(),
            numbering.isStatus()
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
    
    public List<Numbering> getNumberingList(){
        return numberinglist;
    }
    
    public void setNumberingList(List<Numbering> numberinglist){
        this.numberinglist = numberinglist;
        fireTableDataChanged();
    }
    
    public Numbering getNumbering(int index){
        return numberinglist.get(index);
    }
}
