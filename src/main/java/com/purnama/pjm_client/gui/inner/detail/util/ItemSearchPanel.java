/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.detail.ItemDetail;
import com.purnama.pjm_client.gui.inner.dialog.ItemSelectDialog;
import com.purnama.pjm_client.gui.inner.form.util.LabelComboBoxPanel;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.model.combine.ItemItemGroup;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.ItemGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemItemGroupTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ItemSearchPanel extends MyPanel implements ActionListener{

    private final MyPanel upperpanel;
    
    private final LabelTextFieldErrorPanel itempanel;
    
    private final LabelComboBoxPanel labelcombobox;
    
    private final ItemSelectDialog itemselectdialog;
    
    private final SubmitPanel submitpanel;
    
    private final MyTable table;
    private final MyScrollPane scrollpane;
    
    private final JMenuItem menuitemdelete, menuitemdetail;
    
    private ArrayList<ItemItemGroup> list;
    
    private final TableRowSorter<TableModel> sorter;
    private final ItemItemGroupTableModel itemitemgrouptablemodel;
    
    private final int itemgroupid;
    
    private final int index;
    
    public ItemSearchPanel(int itemgroupid, int index){
        super(new BorderLayout());
        
        itempanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_ITEM"), "");
        
        labelcombobox = new LabelComboBoxPanel();
        
        itemselectdialog = new ItemSelectDialog();
        
        submitpanel = new SubmitPanel();
        
        upperpanel = new MyPanel();
        
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        itemitemgrouptablemodel = new ItemItemGroupTableModel();
        
        menuitemdelete = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"),
                new MyImageIcon().getImage("image/Delete_16.png"));
        menuitemdetail = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DETAIL"),
                new MyImageIcon().getImage("image/Detail_16.png"));
        
        sorter = new TableRowSorter<>(table.getModel());
        
        list = new ArrayList<>();
        
        this.itemgroupid = itemgroupid;
        this.index = index;
        
        init();
    }
    
    private void init(){
        table.setModel(itemitemgrouptablemodel);
        
        table.addMenuItemSeparator();
        table.addMenuItem(menuitemdetail);
        table.addMenuItemSeparator();
        table.addMenuItem(menuitemdelete);
        
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                Point p = e.getPoint();
		if(SwingUtilities.isLeftMouseButton(e)){
                    if(e.getClickCount() ==2){
                        detail();
                    }
		}
		else if(SwingUtilities.isRightMouseButton(e)){
                    int rowNumber = table.rowAtPoint( p );
                    ListSelectionModel model = table.getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
		}
            }
        });
        
        scrollpane.getViewport().add(table);
        scrollpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(upperpanel, BorderLayout.NORTH);
        add(scrollpane);
        
        upperpanel.setLayout(new BoxLayout(upperpanel, BoxLayout.Y_AXIS));
        
        upperpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), GlobalFields.PROPERTIES.getProperty("LABEL_SEARCH")));
        
        upperpanel.add(itempanel);
        upperpanel.add(labelcombobox);
        upperpanel.add(submitpanel);
        
        submitpanel.getSubmitButton().addActionListener(this);
        
        menuitemdetail.addActionListener((ActionEvent e) -> {
            detail();
        });
        
        menuitemdelete.addActionListener((ActionEvent e) -> {
            delete();
        });
        
        load();
    }
    
    public void detail(){
        
        ItemItemGroup itemitemgroup = itemitemgrouptablemodel.getItemItemGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(index+1, new ItemDetail(itemitemgroup.getItem().getId()));
    }
    
    public void delete(){
        ItemItemGroup itemitemgroup = itemitemgrouptablemodel.getItemItemGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.delete("itemitemgroups?id=" + itemitemgroup.getId(), "");

                    return true;
                }

                @Override
                protected void done() {
                    if(response == null){
                    }
                    else if(response.getStatus() != 200) {
                        String output = response.getEntity(String.class);
                        
                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                        + response.getStatus(), 
                        JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        itemitemgrouptablemodel.deleteRow(table.getSelectedRow());
                    }
                }
            };
            worker.execute();
    }
    
    public void load(){
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse response;
            
            @Override
            protected Boolean doInBackground(){
                
                
                response = RestClient.get("itemitemgroups?itemgroupid=" + itemgroupid);
                
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
                                        ItemItemGroup.class));
                        itemitemgrouptablemodel.setItemItemGroupList(list);
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
            }
        };
        worker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Item item = itemselectdialog.showDialog();
        
        ItemItemGroup iig = new ItemItemGroup();
        iig.setItem(item);
        
        ItemGroup itemgroup = new ItemGroup();
        itemgroup.setId(itemgroupid);
        
        iig.setItemgroup(itemgroup);
        
        if(item != null){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.post("itemitemgroups", iig);

                    return true;
                }

                @Override
                protected void done() {
                    if(response == null){
                    }
                    else if(response.getStatus() != 200) {
                        String output = response.getEntity(String.class);
                        
                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                        + response.getStatus(), 
                        JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        String output = response.getEntity(String.class);
                    
                        ObjectMapper mapper = new ObjectMapper();
                        
                        try {
                            ItemItemGroup result = mapper.readValue(output, ItemItemGroup.class);
                            itemitemgrouptablemodel.addRow(result);
                        } 
                        catch (IOException ex) {
                            
                        }
                        
                        
                    }
                }
            };
            worker.execute();
            
            
        }
    }
    

}
