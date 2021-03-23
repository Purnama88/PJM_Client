/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyList;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.model.nontransactional.Brand;
import com.purnama.pjm_client.model.nontransactional.Model;
import com.purnama.pjm_client.rest.RestClient;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author p_cor
 */
public class ModelPanel extends MyPanel{
    
    private final MyPanel upperpanel, lowerpanel, modeldescriptionpanel;
    
    private final BrandList brandlist;
    
    private final ModelList modellist;
    
    private final MyScrollPane modelscrollpane, brandscrollpane;
    
    public ModelPanel(){
        super(new BorderLayout());
        
        upperpanel = new MyPanel();
        lowerpanel = new MyPanel(new GridLayout(1, 3, 5, 5));
        
        modeldescriptionpanel = new MyPanel();
        
        brandlist = new BrandList();
        
        modellist = new ModelList();
        
        brandscrollpane = new MyScrollPane(brandlist);
        
        modelscrollpane = new MyScrollPane(modellist);
        
        upperpanel.add(new MyLabel("upper"));
        
        init();
    }
    
    private void init(){
        
        upperpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        upperpanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 75));
        upperpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        upperpanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
        
        ListSelectionListener listSelectionListener = (ListSelectionEvent listSelectionEvent) -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                Brand brand = (Brand)brandlist.getSelectedValue();
                modellist.removeAll();
                modellist.load(brand.getId());
            }
        };
        brandlist.addListSelectionListener(listSelectionListener);
        
        ListSelectionListener listSelectionListener2 = (ListSelectionEvent listSelectionEvent) -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                System.out.println(modellist.getSelectedValue() + "aa");
            }
        };
        modellist.addListSelectionListener(listSelectionListener2);
        
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
               
        add(upperpanel, BorderLayout.NORTH);
        add(lowerpanel, BorderLayout.CENTER);
        
        lowerpanel.add(brandscrollpane);
        lowerpanel.add(modelscrollpane);
        lowerpanel.add(modeldescriptionpanel);
    }
}

class CheckListItem {

    private final Model model;
    private boolean isSelected = false;

    public CheckListItem(Model model) {
      this.model = model;
    }

    public boolean isSelected() {
      return isSelected;
    }

    public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }

    @Override
    public String toString() {
      return model.getName();
    }
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}

class ModelList extends MyList{
    private final DefaultListModel listmodel;
    
    public ModelList(){
        super();
        
        listmodel = new DefaultListModel();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        init();
    }
    
    private void init(){
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Model"));
        
        setCellRenderer(new CheckListRenderer());
        
        setModel(listmodel);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
              JList list = (JList) event.getSource();
              int index = list.locationToIndex(event.getPoint());// Get index of item
                                                                 // clicked
              CheckListItem item = (CheckListItem) list.getModel()
                  .getElementAt(index);
              item.setSelected(!item.isSelected()); // Toggle selected state
              list.repaint(list.getCellBounds(index, index));// Repaint cell
            }
          });
        
    }
    
    public void load(int brandid){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.get("models?brandid="+ brandid +"&status=true");
               
                return true;
                
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
                        List<Model> modellist = mapper.readValue(output,
                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                        Model.class));
                        
                        listmodel.clear();
                        
                        for(Model model : modellist){
                            listmodel.addElement(new CheckListItem(model));
                        }
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
}

class BrandList extends MyList{
    
    private final DefaultListModel listmodel;
    
    public BrandList(){
        super();
        
        listmodel = new DefaultListModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        init();
    }
    
    private void init(){
        
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Brand"));
        
        setModel(listmodel);
        
        load();
    }
    
    private void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.get("brands?status=true");
               
                return true;
                
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
                        List<Brand> brandlist = mapper.readValue(output,
                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                        Brand.class));
                        
                        for(Brand brand : brandlist){
                            listmodel.addElement(brand);
                        }
                        
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
}