/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.combine.PartnerPartnerGroup;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class PartnerPartnerGroupTableModel extends AbstractTableModel{
    private List<PartnerPartnerGroup> partnerpartnergrouplist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_BALANCE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public PartnerPartnerGroupTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return partnerpartnergrouplist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PartnerPartnerGroup partnerpartnergroup = partnerpartnergrouplist.get(rowIndex);
        
        Object[] values = new Object[]{
            partnerpartnergroup.getPartner().getCode(), 
            partnerpartnergroup.getPartner().getName(),
            partnerpartnergroup.getPartner().getBalance(),
            partnerpartnergroup.getPartner().isStatus()
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
    
    public List<PartnerPartnerGroup> getPartnerPartnerGroupList(){
        return partnerpartnergrouplist;
    }
    
    public void setPartnerPartnerGroupList(List<PartnerPartnerGroup> partnerpartnergrouplist){
        this.partnerpartnergrouplist = partnerpartnergrouplist;
        fireTableDataChanged();
    }
    
    public PartnerPartnerGroup getPartnerPartnerGroup(int index){
        return partnerpartnergrouplist.get(index);
    }
    
    public void addRow(PartnerPartnerGroup partnerpartnergroup) {
        partnerpartnergrouplist.add(partnerpartnergroup);
        fireTableDataChanged();
    }
    
    public void deleteRow(int rownum){
          
        partnerpartnergrouplist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
    }
}
