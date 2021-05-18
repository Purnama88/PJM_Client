/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.detail.ModelDetail;
import com.purnama.pjm_client.gui.inner.dialog.ModelSelectDialog;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyList;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.model.combine.ItemModel;
import com.purnama.pjm_client.model.nontransactional.Brand;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.Model;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.ItemModelTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author p_cor
 */
public class ModelSearchPanel extends MyPanel implements ActionListener{

    private final MyPanel upperpanel;
    
    private final LabelTextFieldErrorPanel modelpanel;
    
    private final ModelSelectDialog modelselectdialog;
    
    private final SubmitPanel submitpanel;
    
    private final MyTable table;
    private final MyScrollPane scrollpane;
    
    private final JMenuItem menuitemdelete, menuitemdetail;
    
    private ArrayList<ItemModel> list;
    
    private final TableRowSorter<TableModel> sorter;
    private final ItemModelTableModel itemmodeltablemodel;
    
    private final int itemid;
    private final int index;
    
    public ModelSearchPanel(int itemid, int index){
        super(new BorderLayout());
        
        upperpanel = new MyPanel(new GridLayout(1, 2, 0, 5));
        
        modelselectdialog = new ModelSelectDialog();
        
        modelpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_MODEL"), "");
        
        submitpanel = new SubmitPanel();
        
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        itemmodeltablemodel = new ItemModelTableModel();
        
        menuitemdelete = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"),
                new MyImageIcon().getImage("image/Delete_16.png"));
        menuitemdetail = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DETAIL"),
                new MyImageIcon().getImage("image/Detail_16.png"));
        
        sorter = new TableRowSorter<>(table.getModel());
        
        list = new ArrayList<>();
        
        this.itemid = itemid;
        this.index = index;
        
        init();
    }
    
    private void init(){
        table.setModel(itemmodeltablemodel);
        
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
        
        upperpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), GlobalFields.PROPERTIES.getProperty("LABEL_SEARCH")));
        
        upperpanel.add(modelpanel);
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
        
        ItemModel itemmodel = itemmodeltablemodel.getItemModel(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        GlobalFields.MAINTABBEDPANE.insertTab(index+1, new ModelDetail(itemmodel.getModel().getId()));
    }
    
    public void delete(){
        ItemModel itemmodel = itemmodeltablemodel.getItemModel(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.delete("itemmodels?id=" + itemmodel.getId(), "");

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
                        itemmodeltablemodel.deleteRow(table.getSelectedRow());
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
                
                
                response = RestClient.get("itemmodels?itemid=" + itemid);
                
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
                                        ItemModel.class));
                        itemmodeltablemodel.setItemModelList(list);
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
        Model model = modelselectdialog.showDialog();
        
        ItemModel itemmodel = new ItemModel();
        itemmodel.setModel(model);
        
        Item item = new Item();
        item.setId(itemid);
        
        itemmodel.setItem(item);
        
        if(model != null){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.post("itemmodels", itemmodel);

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
                            ItemModel result = mapper.readValue(output, ItemModel.class);
                            itemmodeltablemodel.addRow(result);
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
    
//    private final MyPanel upperpanel, lowerpanel, modeldescriptionpanel;
//    
//    private final BrandList brandlist;
//    
//    private final ModelList modellist;
//    
//    private final MyScrollPane modelscrollpane, brandscrollpane;
//    
//    public ModelSearchPanel(){
//        super(new BorderLayout());
//        
//        upperpanel = new MyPanel();
//        lowerpanel = new MyPanel(new GridLayout(1, 3, 5, 5));
//        
//        modeldescriptionpanel = new MyPanel();
//        
//        brandlist = new BrandList();
//        
//        modellist = new ModelList();
//        
//        brandscrollpane = new MyScrollPane(brandlist);
//        
//        modelscrollpane = new MyScrollPane(modellist);
//        
//        upperpanel.add(new MyLabel("upper"));
//        
//        init();
//    }
//    
//    private void init(){
//        
//        upperpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        
//        upperpanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 75));
//        upperpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
//        upperpanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
//        
//        ListSelectionListener listSelectionListener = (ListSelectionEvent listSelectionEvent) -> {
//            if (!listSelectionEvent.getValueIsAdjusting()) {
//                Brand brand = (Brand)brandlist.getSelectedValue();
//                modellist.removeAll();
//                modellist.load(brand.getId());
//            }
//        };
//        brandlist.addListSelectionListener(listSelectionListener);
//        
//        ListSelectionListener listSelectionListener2 = (ListSelectionEvent listSelectionEvent) -> {
//            if (!listSelectionEvent.getValueIsAdjusting()) {
//                System.out.println(modellist.getSelectedValue() + "aa");
//            }
//        };
//        modellist.addListSelectionListener(listSelectionListener2);
//        
//        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        
//               
//        add(upperpanel, BorderLayout.NORTH);
//        add(lowerpanel, BorderLayout.CENTER);
//        
//        lowerpanel.add(brandscrollpane);
//        lowerpanel.add(modelscrollpane);
//        lowerpanel.add(modeldescriptionpanel);
//    }
//}
//
//class CheckListItem {
//
//    private final Model model;
//    private boolean isSelected = false;
//
//    public CheckListItem(Model model) {
//      this.model = model;
//    }
//
//    public boolean isSelected() {
//      return isSelected;
//    }
//
//    public void setSelected(boolean isSelected) {
//      this.isSelected = isSelected;
//    }
//
//    @Override
//    public String toString() {
//      return model.getName();
//    }
//}
//
//class CheckListRenderer extends JCheckBox implements ListCellRenderer {
//    @Override
//    public Component getListCellRendererComponent(JList list, Object value,
//      int index, boolean isSelected, boolean hasFocus) {
//        setEnabled(list.isEnabled());
//        setSelected(((CheckListItem) value).isSelected());
//        setFont(list.getFont());
//        setBackground(list.getBackground());
//        setForeground(list.getForeground());
//        setText(value.toString());
//        return this;
//    }
//}
//
//class ModelList extends MyList{
//    private final DefaultListModel listmodel;
//    
//    public ModelList(){
//        super();
//        
//        listmodel = new DefaultListModel();
//        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        
//        init();
//    }
//    
//    private void init(){
//        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Model"));
//        
//        setCellRenderer(new CheckListRenderer());
//        
//        setModel(listmodel);
//        
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent event) {
//              JList list = (JList) event.getSource();
//              int index = list.locationToIndex(event.getPoint());// Get index of item
//                                                                 // clicked
//              CheckListItem item = (CheckListItem) list.getModel()
//                  .getElementAt(index);
//              item.setSelected(!item.isSelected()); // Toggle selected state
//              list.repaint(list.getCellBounds(index, index));// Repaint cell
//            }
//          });
//        
//    }
//    
//    public void load(int brandid){
//        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
//            ClientResponse response;
//            
//            @Override
//            protected Boolean doInBackground() {
//                
//                response = RestClient.get("models?brandid="+ brandid +"&status=true");
//               
//                return true;
//                
//            }
//            
//            @Override
//            protected void done() {
//                if(response == null){
//                }
//                else if(response.getStatus() != 200) {
//                }
//                else{
//                    String output = response.getEntity(String.class);
//
//                    ObjectMapper mapper = new ObjectMapper();
//
//                    try{
//                        List<Model> modellist = mapper.readValue(output,
//                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
//                                        Model.class));
//                        
//                        listmodel.clear();
//                        
//                        for(Model model : modellist){
//                            listmodel.addElement(new CheckListItem(model));
//                        }
//                        
//                    }
//                    catch(IOException e){
//
//                    }
//                }
//            }
//        };
//        
//        worker.execute();
//    }
//}
//
//class BrandList extends MyList{
//    
//    private final DefaultListModel listmodel;
//    
//    public BrandList(){
//        super();
//        
//        listmodel = new DefaultListModel();
//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        
//        init();
//    }
//    
//    private void init(){
//        
//        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Brand"));
//        
//        setModel(listmodel);
//        
//        load();
//    }
//    
//    private void load(){
//        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
//            ClientResponse response;
//            
//            @Override
//            protected Boolean doInBackground() {
//                
//                response = RestClient.get("brands?status=true");
//               
//                return true;
//                
//            }
//            
//            @Override
//            protected void done() {
//                if(response == null){
//                }
//                else if(response.getStatus() != 200) {
//                }
//                else{
//                    String output = response.getEntity(String.class);
//
//                    ObjectMapper mapper = new ObjectMapper();
//
//                    try{
//                        List<Brand> brandlist = mapper.readValue(output,
//                                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
//                                        Brand.class));
//                        
//                        for(Brand brand : brandlist){
//                            listmodel.addElement(brand);
//                        }
//                        
//                    }
//                    catch(IOException e){
//
//                    }
//                }
//            }
//        };
//        
//        worker.execute();
//    }
//}