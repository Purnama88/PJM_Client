/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.model.transactional.draft.InvoicePurchaseDraft;
import com.purnama.pjm_client.model.transactional.draft.ItemInvoicePurchaseDraft;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemInvoicePurchaseDraftTableModel extends AbstractTableModel{
    private final int invoiceid;
    
    private final DiscountSubtotalPanel discountsubtotalpanel;
    
    private List<ItemInvoicePurchaseDraft> iteminvoicepurchasedraftlist = new ArrayList<>();
    private final ArrayList<ItemInvoicePurchaseDraft> deletediteminvoicepurchasedraftlist;
    
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
    
    public ItemInvoicePurchaseDraftTableModel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        super();
        
        this.invoiceid = invoiceid;
        this.discountsubtotalpanel = discountsubtotalpanel;
        
        deletediteminvoicepurchasedraftlist = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return iteminvoicepurchasedraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemInvoicePurchaseDraft iteminvoicepurchasedraft = iteminvoicepurchasedraftlist.get(rowIndex);
        
        boolean status = iteminvoicepurchasedraft.getItem() != null;
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            iteminvoicepurchasedraft.getDescription(),
            iteminvoicepurchasedraft.getFormattedQuantity(),
            iteminvoicepurchasedraft.getFormattedPrice(),
            iteminvoicepurchasedraft.getFormattedDiscount_percentage(),
            iteminvoicepurchasedraft.getFormattedDiscount(),
            iteminvoicepurchasedraft.getFormattedTotal(),
            iteminvoicepurchasedraft.getBox(),
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
        ItemInvoicePurchaseDraft iisd = iteminvoicepurchasedraftlist.get(row);
        
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
            iteminvoicepurchasedraftlist.add(
                    createEmptyItemInvoicePurchaseDraft(invoiceid));
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        if(!iteminvoicepurchasedraftlist.get(row).getDescription().isEmpty()){
            return col == 1 || col == 2 || col == 3 || col == 4 || col == 5 || col == 7;
        }
        else{
            return col == 1;
        }
    }
    
    public List<ItemInvoicePurchaseDraft> getItemInvoicePurchaseDraftList(){
        iteminvoicepurchasedraftlist.remove(iteminvoicepurchasedraftlist.size()-1);
        return iteminvoicepurchasedraftlist;
    }
    
    public List<ItemInvoicePurchaseDraft> getDeletedItemInvoicePurchaseDraftList(){
        return deletediteminvoicepurchasedraftlist;
    }
    
    public void setItemInvoicePurchaseDraftList(List<ItemInvoicePurchaseDraft> iteminvoicepurchasedraftlist){
        this.iteminvoicepurchasedraftlist = iteminvoicepurchasedraftlist;
        addRow(createEmptyItemInvoicePurchaseDraft(invoiceid));
        fireTableDataChanged();
    }
    
    public ItemInvoicePurchaseDraft getItemInvoicePurchaseDraft(int index){
        return iteminvoicepurchasedraftlist.get(index);
    }
    
    public void addRow(ItemInvoicePurchaseDraft iteminvoicepurchasedraft) {
        iteminvoicepurchasedraftlist.add(iteminvoicepurchasedraft);
    }
    
    public void deleteRow(int rownum){
          
        ItemInvoicePurchaseDraft iisd = iteminvoicepurchasedraftlist.get(rownum);
        
        deletediteminvoicepurchasedraftlist.add(iisd);
        
        iteminvoicepurchasedraftlist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
        discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
        discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
        
        if(getRowCount() == 0){
            addRow(createEmptyItemInvoicePurchaseDraft(invoiceid));
        }
    }
    
    public ItemInvoicePurchaseDraft createEmptyItemInvoicePurchaseDraft(int invoiceid){
        InvoicePurchaseDraft invoicepurchasedraft = new InvoicePurchaseDraft();
        invoicepurchasedraft.setId(invoiceid);
        
        ItemInvoicePurchaseDraft newiis = new ItemInvoicePurchaseDraft();
        newiis.setDescription("");
        newiis.setQuantity(1);
        newiis.setPrice(0);
        newiis.setDiscount(0);
        newiis.setInvoicepurchasedraft(invoicepurchasedraft);
        newiis.setBox("");
        
        return newiis;
    }
}
