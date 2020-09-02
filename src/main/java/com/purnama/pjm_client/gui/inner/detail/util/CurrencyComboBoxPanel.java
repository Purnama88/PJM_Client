/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class CurrencyComboBoxPanel extends MyPanel{
    
    private ArrayList<Currency> list;
    
    private final MyLabel label, loadinglabel;
    private final JComboBox combobox;
    
    public CurrencyComboBoxPanel(){
        super(new FlowLayout(FlowLayout.LEFT));
        
        list = new ArrayList<>();
        combobox = new JComboBox();
        
        label = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_CURRENCY"), 100);
        
        loadinglabel = new MyLabel(new MyImageIcon().getImage("image/Loading_16.gif"));
        
        init();
    }
    
    private void init(){
        combobox.setPreferredSize(new Dimension(200, 25));
        
        add(label);
        add(combobox);
        add(loadinglabel);
        
        load();
    }
    
    public JComboBox getComboBox(){
        return combobox;
    }
    
    public final void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>(){
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("currencies?status=true");
                
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
                                        Currency.class));
                        
                        DefaultComboBoxModel model = new DefaultComboBoxModel(list.toArray());

                        combobox.setModel(model);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public Currency getComboBoxValue(){
        Currency currency = (Currency)combobox.getSelectedItem();
        return currency;
    }
    
    public void setComboBoxValue(Currency currency){
        if(currency != null){
            for(int i = 0; i < combobox.getItemCount(); i++){
                Currency temp = (Currency)combobox.getItemAt(i);
                if(currency.getId() == temp.getId()){
                    combobox.setSelectedIndex(i);
                }
            }
        }
    }
    
    public void setComboBoxEnabled(boolean value){
        combobox.setEnabled(value);
    }
    
    public void refresh(){
        load();
    }
}
