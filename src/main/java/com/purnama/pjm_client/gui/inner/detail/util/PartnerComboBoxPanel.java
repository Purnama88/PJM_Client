/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyComboBox;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class PartnerComboBoxPanel extends MyPanel{
    
    public static int CUSTOMER = 1;
    public static int SUPPLIER = 2;
    public static int NONTRADE = 3;
    
    private ArrayList<Partner> list;
    
    private final MyLabel label, loadinglabel;
    private final MyComboBox combobox;
    
    private final int type;
    
    public PartnerComboBoxPanel(int type){
        super(new FlowLayout(FlowLayout.LEFT));
        
        this.type = type;
        
        list = new ArrayList<>();
        combobox = new MyComboBox();
        
        label = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_PARTNER"), 100);
        
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
    
    public MyComboBox getComboBox(){
        return combobox;
    }
    
    public void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>(){
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                if(type == CUSTOMER){
                    response = RestClient.get("partners?type=customer&status=true");
                }
                else if(type == SUPPLIER){
                    response = RestClient.get("partners?type=supplier&status=true");
                }
                else{
                    response = RestClient.get("partners?type=nontrade&status=true");
                }
                
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
                                        Partner.class));
                        
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
    
    public void refresh(){
        load();
    }
    
    public Partner getComboBoxValue(){
        Partner partner = (Partner)combobox.getSelectedItem();
        return partner;
    }
    
    public void setComboBoxValue(Partner partner){
        if(partner != null){
            for(int i = 0; i < combobox.getItemCount(); i++){
                Partner temp = (Partner)combobox.getItemAt(i);
                if(partner.getId() == temp.getId()){
                    combobox.setSelectedIndex(i);
                }
            }
        }
    }
    
    public void setComboBoxEnabled(boolean value){
        combobox.setEnabled(value);
    }
}
