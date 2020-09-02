/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.form.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.model.nontransactional.Brand;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class BrandComboBoxPanel extends JPanel{
    
     private ArrayList<Brand> list;
    
    private final MyLabel label, loadinglabel, errorlabel;
    private final JComboBox combobox;
    
    public BrandComboBoxPanel(){
        super(new FlowLayout(FlowLayout.LEFT));
        
        list = new ArrayList<>();
        combobox = new JComboBox();
        
        label = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_BRAND"), 150);
        combobox.setPreferredSize(new Dimension(250, 25));
        loadinglabel = new MyLabel(new MyImageIcon().getImage("image/Loading_16.gif"));
        
        errorlabel = new MyLabel("", 200);
        
        init();
    }
    
    private void init(){
        setMaximumSize(new Dimension(700, 40));
        
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(combobox);
        add(loadinglabel);
        add(errorlabel);
        
        load();
    }
    
    public final void load(){
        SwingWorker<Boolean, String> refreshworker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                loadinglabel.setVisible(true);
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("brands?status=true");
                
                return true;
            }
            
            @Override
            protected void process(List<String> chunks) {
                chunks.forEach((number) -> {
                    setErrorLabel(number);
                });
            }
            
            @Override
            protected void done() {
                loadinglabel.setVisible(false);
                if(response == null){
                    setErrorLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(response.getStatus() != 200) {
                    setErrorLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                    + response.getStatus());
                }
                else{
                    errorlabel.setText("");
                    
                    String output = response.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        list = mapper.readValue(output,
                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                        Brand.class));
                        
                        DefaultComboBoxModel model = new DefaultComboBoxModel(list.toArray());

                        combobox.setModel(model);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        refreshworker.execute();
    }
    
    public int getComboBoxSize(){
        return combobox.getItemCount();
    }
    
    public Brand getComboBoxValue(){
        Brand brand = (Brand)combobox.getSelectedItem();
        return brand;
    }
    
    public void setComboBoxValue(Brand brand){
        ComboBoxModel model = combobox.getModel();
        
        int size = model.getSize();
        
        for(int i = 0; i < size; i++){
            Brand temp = (Brand)model.getElementAt(i);
            if(brand.getId() == temp.getId()){
                model.setSelectedItem(brand);
            }
        }
    }
    
    public void setComboBoxEnabled(boolean value){
        combobox.setEnabled(value);
    }
    
    public void setErrorLabel(String value){
        errorlabel.setText("<HTML><FONT COLOR=RED>" + value + "</FONT></HTML>");
    }
    
    public void refresh(){
        load();
    }
}