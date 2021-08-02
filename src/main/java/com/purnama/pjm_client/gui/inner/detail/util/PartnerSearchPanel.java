/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.detail.PartnerDetail;
import com.purnama.pjm_client.gui.inner.dialog.PartnerSelectDialog;
import com.purnama.pjm_client.gui.inner.form.util.LabelTextFieldErrorPanel;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.gui.library.MyTable;
import com.purnama.pjm_client.gui.main.MainPanel;
import com.purnama.pjm_client.model.combine.PartnerPartnerGroup;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.model.nontransactional.PartnerGroup;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.PartnerPartnerGroupTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.BorderLayout;
import java.awt.Component;
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
public class PartnerSearchPanel extends MyPanel implements ActionListener{

    private final MyPanel upperpanel;
    
    private final LabelTextFieldErrorPanel partnerpanel;
    
    private final PartnerSelectDialog partnerselectdialog;
    
    private final SubmitPanel submitpanel;
    
    private final MyTable table;
    private final MyScrollPane scrollpane;
    
    private final JMenuItem menuitemdelete, menuitemdetail;
    
    private ArrayList<PartnerPartnerGroup> list;
    
    private final TableRowSorter<TableModel> sorter;
    private final PartnerPartnerGroupTableModel partnerpartnergrouptablemodel;
    
    private final int partnergroupid;
    
    public PartnerSearchPanel(int partnergroupid){
        super(new BorderLayout());
        
        upperpanel = new MyPanel(new GridLayout(1, 2, 0, 5));
        
        partnerselectdialog = new PartnerSelectDialog();
        
        partnerpanel = new LabelTextFieldErrorPanel(GlobalFields.PROPERTIES.getProperty("LABEL_PARTNER"), "");
        
        submitpanel = new SubmitPanel();
        
        table = new MyTable();
        scrollpane = new MyScrollPane();
        
        partnerpartnergrouptablemodel = new PartnerPartnerGroupTableModel();
        
        menuitemdelete = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DELETE"),
                new MyImageIcon().getImage("image/Delete_16.png"));
        menuitemdetail = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_DETAIL"),
                new MyImageIcon().getImage("image/Detail_16.png"));
        
        sorter = new TableRowSorter<>(table.getModel());
        
        list = new ArrayList<>();
        
        this.partnergroupid = partnergroupid;
        
        init();
    }
    
    private void init(){
        table.setModel(partnerpartnergrouptablemodel);
        
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
        
        upperpanel.add(partnerpanel);
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
        
        PartnerPartnerGroup partnerpartnergroup = partnerpartnergrouptablemodel.getPartnerPartnerGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        int index = ((MainPanel)getParent().getParent()).getIndex();
        
        GlobalFields.MAINTABBEDPANE.insertTab(index+1, new PartnerDetail(partnerpartnergroup.getPartner().getId()));
    }
    
    public void delete(){
        PartnerPartnerGroup partnerpartnergroup = partnerpartnergrouptablemodel.getPartnerPartnerGroup(table.
                    convertRowIndexToModel(table.getSelectedRow()));
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.delete("partnerpartnergroups?id=" + partnerpartnergroup.getId(), "");

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
                        partnerpartnergrouptablemodel.deleteRow(table.getSelectedRow());
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
                
                
                response = RestClient.get("partnerpartnergroups?partnergroupid=" + partnergroupid);
                
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
                                        PartnerPartnerGroup.class));
                        partnerpartnergrouptablemodel.setPartnerPartnerGroupList(list);
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
        Partner partner = partnerselectdialog.showDialog();
        
        PartnerPartnerGroup iig = new PartnerPartnerGroup();
        iig.setPartner(partner);
        
        PartnerGroup partnergroup = new PartnerGroup();
        partnergroup.setId(partnergroupid);
        
        iig.setPartnergroup(partnergroup);
        
        if(partner != null){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse response;

                @Override
                protected Boolean doInBackground(){


                    response = RestClient.post("partnerpartnergroups", iig);

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
                            PartnerPartnerGroup result = mapper.readValue(output, PartnerPartnerGroup.class);
                            partnerpartnergrouptablemodel.addRow(result);
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
