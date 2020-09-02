/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class PartnerTableModel extends AbstractTableModel{
    private List<Partner> partnerlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_BALANCE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public PartnerTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return partnerlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Partner partner = partnerlist.get(rowIndex);
        
        Object[] values = new Object[]{
            partner.getCode(), 
            partner.getName(),
            partner.getBalance(),
            partner.isStatus()
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
    
    public List<Partner> getPartnerList(){
        return partnerlist;
    }
    
    public void setPartnerList(List<Partner> partnerlist){
        this.partnerlist = partnerlist;
        fireTableDataChanged();
    }
    
    public Partner getPartner(int index){
        return partnerlist.get(index);
    }
}
