/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.PartnerGroup;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class PartnerGroupTableModel extends AbstractTableModel{
    private List<PartnerGroup> partnergrouplist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public PartnerGroupTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return partnergrouplist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PartnerGroup partnergroup = partnergrouplist.get(rowIndex);
        
        Object[] values = new Object[]{
            partnergroup.getCode(), 
            partnergroup.getName(),
            partnergroup.isStatus()
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
    
    public List<PartnerGroup> getPartnerGroupList(){
        return partnergrouplist;
    }
    
    public void setPartnerGroupList(List<PartnerGroup> partnergrouplist){
        this.partnergrouplist = partnergrouplist;
        fireTableDataChanged();
    }
    
    public PartnerGroup getPartnerGroup(int index){
        return partnergrouplist.get(index);
    }
}
