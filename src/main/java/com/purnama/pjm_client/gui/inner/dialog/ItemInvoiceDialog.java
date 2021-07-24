/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.dialog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.form.util.LabelComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.library.MyDialog;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTabbedPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemDeliveryDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemExpensesDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemGroupTableModel;
import com.purnama.pjm_client.tablemodel.ItemInvoicePurchaseDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemInvoiceSalesDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemReturnPurchaseDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemReturnSalesDraftTableModel;
import com.purnama.pjm_client.tablemodel.ItemTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ItemInvoiceDialog extends MyDialog{
    
    private final LabelTextFieldErrorPanel itemdbsearchpanel, itemtablesearchpanel,
            itemgroupdbsearchpanel, itemgrouptablesearchpanel;
    
    private final MyScrollPane itemscrollpane, itemgroupscrollpane;
    
    private final TableRowSorter<TableModel> itemsorter, itemgroupsorter;
    
    private final MyTable itemtable, itemgrouptable;
    
    private final MyTabbedPane tabbedpane;
    
    private final LabelComboBoxPanel labelcombobox;
    
    private final MyPanel itempanel, itemgrouppanel;
    
    private TableModel tablemodel;
    
    private final ItemTableModel itemtablemodel;
    private final ItemGroupTableModel itemgrouptablemodel;
    
    private ArrayList<Item> itemlist;
    private ArrayList<ItemGroup> itemgrouplist;
    
    private final SubmitPanel itemgroupsubmitpanel;
    
    public ItemInvoiceDialog() {
        super("", GlobalFields.DIALOG_WIDTH, GlobalFields.DIALOG_HEIGHT);
        
        itemdbsearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHDATABASE"), "");
        itemtablesearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHTABLE"), "");
        
        itemgroupdbsearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHDATABASE"), "");
        itemgrouptablesearchpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_SEARCHTABLE"), "");
        
        itemtable = new MyTable();
        itemgrouptable = new MyTable();
        
        itemscrollpane = new MyScrollPane();
        itemgroupscrollpane = new MyScrollPane();
        
        itemsorter = new TableRowSorter<>(itemtable.getModel());
        itemgroupsorter = new TableRowSorter<>(itemgrouptable.getModel());
        
        labelcombobox = new LabelComboBoxPanel();
        
        itemtablemodel = new ItemTableModel();
        itemgrouptablemodel = new ItemGroupTableModel();
        
        itempanel = new MyPanel();
        itemgrouppanel = new MyPanel();
        
        tabbedpane = new MyTabbedPane();
        
        itemgroupsubmitpanel = new SubmitPanel();
        
        init();
    }
    
    private void init(){
        
        submitpanel.addSubmitAndAddMorebutton();
        
        itemgroupsubmitpanel.addCancelButton();
        itemgroupsubmitpanel.addSubmitAndAddMorebutton();
        
        itempanel.setLayout(new BoxLayout(itempanel, BoxLayout.Y_AXIS));
        itemgrouppanel.setLayout(new BoxLayout(itemgrouppanel, BoxLayout.Y_AXIS));
        
        tabbedpane.add(GlobalFields.PROPERTIES.getProperty("LABEL_ITEM"), itempanel);
        tabbedpane.add(GlobalFields.PROPERTIES.getProperty("LABEL_ITEMGROUP"), itemgrouppanel);
        
        itemscrollpane.getViewport().add(itemtable);
        itemscrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        itemgroupscrollpane.getViewport().add(itemgrouptable);
        itemgroupscrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        itemtable.setModel(itemtablemodel);
        itemgrouptable.setModel(itemgrouptablemodel);
        
        itempanel.add(itemdbsearchpanel);
        itempanel.add(labelcombobox);
        itempanel.add(itemtablesearchpanel);
        itempanel.add(itemscrollpane);
        itempanel.add(submitpanel);
        
        itemgrouppanel.add(itemgroupdbsearchpanel);
        itemgrouppanel.add(itemgrouptablesearchpanel);
        itemgrouppanel.add(itemgroupscrollpane);
        itemgrouppanel.add(itemgroupsubmitpanel);
        
        box.add(tabbedpane);
        
        itemgroupsubmitpanel.getCancelButton().addActionListener( e -> {
           dispose();
        });
        
        itemgroupsubmitpanel.getSubmitButton().addActionListener( e -> {
           dispose();
        });
        
        submitpanel.getSubmitandaddmorebutton().addActionListener( e -> {
            
            if(itemtable.getSelectedRow() != -1){
        
                Item item = itemtablemodel.getItem(itemtable.
                        convertRowIndexToModel(itemtable.getSelectedRow()));
        
                if(tablemodel instanceof ItemInvoiceSalesDraftTableModel){
                    ItemInvoiceSalesDraftTableModel iteminvoicesalesdrafttablemodel = (ItemInvoiceSalesDraftTableModel)tablemodel;
                    iteminvoicesalesdrafttablemodel.createItemInvoiceSalesDraft(item, 1, "1-3");
                }
                else if(tablemodel instanceof ItemInvoicePurchaseDraftTableModel){
                    ItemInvoicePurchaseDraftTableModel iteminvoicepurchasedrafttablemodel = (ItemInvoicePurchaseDraftTableModel)tablemodel;
                    iteminvoicepurchasedrafttablemodel.createItemInvoicePurchaseDraft(item, 1, "1-3");
                }
                else if(tablemodel instanceof ItemReturnSalesDraftTableModel){
                    ItemReturnSalesDraftTableModel itemreturnsalesdrafttablemodel = (ItemReturnSalesDraftTableModel)tablemodel;
                    itemreturnsalesdrafttablemodel.createItemReturnSalesDraft(item, 1, "1-3");
                }
                else if(tablemodel instanceof ItemReturnPurchaseDraftTableModel){
                    ItemReturnPurchaseDraftTableModel itemreturnpurchasedrafttablemodel = (ItemReturnPurchaseDraftTableModel)tablemodel;
                    itemreturnpurchasedrafttablemodel.createItemReturnPurchaseDraft(item, 1, "1-3");
                }
                else if(tablemodel instanceof ItemDeliveryDraftTableModel){
                    ItemDeliveryDraftTableModel itemdeliverydrafttablemodel = (ItemDeliveryDraftTableModel)tablemodel;
                    itemdeliverydrafttablemodel.createItemDeliveryDraft(item, 1, "1-3");
                }
                else if(tablemodel instanceof ItemExpensesDraftTableModel){
                    ItemExpensesDraftTableModel itemexpensesdrafttablemodel = (ItemExpensesDraftTableModel)tablemodel;
                    itemexpensesdrafttablemodel.createItemExpensesDraft(item, 1, "1-3");
                }
            }
        });
        
        ActionListener actionlistener = e -> {
            loaditem();
        };
        labelcombobox.setActionListener(actionlistener);
        
        DocumentListener itemdocumentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
              loaditem();
            }
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
              loaditem();
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
              loaditem();
            }
        };
        itemdbsearchpanel.setDocumentListener(itemdocumentListener);
        
        DocumentListener itemgroupdocumentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
              loaditemgroup();
            }
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
              loaditemgroup();
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
              loaditemgroup();
            }
        };
        itemgroupdbsearchpanel.setDocumentListener(itemgroupdocumentListener);
    }

    public void setTablemodel(TableModel tablemodel) {
        this.tablemodel = tablemodel;
    }
    
    public Item showDialog(){
        setVisible(true);
        
        return null;
    }
    
    private void loaditem(){
        String keyword = getItemSearchKeyword();
        int labelid = labelcombobox.getComboBoxValue().getId();
        
        if(keyword.length() >= GlobalFields.MINIMAL_CHARACTER_ON_SEARCH){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

                ClientResponse response;

                @Override
                protected Boolean doInBackground(){

                    response = RestClient.get("items?labelid=" + labelid +
                            "&keyword=" + keyword);

                    return true;
                }

                @Override
                protected void process(List<String> chunks) {

                }

                @Override
                protected void done() {
                    if(response == null){
                    }
                    else if(response.getStatus() != 200) {
                    }
                    else{
                        String output = response.getEntity(String.class);

                        ObjectMapper mapper = new ObjectMapper();


                        try{
                            itemlist = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            Item.class));
                            itemtablemodel.setItemList(itemlist);
                        }
                        catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void loaditemgroup(){
        String keyword = getItemGroupSearchKeyword();
        
        if(keyword.length() >= GlobalFields.MINIMAL_CHARACTER_ON_SEARCH){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

                ClientResponse response;

                @Override
                protected Boolean doInBackground(){

                    response = RestClient.get("itemgroups?keyword=" + keyword);

                    return true;
                }

                @Override
                protected void process(List<String> chunks) {

                }

                @Override
                protected void done() {
                    if(response == null){
                    }
                    else if(response.getStatus() != 200) {
                    }
                    else{
                        String output = response.getEntity(String.class);

                        ObjectMapper mapper = new ObjectMapper();

                        try{
                            itemgrouplist = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            ItemGroup.class));
                            itemgrouptablemodel.setItemGroupList(itemgrouplist);
                        }
                        catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
            };
            worker.execute();
        }
    }
    
    protected String getItemSearchKeyword(){
        String text = itemdbsearchpanel.getTextFieldValue();
        String newtext = text.replaceAll(" ", "%20");
        return newtext;
    }
    
    protected String getItemGroupSearchKeyword(){
        String text = itemgroupdbsearchpanel.getTextFieldValue();
        String newtext = text.replaceAll(" ", "%20");
        return newtext;
    }
}
