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
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.model.nontransactional.Warehouse;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import lombok.Data;

/**
 *
 * @author p_cor
 */
public class WarehouseCheckBoxPanel extends MyPanel{
    
    private final MyLabel label, loadinglabel, errorlabel;;
    
    private final JPanel panel;
    
    private final List<WarehouseCheckBox> checkboxlist;
    
    private ArrayList<Warehouse> list;
    
    public WarehouseCheckBoxPanel(){
        super(new FlowLayout(FlowLayout.LEFT));
        
        panel = new JPanel();
        panel.setMaximumSize(new Dimension(250, 25));
        
        label = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_WAREHOUSE"), 150);
        loadinglabel = new MyLabel(new MyImageIcon().getImage("image/Loading_16.gif"));
        errorlabel = new MyLabel("", 200);
        
        checkboxlist = new ArrayList<>();
        
        init();
    }
    
    private void init(){
        add(label);
        add(panel);
        add(loadinglabel);
        add(errorlabel);
        
        setMaximumSize(new Dimension(700, 200));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        load();
    }
    
    public final void load(){
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>(){
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                loadinglabel.setVisible(true);
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("warehouses?status=true");
                
                return true;
            }
            
            @Override
            protected void process(List<String> chunks) {
                chunks.forEach((number) -> {
                    errorlabel.setText(number);
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
                                        Warehouse.class));
                        
                        panel.setLayout(new GridLayout((list.size()+2)/2, 2));
                        panel.removeAll();
                        
                        list.stream().forEach((o) -> {
                            Warehouse warehouse = (Warehouse)o;
                            WarehouseCheckBox jcb = new WarehouseCheckBox(warehouse.getCode(), warehouse);
                            jcb.setToolTipText("<HTML>" + warehouse.getName() + "<BR/>" + 
                                    warehouse.getAddress() + "</HTML>");
                            jcb.setActionCommand(warehouse.getCode());
                            checkboxlist.add(jcb);
                            panel.add(jcb);
                        });
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
    public Set<Warehouse> getCheckBoxValues(){
        
        Set<Warehouse> set = new HashSet<>();
        
        checkboxlist.stream().filter((jcb) -> (jcb.isSelected())).map((jcb) -> {
            Warehouse warehouse = jcb.getWarehouse();
            return warehouse;
        }).forEachOrdered((warehouse) -> {
            set.add(warehouse);
        });
        
        return set;
    }
    
    public void setCheckBoxValues(Set<Warehouse> warehouses){
        
        checkboxlist.forEach((wcb) -> {
            warehouses.stream().filter((warehouse) -> (wcb.getActionCommand().equals(warehouse.getCode()))).forEachOrdered((_item) -> {
                wcb.setSelected(true);
            });
        });

//        checkboxlist.forEach((jcb) -> {
//            warehouses.stream().filter((warehouse) -> (warehouse.getId() == (Integer.parseInt(jcb.getActionCommand())))).forEachOrdered((_item) -> {
//                System.out.println(jcb.getActionCommand());
//                jcb.setSelected(true);
//            });
//        });
    }
    
    public void setCheckBoxEnabled(boolean status){
        checkboxlist.forEach((jcb) -> {
            jcb.setEnabled(status);
        });
    }

    public void setErrorLabel(String value){
        errorlabel.setText("<HTML><FONT COLOR=RED>" + value + "</FONT></HTML>");
    }
    
    public final void refresh(){
        load();
    }
}

@Data
class WarehouseCheckBox extends JCheckBox{
    
    private Warehouse warehouse;
    
    public WarehouseCheckBox(String title, Warehouse warehouse){
        super(title);
        this.warehouse = warehouse;
    }
    
}
