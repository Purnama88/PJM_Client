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
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.PartnerTableModel;
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
public class PartnerSelectDialog extends MyDialog{
    
    private final LabelTextFieldErrorPanel dbsearchpanel, tablesearchpanel;
    
    private final MyScrollPane scrollpane;
    
    private final TableRowSorter<TableModel> sorter;
    
    private final MyTable table;
    
    private final PartnerTableModel partnertablemodel;
    
    private ArrayList<Partner> list;
    
    public PartnerSelectDialog() {
        super("", 1240, 550);
        
        dbsearchpanel = new LabelTextFieldErrorPanel("Search in Database", "");
        tablesearchpanel = new LabelTextFieldErrorPanel("Search in Table", "");
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        partnertablemodel = new PartnerTableModel();
        
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
        
        scrollpane.getViewport().add(table);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        box.add(dbsearchpanel);
        box.add(tablesearchpanel);
        box.add(scrollpane);
        box.add(submitpanel);
        
        table.setModel(partnertablemodel);
        
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
        
        if(keyword.length() >= GlobalFields.MINIMAL_CHARACTER_ON_SEARCH){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

                ClientResponse response;

                @Override
                protected Boolean doInBackground(){

                    response = RestClient.get("partners?keyword=" + keyword);

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
                                            Partner.class));
                            partnertablemodel.setPartnerList(list);
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
    
    public Partner showDialog(){
        setVisible(true);
        
        if(table.getSelectedRow() != -1){
        
            return partnertablemodel.getPartner(table.
                        convertRowIndexToModel(table.getSelectedRow()));
        }
        
        return null;
    }

}
