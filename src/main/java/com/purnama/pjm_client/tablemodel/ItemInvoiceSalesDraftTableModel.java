/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.model.transactional.draft.InvoiceSalesDraft;
import com.purnama.pjm_client.model.transactional.draft.ItemInvoiceSalesDraft;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemInvoiceSalesDraftTableModel extends AbstractTableModel{
    private final int invoiceid;
    
    private final DiscountSubtotalPanel discountsubtotalpanel;
    
    private List<ItemInvoiceSalesDraft> iteminvoicesalesdraftlist = new ArrayList<>();
    private final ArrayList<ItemInvoiceSalesDraft> deletediteminvoicesalesdraftlist;
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PRICE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PERCENTAGE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DISCOUNT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL")
    };
    
    public ItemInvoiceSalesDraftTableModel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        super();
        
        this.invoiceid = invoiceid;
        this.discountsubtotalpanel = discountsubtotalpanel;
        
        deletediteminvoicesalesdraftlist = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return iteminvoicesalesdraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemInvoiceSalesDraft iteminvoicesalesdraft = iteminvoicesalesdraftlist.get(rowIndex);

//        String description = "";
//        
//        if(iteminvoicesalesdraft.getItem() != null){
//            description = iteminvoicesalesdraft.getItem().getCode() + " - " + iteminvoicesalesdraft.getItem().getName();
//        }
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            iteminvoicesalesdraft.getDescription(),
            iteminvoicesalesdraft.getFormattedQuantity(),
            iteminvoicesalesdraft.getFormattedPrice(),
            iteminvoicesalesdraft.getFormattedDiscount_percentage(),
            iteminvoicesalesdraft.getFormattedDiscount(),
            iteminvoicesalesdraft.getFormattedTotal(),
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
        ItemInvoiceSalesDraft iisd = iteminvoicesalesdraftlist.get(row);
        
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
        
        if(row+1 == getRowCount()){
            iteminvoicesalesdraftlist.add(
                    createEmptyItemInvoiceSalesDraft(invoiceid));
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        if(!getItemInvoiceSalesDraftList().get(row).getDescription().isEmpty()){
            return col == 1 || col == 2 || col == 3 || col == 4 || col == 5 || col == 6;
        }
        else{
            return col == 1;
        }
    }
    
    public List<ItemInvoiceSalesDraft> getItemInvoiceSalesDraftList(){
        return iteminvoicesalesdraftlist;
    }
    
    public List<ItemInvoiceSalesDraft> getDeletedItemInvoiceSalesDraftList(){
        return deletediteminvoicesalesdraftlist;
    }
    
    public void setItemInvoiceSalesDraftList(List<ItemInvoiceSalesDraft> iteminvoicesalesdraftlist){
        this.iteminvoicesalesdraftlist = iteminvoicesalesdraftlist;
        fireTableDataChanged();
    }
    
    public ItemInvoiceSalesDraft getItemInvoiceSalesDraft(int index){
        return iteminvoicesalesdraftlist.get(index);
    }
    
    public void addRow(ItemInvoiceSalesDraft iteminvoicesalesdraft) {
        iteminvoicesalesdraftlist.add(iteminvoicesalesdraft);
    }
    
    public void deleteRow(int rownum){
          
        ItemInvoiceSalesDraft iisd = iteminvoicesalesdraftlist.get(rownum);
        
        deletediteminvoicesalesdraftlist.add(iisd);
        
        iteminvoicesalesdraftlist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
        discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
        discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
        
        if(getRowCount() == 0){
            addRow(createEmptyItemInvoiceSalesDraft(invoiceid));
        }
    }
    
    public ItemInvoiceSalesDraft createEmptyItemInvoiceSalesDraft(int invoiceid){
        InvoiceSalesDraft invoicesalesdraft = new InvoiceSalesDraft();
        invoicesalesdraft.setId(invoiceid);
        
        ItemInvoiceSalesDraft newiis = new ItemInvoiceSalesDraft();
        newiis.setDescription("");
        newiis.setQuantity(1);
        newiis.setPrice(0);
        newiis.setDiscount(0);
        newiis.setInvoicesalesdraft(invoicesalesdraft);
        
        return newiis;
    }
}
