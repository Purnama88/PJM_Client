/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.detail.util.SelectableLabelContentPanel;
import com.purnama.pjm_client.gui.inner.form.ItemEdit;
import com.purnama.pjm_client.gui.inner.home.ItemHome;
import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class ItemDetail extends DetailPanel{
    
    private Item item;
    
    private final SelectableLabelContentPanel codepanel, namepanel, descriptionpanel, costpanel, labelpanel;
    
    private final ItemGroupPanel itemgrouppanel;
    
    private final int id;
    
    public ItemDetail(int id) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_ITEM_DETAIL"));
        this.id = id;
        
        namepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_NAME"),
                "");
        codepanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_CODE"),
                "");
        descriptionpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_DESCRIPTION"),
                "");
        costpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_COST"),
                "");
        labelpanel = new SelectableLabelContentPanel(GlobalFields.PROPERTIES.getProperty("LABEL_LABEL"),
                "");
        
        itemgrouppanel = new ItemGroupPanel();
        
        init();
    }
    
    private void init(){
        
        detailpanel.add(codepanel);
        detailpanel.add(namepanel);
        detailpanel.add(descriptionpanel);
        detailpanel.add(costpanel);
        detailpanel.add(labelpanel);
        detailpanel.add(statuspanel);
        detailpanel.add(notepanel);
        detailpanel.add(lastmodifiedpanel);
        
        tabbedpane.addTab(GlobalFields.PROPERTIES.getProperty("PANEL_ITEMGROUP"), itemgrouppanel);
        
        load();
    }

    @Override
    protected void load() {
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground() {
                
                upperpanel.showProgressBar();
                
                publish(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_CONNECTING"));
                
                response = RestClient.get("items/" + id);
               
                return true;
                
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String number : chunks) {
                upperpanel.setNotifLabel(number);
                }
            }
            
            @Override
            protected void done() {
                upperpanel.hideProgressBar();
                
                if(response == null){
                    upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(response.getStatus() != 200) {
                    upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                    + response.getStatus());
                }
                else{
                    upperpanel.setNotifLabel("");
                    
                    String output = response.getEntity(String.class);
                    
                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        item = mapper.readValue(output, Item.class);
                        
                        idpanel.setContentValue(String.valueOf(item.getId()));
                        codepanel.setContentValue(item.getCode());
                        namepanel.setContentValue(item.getName());
                        descriptionpanel.setContentValue(item.getDescription());
                        costpanel.setContentValue(item.getFormattedCost());
                        labelpanel.setContentValue(item.getLabel().getName());
                        notepanel.setContentValue(item.getNote());
                        statuspanel.setContentValue(item.isStatus());
                        lastmodifiedpanel.setContentValue(item.getFormattedLastmodified());
                    }
                    catch(IOException e){

                    }
                }
            }
        };
        
        worker.execute();
    }

    @Override
    protected void home() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(MainTabbedPane.class, this);
        
        tabbedPane.changeTabPanel(getIndex(), new ItemHome());
    }

    @Override
    protected void edit() {
        MainTabbedPane tabbedPane = (MainTabbedPane)SwingUtilities.
                getAncestorOfClass(JTabbedPane.class, this);
        
        tabbedPane.insertTab(this.getIndex()+1, new ItemEdit(item.getId()));
    }
    
}

class ItemGroupRenderer extends JLabel implements ListCellRenderer<ItemGroup> {
 
    public ItemGroupRenderer() {
        setOpaque(true);
    } 
    
    @Override
    public Component getListCellRendererComponent(JList<? extends ItemGroup> list, ItemGroup itemgroup, int index,
        boolean isSelected, boolean cellHasFocus) {
          
//        String code = itemgroup.getCode();
//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code + ".png"));
//         
//        setIcon(imageIcon);
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

class ItemGroupPanel extends MyPanel{
    
    private final JList itemgrouplist, itemgrouplist2;
    
    private ArrayList<ItemGroup> list;
    
    private final JScrollPane scrollpane, scrollpane2;
    
    private final MyListModel listmodel, listmodel2;
    
    private final MyButton gobutton, backbutton;
    
    public ItemGroupPanel(){
        itemgrouplist = new JList();
        itemgrouplist2 = new JList();
        
        scrollpane = new JScrollPane(itemgrouplist);
        scrollpane.setPreferredSize(new Dimension(250, 80));
        
        scrollpane2 = new JScrollPane(itemgrouplist2);
        scrollpane2.setPreferredSize(new Dimension(250, 80));
        
        gobutton = new MyButton("go");
        backbutton = new MyButton("back");
        
        listmodel = new MyListModel();
        listmodel2 = new MyListModel ();
        
        init();
    }
    
    private void init(){
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
        
        add(scrollpane);
        add(scrollpane2);
        add(gobutton);
        add(backbutton);
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