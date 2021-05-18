/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.combine.ItemModel;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemModelTableModel extends AbstractTableModel{
    private List<ItemModel> itemmodellist = new ArrayList<>();
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_CODE"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_NAME"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_BRAND"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemModelTableModel(){
        super();
    }

    @Override
    public int getRowCount() {
        return itemmodellist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemModel itemmodel = itemmodellist.get(rowIndex);
        
        Object[] values = new Object[]{
            itemmodel.getModel().getCode(),
            itemmodel.getModel().getName(),
            itemmodel.getModel().getBrand().getName(),
            itemmodel.getModel().isStatus()
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
    
    public List<ItemModel> getItemModelList(){
        return itemmodellist;
    }
    
    public void setItemModelList(List<ItemModel> itemmodellist){
        this.itemmodellist = itemmodellist;
        fireTableDataChanged();
    }
    
    public ItemModel getItemModel(int index){
        return itemmodellist.get(index);
    }
    
    public void addRow(ItemModel itemmodel) {
        itemmodellist.add(itemmodel);
        fireTableDataChanged();
    }
    
    public void deleteRow(int rownum){
          
        itemmodellist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
    }
}
