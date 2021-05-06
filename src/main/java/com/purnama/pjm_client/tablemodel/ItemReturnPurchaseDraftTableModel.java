/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.model.transactional.draft.ReturnPurchaseDraft;
import com.purnama.pjm_client.model.transactional.draft.ItemReturnPurchaseDraft;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemReturnPurchaseDraftTableModel extends AbstractTableModel{
    private final int invoiceid;
    
    private final DiscountSubtotalPanel discountsubtotalpanel;
    
    private List<ItemReturnPurchaseDraft> itemreturnpurchasedraftlist = new ArrayList<>();
    private final ArrayList<ItemReturnPurchaseDraft> deleteditemreturnpurchasedraftlist;
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PRICE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PERCENTAGE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DISCOUNT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_BOX"),
        GlobalFields.PROPERTIES.getProperty("TABLE_STATUS")
    };
    
    public ItemReturnPurchaseDraftTableModel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        super();
        
        this.invoiceid = invoiceid;
        this.discountsubtotalpanel = discountsubtotalpanel;
        
        deleteditemreturnpurchasedraftlist = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return itemreturnpurchasedraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemReturnPurchaseDraft itemreturnpurchasedraft = itemreturnpurchasedraftlist.get(rowIndex);
        
        boolean status = itemreturnpurchasedraft.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemreturnpurchasedraft.getDescription(),
            itemreturnpurchasedraft.getFormattedQuantity(),
            itemreturnpurchasedraft.getFormattedPrice(),
            itemreturnpurchasedraft.getFormattedDiscount_percentage(),
            itemreturnpurchasedraft.getFormattedDiscount(),
            itemreturnpurchasedraft.getFormattedTotal(),
            itemreturnpurchasedraft.getBox(),
            status
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
        ItemReturnPurchaseDraft iisd = itemreturnpurchasedraftlist.get(row);
        
        if(col == 1){
            String description = String.valueOf(value);
            iisd.setDescription(description);
            fireTableCellUpdated(row, 1);
        }
        else if(col == 2){
            discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
            iisd.setQuantity(GlobalFunctions.convertToDouble(value.toString()));
            fireTableCellUpdated(row, 6);
            discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() + iisd.getSubtotal());
        }
        else if(col == 3){
            discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
            iisd.setPrice(GlobalFunctions.convertToDouble(value.toString()));
            fireTableCellUpdated(row, 6);
            discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() + iisd.getSubtotal());
        }
        else if(col == 4){
            discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
            double percentage = GlobalFunctions.convertToDouble(value.toString());
            iisd.setDiscount(iisd.getSubtotal() * percentage / 100);
            
            fireTableCellUpdated(row, 5);
            fireTableCellUpdated(row, 6);
            discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() + iisd.getDiscount());
        }
        else if(col == 5){
            discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
            iisd.setDiscount(GlobalFunctions.convertToDouble(value.toString()));
            fireTableCellUpdated(row, 4);
            fireTableCellUpdated(row, 6);
            discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() + iisd.getDiscount());
        }
        else if(col == 7){
            String box = String.valueOf(value);
            iisd.setBox(box);
            fireTableCellUpdated(row, 7);
        }
        
        if(row+1 == getRowCount()){
            itemreturnpurchasedraftlist.add(
                    createEmptyItemReturnPurchaseDraft(invoiceid));
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        if(!itemreturnpurchasedraftlist.get(row).getDescription().isEmpty()){
            return col == 1 || col == 2 || col == 3 || col == 4 || col == 5 || col == 7;
        }
        else{
            return col == 1;
        }
    }
    
    public List<ItemReturnPurchaseDraft> getItemReturnPurchaseDraftList(){
        itemreturnpurchasedraftlist.remove(itemreturnpurchasedraftlist.size()-1);
        return itemreturnpurchasedraftlist;
    }
    
    public List<ItemReturnPurchaseDraft> getDeletedItemReturnPurchaseDraftList(){
        return deleteditemreturnpurchasedraftlist;
    }
    
    public void setItemReturnPurchaseDraftList(List<ItemReturnPurchaseDraft> itemreturnpurchasedraftlist){
        this.itemreturnpurchasedraftlist = itemreturnpurchasedraftlist;
        addRow(createEmptyItemReturnPurchaseDraft(invoiceid));
        fireTableDataChanged();
    }
    
    public ItemReturnPurchaseDraft getItemReturnPurchaseDraft(int index){
        return itemreturnpurchasedraftlist.get(index);
    }
    
    public void addRow(ItemReturnPurchaseDraft itemreturnpurchasedraft) {
        itemreturnpurchasedraftlist.add(itemreturnpurchasedraft);
    }
    
    public void deleteRow(int rownum){
          
        ItemReturnPurchaseDraft iisd = itemreturnpurchasedraftlist.get(rownum);
        
        deleteditemreturnpurchasedraftlist.add(iisd);
        
        itemreturnpurchasedraftlist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
        discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
        discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
        
        if(getRowCount() == 0){
            addRow(createEmptyItemReturnPurchaseDraft(invoiceid));
        }
    }
    
    public ItemReturnPurchaseDraft createEmptyItemReturnPurchaseDraft(int invoiceid){
        ReturnPurchaseDraft returnpurchasedraft = new ReturnPurchaseDraft();
        returnpurchasedraft.setId(invoiceid);
        
        ItemReturnPurchaseDraft newiis = new ItemReturnPurchaseDraft();
        newiis.setDescription("");
        newiis.setQuantity(1);
        newiis.setPrice(0);
        newiis.setDiscount(0);
        newiis.setReturnpurchasedraft(returnpurchasedraft);
        newiis.setBox("");
        
        return newiis;
    }
}
