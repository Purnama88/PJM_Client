/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemGroupPanel extends MyPanel{
    
    private final MyPanel upperpanel, middlepanel, lowerpanel;
    
    private final JList itemgrouplist, itemgrouplist2;
    
    private ArrayList<ItemGroup> list;
    
    private final MyScrollPane scrollpane, scrollpane2;
    
    private final MyListModel listmodel, listmodel2;
    
    private final MyButton gobutton, backbutton, savebutton;
    
    public ItemGroupPanel(){
        super(new BorderLayout());
        
        upperpanel = new MyPanel();
        middlepanel = new MyPanel(new GridLayout(1, 2, 5, 5));
        lowerpanel = new MyPanel();
        
        itemgrouplist = new JList();
        itemgrouplist2 = new JList();
        
        scrollpane = new MyScrollPane(itemgrouplist);
        
        scrollpane2 = new MyScrollPane(itemgrouplist2);
        
        gobutton = new MyButton("go");
        backbutton = new MyButton("back");
        savebutton = new MyButton("save");
        
        listmodel = new MyListModel();
        listmodel2 = new MyListModel();
        
        init();
    }
    
    private void init(){
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        itemgrouplist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemgrouplist.setLayoutOrientation(JList.VERTICAL);
        itemgrouplist.setVisibleRowCount(-1);
        
        itemgrouplist.setCellRenderer(new ItemGroupRenderer());
        
        itemgrouplist2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemgrouplist2.setLayoutOrientation(JList.VERTICAL);
        itemgrouplist2.setVisibleRowCount(-1);
        
        itemgrouplist2.setCellRenderer(new ItemGroupRenderer());
        
        itemgrouplist.setModel(listmodel);
        itemgrouplist2.setModel(listmodel2);
        
        gobutton.addActionListener((ActionEvent e) -> {
            List<ItemGroup> sellist = itemgrouplist.getSelectedValuesList();
            
            for(ItemGroup itemgroup : sellist){
                listmodel.removeElement(itemgroup);
                listmodel2.addElement(itemgroup);
            }
        });
        
        backbutton.addActionListener((ActionEvent e) -> {
            List<ItemGroup> sellist = itemgrouplist2.getSelectedValuesList();
            
            for(ItemGroup itemgroup : sellist){
                listmodel.addElement(itemgroup);
                listmodel2.removeElement(itemgroup);
            }
        });
        
        middlepanel.add(scrollpane);
        middlepanel.add(scrollpane2);
        
        upperpanel.add(gobutton);
        upperpanel.add(backbutton);
        
        lowerpanel.add(savebutton);
        
        add(upperpanel, BorderLayout.NORTH);
        add(middlepanel, BorderLayout.CENTER);
        add(lowerpanel, BorderLayout.SOUTH);
        
        load();
    }
    
    private void load(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                response = RestClient.get("itemgroups?status=true");
               
                return true;
                
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String number : chunks) {
                }
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
                                        ItemGroup.class));
                        
                        listmodel.setBrandList(list);
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }
    
}

class ItemGroupRenderer extends JLabel implements ListCellRenderer<ItemGroup> {
 
    public ItemGroupRenderer() {
        init();
    }
    
    private void init(){
        setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends ItemGroup> list, ItemGroup itemgroup, int index,
        boolean isSelected, boolean cellHasFocus) {
          
        setText(itemgroup.getName());
         
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
 
        
        return this;
    }
     
}

class MyListModel extends AbstractListModel{

    private List<ItemGroup> itemgrouplist =  new ArrayList<>();;
    
    public MyListModel(){
        super();
    }
    
    @Override
    public int getSize() {
        return itemgrouplist.size();
    }

    @Override
    public Object getElementAt(int index) {
        return itemgrouplist.get(index);
    }
    
    
    public void addElement(ItemGroup element)
    {
        System.out.println("add->" + element);
        itemgrouplist.add(element);
        fireIntervalAdded(this, 0, getSize());
    }
    
    public void removeElement(ItemGroup element)
    {
        System.out.println("remove->" + element);
        itemgrouplist.remove(element);
        fireIntervalRemoved(this, 0, getSize());
    }
    
    public void setBrandList(List<ItemGroup> itemgrouplist){
        this.itemgrouplist = itemgrouplist;
        fireContentsChanged(this, 0, getSize());
    }
}