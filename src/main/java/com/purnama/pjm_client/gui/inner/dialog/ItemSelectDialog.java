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
import com.purnama.pjm_client.gui.library.MyDialog;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ItemSelectDialog extends MyDialog{
    
    private final LabelTextFieldErrorPanel dbsearchpanel, tablesearchpanel;
    
    private final LabelComboBoxPanel labelcombobox;
    
    private final MyScrollPane scrollpane;
    
    private final TableRowSorter<TableModel> sorter;
    
    private final MyTable table;
    
    private final ItemTableModel itemtablemodel;
    
    private ArrayList<Item> list;
    
    public ItemSelectDialog() {
        super("", 1240, 550);
        
        dbsearchpanel = new LabelTextFieldErrorPanel("Search in Database", "");
        tablesearchpanel = new LabelTextFieldErrorPanel("Search in Table", "");
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        itemtablemodel = new ItemTableModel();
        
        labelcombobox = new LabelComboBoxPanel();
        
        sorter = new TableRowSorter<>(table.getModel());
        
        list = new ArrayList<>();
        
        init();
    }
    
    private void init(){
        
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
              load();
            }
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
              load();
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
              load();
            }
        };
        dbsearchpanel.setDocumentListener(documentListener);
        
        ActionListener actionlistener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
            
        };
        labelcombobox.setActionListener(actionlistener);
        
        scrollpane.getViewport().add(table);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box.add(dbsearchpanel);
        box.add(labelcombobox);
        box.add(tablesearchpanel);
        box.add(scrollpane);
        box.add(submitpanel);
        
        table.setModel(itemtablemodel);
        
        submitpanel.getCancelButton().addActionListener((ActionEvent e) -> {
            table.clearSelection();
            dispose();
        });
        
        submitpanel.getSubmitButton().addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        this.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
              table.clearSelection();
            }
        });
    }
    
    public String getSearchKeyword(){
        String text = dbsearchpanel.getTextFieldValue();
        String newtext = text.replaceAll(" ", "%20");
        return newtext;
    }
    
    private void load(){
        String keyword = getSearchKeyword();
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
                            list = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            Item.class));
                            itemtablemodel.setItemList(list);
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
    
    public Item showDialog(){
        setVisible(true);
        
        if(table.getSelectedRow() != -1){
        
            return itemtablemodel.getItem(table.
                        convertRowIndexToModel(table.getSelectedRow()));
        }
        
        return null;
    }

}
