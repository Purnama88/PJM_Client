/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.main;

import com.purnama.pjm_client.gui.inner.home.BrandHome;
import com.purnama.pjm_client.gui.inner.home.CurrencyHome;
import com.purnama.pjm_client.gui.inner.home.InvoiceSalesDraftHome;
import com.purnama.pjm_client.gui.inner.home.ItemGroupHome;
import com.purnama.pjm_client.gui.inner.home.ItemHome;
import com.purnama.pjm_client.gui.inner.home.LabelHome;
import com.purnama.pjm_client.gui.inner.home.ModelHome;
import com.purnama.pjm_client.gui.inner.home.NumberingHome;
import com.purnama.pjm_client.gui.inner.home.PartnerGroupHome;
import com.purnama.pjm_client.gui.inner.home.PartnerHome;
import com.purnama.pjm_client.gui.inner.home.RoleHome;
import com.purnama.pjm_client.gui.inner.home.UserHome;
import com.purnama.pjm_client.gui.inner.home.WarehouseHome;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author p_cor
 */
public class MainNavigation extends MyPanel{
    
    private final JTree navigationtree;
    private final DefaultMutableTreeNode top;
    private final MyScrollPane scrollpane;
    
    private final MainTabbedPane maintabbedpane;
    
    public MainNavigation(MainTabbedPane maintabbedpane){
        super(new GridLayout(1,1));
        
        top = new DefaultMutableTreeNode("PJM");
        
        navigationtree = new JTree(top);
        
        scrollpane = new MyScrollPane();
        
        this.maintabbedpane = maintabbedpane;
        
        init();
        
    }
    
    private void init(){
        setMinimumSize(new Dimension(250, Integer.MAX_VALUE));
        
        DefaultMutableTreeNode master = new DefaultMutableTreeNode("Master");
        DefaultMutableTreeNode menu = new DefaultMutableTreeNode("Menu");
        DefaultMutableTreeNode group = new DefaultMutableTreeNode("Group");
        DefaultMutableTreeNode draft = new DefaultMutableTreeNode("Draft");
        DefaultMutableTreeNode setting = new DefaultMutableTreeNode("Setting");
        DefaultMutableTreeNode favourite = new DefaultMutableTreeNode("Favourite");
        
        DefaultMutableTreeNode warehouse = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_WAREHOUSE"));
        DefaultMutableTreeNode user = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_USER"));
        DefaultMutableTreeNode role = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_ROLE"));
        DefaultMutableTreeNode label = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_LABEL"));
        DefaultMutableTreeNode brand = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_BRAND"));
        DefaultMutableTreeNode model = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_MODEL"));
        DefaultMutableTreeNode item = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_ITEM"));
        DefaultMutableTreeNode partner = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_PARTNER"));
        
        DefaultMutableTreeNode itemgroup = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_ITEMGROUP"));
        DefaultMutableTreeNode partnergroup = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_PARTNERGROUP"));
        
        DefaultMutableTreeNode invoicesalesdraft = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_INVOICESALESDRAFT"));
        
        DefaultMutableTreeNode sales = new DefaultMutableTreeNode("Sales");
        DefaultMutableTreeNode purchase = new DefaultMutableTreeNode("Purchase");
        DefaultMutableTreeNode itemmanagement = new DefaultMutableTreeNode("Item Management");
        DefaultMutableTreeNode payment = new DefaultMutableTreeNode("Payment");
        
        DefaultMutableTreeNode currency = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_CURRENCY"));
        DefaultMutableTreeNode rate = new DefaultMutableTreeNode("Rate");
        DefaultMutableTreeNode numbering = new DefaultMutableTreeNode(GlobalFields.
                PROPERTIES.getProperty("MENU_NUMBERING"));
        
        top.add(master);
        top.add(group);
        top.add(draft);
        top.add(menu);
        top.add(favourite);
        top.add(setting);
        
        master.add(warehouse);
        master.add(user);
        master.add(role);
        master.add(label);
        master.add(brand);
        master.add(model);
        master.add(item);
        master.add(partner);
        
        group.add(itemgroup);
        group.add(partnergroup);
        
        draft.add(invoicesalesdraft);
        
        menu.add(sales);
        menu.add(purchase);
        menu.add(itemmanagement);
        menu.add(payment);
        
        setting.add(numbering);
        setting.add(currency);
        setting.add(rate);
        
        add(scrollpane);
        scrollpane.getViewport().add(navigationtree);
        
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                TreePath selPath = navigationtree.getPathForLocation(e.getX(), e.getY());
                int selRow = navigationtree.getRowForLocation(e.getX(), e.getY());
                
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {

                    }
                    else if(e.getClickCount() == 2) {
                        DefaultMutableTreeNode selectedNode = 
                                    ((DefaultMutableTreeNode)selPath.getLastPathComponent());
                        
                        if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_USER"))){
                            maintabbedpane.addTab(new UserHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_ROLE"))){
                            maintabbedpane.addTab(new RoleHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_WAREHOUSE"))){
                            maintabbedpane.addTab(new WarehouseHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_LABEL"))){
                            maintabbedpane.addTab(new LabelHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_BRAND"))){
                            maintabbedpane.addTab(new BrandHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_MODEL"))){
                            maintabbedpane.addTab(new ModelHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_ITEMGROUP"))){
                            maintabbedpane.addTab(new ItemGroupHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_PARTNER"))){
                            maintabbedpane.addTab(new PartnerHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_PARTNERGROUP"))){
                            maintabbedpane.addTab(new PartnerGroupHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_CURRENCY"))){
                            maintabbedpane.addTab(new CurrencyHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_ITEM"))){
                            maintabbedpane.addTab(new ItemHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_NUMBERING"))){
                            maintabbedpane.addTab(new NumberingHome());
                        }
                        else if(selectedNode.toString().equals(GlobalFields.PROPERTIES.getProperty("MENU_INVOICESALESDRAFT"))){
                            maintabbedpane.addTab(new InvoiceSalesDraftHome());
                        }
                    }
                }
            }
        };
        
        navigationtree.addMouseListener(ml);
    }
}
