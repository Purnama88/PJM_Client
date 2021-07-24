/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.dialog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.form.util.LabelComboBoxPanel;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemSelectDialog extends SelectDialog{
    
    private final LabelComboBoxPanel labelcombobox;
    
    private final ItemTableModel itemtablemodel;
    
    private ArrayList<Item> list;
    
    public ItemSelectDialog() {
        super("");
        
        itemtablemodel = new ItemTableModel();
        
        labelcombobox = new LabelComboBoxPanel();
        
        list = new ArrayList<>();
        
        init();
    }
    
    private void init(){
        
        ActionListener actionlistener = e -> {
            load();
        };
        
        labelcombobox.setActionListener(actionlistener);
        
        box.add(labelcombobox, 1);
        
        table.setModel(itemtablemodel);
        
    }
    
    
    @Override
    protected void load(){
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
    
    @Override
    public Item showDialog(){
        setVisible(true);
        
        if(table.getSelectedRow() != -1){
        
            return itemtablemodel.getItem(table.
                        convertRowIndexToModel(table.getSelectedRow()));
        }
        
        return null;
    }
}
