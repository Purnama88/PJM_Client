/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.login.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.model.nontransactional.Warehouse;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class WarehouseComboBoxPanel extends MyPanel{
    
    private ArrayList<Warehouse> list;
    
    private final MyLabel warehouseicon, loadinglabel;
    private final JComboBox combobox;
    
    public WarehouseComboBoxPanel(){
        super(new FlowLayout(FlowLayout.CENTER));
        
        list = new ArrayList<>();
        combobox = new JComboBox();
        
        warehouseicon = new MyLabel(new MyImageIcon().getImage("image/Warehouse_16.png"), 20, 20);
        loadinglabel = new MyLabel(new MyImageIcon().getImage("image/Loading_16.gif"));
        
        init();
    }
    
    public final void init(){
        add(warehouseicon);
        add(combobox);
        add(loadinglabel);
        
        load();
    }
    
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>(){
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                loadinglabel.setVisible(true);
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.getNonApi("warehouse/activelist");

                return true;
            }
            
            @Override
            protected void done() {
                loadinglabel.setVisible(false);
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
                                        Warehouse.class));
                        
                        DefaultComboBoxModel model = new DefaultComboBoxModel(list.toArray());
                        
                        combobox.setVisible(true);
                        combobox.setModel(model);
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public Warehouse getComboBoxValue(){
        Warehouse warehouse = (Warehouse)combobox.getSelectedItem();
        return warehouse;
    }
    
    public void setComboBoxValue(Warehouse warehouse){
        ComboBoxModel model = combobox.getModel();
        
        int size = model.getSize();
        
        for(int i = 0; i < size; i++){
            Warehouse temp = (Warehouse)model.getElementAt(i);
            if(warehouse.getId() == (temp.getId())){
                model.setSelectedItem(warehouse);
            }
        }
    }
    
    public void setComboBoxEnabled(boolean value){
        combobox.setEnabled(value);
    }
}
