/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.tablemodel;

import com.purnama.pjm_client.gui.inner.detail.util.DiscountSubtotalPanel;
import com.purnama.pjm_client.model.transactional.draft.ReturnSalesDraft;
import com.purnama.pjm_client.model.transactional.draft.ItemReturnSalesDraft;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author p_cor
 */
public class ItemReturnSalesDraftTableModel extends AbstractTableModel{
    private final int invoiceid;
    
    private final DiscountSubtotalPanel discountsubtotalpanel;
    
    private List<ItemReturnSalesDraft> itemreturnsalesdraftlist = new ArrayList<>();
    private final ArrayList<ItemReturnSalesDraft> deleteditemreturnsalesdraftlist;
    
    String[] columnNames = new String[]{
        GlobalFields.PROPERTIES.getProperty("TABLE_NO"), 
        GlobalFields.PROPERTIES.getProperty("TABLE_DESCRIPTION"),
        GlobalFields.PROPERTIES.getProperty("TABLE_QUANTITY"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PRICE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_PERCENTAGE"),
        GlobalFields.PROPERTIES.getProperty("TABLE_DISCOUNT"),
        GlobalFields.PROPERTIES.getProperty("TABLE_TOTAL"),
        GlobalFields.PROPERTIES.getProperty("TABLE_BOX"),
        GlobalFields.PROPERTIES.getProperty("TABLE_ACTION")
    };
    
    public ItemReturnSalesDraftTableModel(int invoiceid, DiscountSubtotalPanel discountsubtotalpanel){
        super();
        
        this.invoiceid = invoiceid;
        this.discountsubtotalpanel = discountsubtotalpanel;
        
        deleteditemreturnsalesdraftlist = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return itemreturnsalesdraftlist.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemReturnSalesDraft itemreturnsalesdraft = itemreturnsalesdraftlist.get(rowIndex);
        
        Object[] values = new Object[]{
            String.valueOf(rowIndex + 1), 
            itemreturnsalesdraft.getDescription(),
            itemreturnsalesdraft.getFormattedQuantity(),
            itemreturnsalesdraft.getFormattedPrice(),
            itemreturnsalesdraft.getFormattedDiscount_percentage(),
            itemreturnsalesdraft.getFormattedDiscount(),
            itemreturnsalesdraft.getFormattedTotal(),
            itemreturnsalesdraft.getBox(),
            ""
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
        ItemReturnSalesDraft iisd = itemreturnsalesdraftlist.get(row);
        
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
            itemreturnsalesdraftlist.add(
                    createEmptyItemReturnSalesDraft(invoiceid));
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col){
        if(!itemreturnsalesdraftlist.get(row).getDescription().isEmpty()){
            return col == 1 || col == 2 || col == 3 || col == 4 || col == 5 || col == 7;
        }
        else{
            return col == 1;
        }
    }
    
    public List<ItemReturnSalesDraft> getItemReturnSalesDraftList(){
        itemreturnsalesdraftlist.remove(itemreturnsalesdraftlist.size()-1);
        return itemreturnsalesdraftlist;
    }
    
    public List<ItemReturnSalesDraft> getDeletedItemReturnSalesDraftList(){
        return deleteditemreturnsalesdraftlist;
    }
    
    public void setItemReturnSalesDraftList(List<ItemReturnSalesDraft> itemreturnsalesdraftlist){
        this.itemreturnsalesdraftlist = itemreturnsalesdraftlist;
        addRow(createEmptyItemReturnSalesDraft(invoiceid));
        fireTableDataChanged();
    }
    
    public ItemReturnSalesDraft getItemReturnSalesDraft(int index){
        return itemreturnsalesdraftlist.get(index);
    }
    
    public void addRow(ItemReturnSalesDraft itemreturnsalesdraft) {
        itemreturnsalesdraftlist.add(itemreturnsalesdraft);
    }
    
    public void deleteRow(int rownum){
          
        ItemReturnSalesDraft iisd = itemreturnsalesdraftlist.get(rownum);
        
        deleteditemreturnsalesdraftlist.add(iisd);
        
        itemreturnsalesdraftlist.remove(rownum);
        fireTableRowsDeleted(rownum, rownum);
        
        discountsubtotalpanel.setSubtotal(discountsubtotalpanel.getSubtotal() - iisd.getSubtotal());
        discountsubtotalpanel.setDiscount(discountsubtotalpanel.getDiscount() - iisd.getDiscount());
        
        if(getRowCount() == 0){
            addRow(createEmptyItemReturnSalesDraft(invoiceid));
        }
    }
    
    public ItemReturnSalesDraft createEmptyItemReturnSalesDraft(int invoiceid){
        ReturnSalesDraft returnsalesdraft = new ReturnSalesDraft();
        returnsalesdraft.setId(invoiceid);
        
        ItemReturnSalesDraft newiis = new ItemReturnSalesDraft();
        newiis.setDescription("");
        newiis.setQuantity(1);
        newiis.setPrice(0);
        newiis.setDiscount(0);
        newiis.setReturnsalesdraft(returnsalesdraft);
        newiis.setBox("");
        
        return newiis;
    }
}
