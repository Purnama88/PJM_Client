/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.User;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class UserTableModel extends AbstractTableModel{

    private List<User> userlist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_ROLE"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public UserTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return userlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User u = userlist.get(rowIndex);
        
        Object[] values = new Object[]{
            u.getCode(),
            u.getName(),
            u.getRole().getName(),
            u.isStatus()
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
    
    public List<User> getUserList(){
        return userlist;
    }
    
    public void setUserList(List<User> userlist){
        this.userlist = userlist;
        fireTableDataChanged();
    }
    
    public User getUser(int index){
        return userlist.get(index);
    }
    
}
