/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.transactional.draft.DeliveryDraft;
import com.purnama.pjm_client.model.transactional.draft.ItemDeliveryDraft;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemDeliveryDraftTableModel extends AbstractTableModel{
    private final int deliverydraftid;
    
    private List<ItemDeliveryDraft> itemdeliverydraftlist = new ArrayList<>();
    private final ArrayList<ItemDeliveryDraft> deleteditemdeliverydraftlist;
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_REMARK"),
    };
    
    public ItemDeliveryDraftTableModel(int deliverydraftid){
        super();
        
        this.deliverydraftid = deliverydraftid;
        
        deleteditemdeliverydraftlist = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return itemdeliverydraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemDeliveryDraft itemdeliverydraft = itemdeliverydraftlist.get(rowIndex);
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemdeliverydraft.getDescription(),
            itemdeliverydraft.getQuantity(),
            itemdeliverydraft.getRemark()
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
    
    @Override
    public void setValueAt(Object value, int row, int col){
        ItemDeliveryDraft iisd = itemdeliverydraftlist.get(row);
        
        
        if(col == 1){
            String description = String.valueOf(value);
            iisd.setDescription(description);
            fireTableCellUpdated(row, 1);
        }
        else if(col == 2){
            String quantity = String.valueOf(value);
            iisd.setQuantity(quantity);
            fireTableCellUpdated(row, 2);
        }
        else if(col == 3){
            String remark = String.valueOf(value);
            iisd.setRemark(remark);
            fireTableCellUpdated(row, 3);
        }
        
        if(row+1 == getRowCount()){
            createEmptyItemDeliveryDraft();
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        if(!itemdeliverydraftlist.get(row).getDescription().isEmpty()){
            return col == 1 || col == 2 || col == 3;
        }
        else{
            return col == 1;
        }
    }
    
    public List<ItemDeliveryDraft> getItemDeliveryDraftList(){
        itemdeliverydraftlist.remove(itemdeliverydraftlist.size()-1);
        return itemdeliverydraftlist;
    }
    
    public List<ItemDeliveryDraft> getDeletedItemDeliveryDraftList(){
        return deleteditemdeliverydraftlist;
    }
    
    public void setItemDeliveryDraftList(List<ItemDeliveryDraft> itemdeliverydraftlist){
        this.itemdeliverydraftlist = itemdeliverydraftlist;
        createEmptyItemDeliveryDraft();
    }
    
    public ItemDeliveryDraft getItemDeliveryDraft(int index){
        return itemdeliverydraftlist.get(index);
    }
    
    public void deleteRow(int rownum){
          
        ItemDeliveryDraft iisd = itemdeliverydraftlist.get(rownum);
        
        deleteditemdeliverydraftlist.add(iisd);
        
        itemdeliverydraftlist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
        if(getRowCount() == 0){
            createEmptyItemDeliveryDraft();
        }
    }
    
    public void createEmptyItemDeliveryDraft(){
        DeliveryDraft deliverydraft = new DeliveryDraft();
        deliverydraft.setId(deliverydraftid);
        
        ItemDeliveryDraft newiis = new ItemDeliveryDraft();
        newiis.setDescription("");
        newiis.setQuantity("");
        newiis.setRemark("");
        newiis.setDeliverydraft(deliverydraft);
        
        itemdeliverydraftlist.add(newiis);
        fireTableDataChanged();
    }
    
    public void createItemDeliveryDraft(Item item, int quantity, String box){
        DeliveryDraft deliverydraft = new DeliveryDraft();
        deliverydraft.setId(deliverydraftid);
        
        ItemDeliveryDraft newiis = new ItemDeliveryDraft();
        newiis.setDescription(item.getCode() + " " + item.getName() + " " + item.getLabel().getCode());
        newiis.setQuantity(quantity+"");
        newiis.setDeliverydraft(deliverydraft);
        newiis.setRemark(box);
        
        itemdeliverydraftlist.add(itemdeliverydraftlist.size()-1, newiis);
        
        fireTableDataChanged();
    }
}
