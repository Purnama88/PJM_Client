/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Role;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class RoleTableModel extends AbstractTableModel{
    private List<Role> rolelist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public RoleTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return rolelist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Role role = rolelist.get(rowIndex);
        
        Object[] values = new Object[]{
            role.getName(),
            role.isStatus()
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
    
    public List<Role> getRoleList(){
        return rolelist;
    }
    
    public void setRoleList(List<Role> rolelist){
        this.rolelist = rolelist;
        fireTableDataChanged();
    }
    
    public Role getRole(int index){
        return rolelist.get(index);
    }
}
