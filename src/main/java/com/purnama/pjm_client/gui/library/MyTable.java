/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import com.purnama.pjm_client.util.GlobalFields;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author p_cor
 */
public class MyTable extends JTable{
    
    private final JPopupMenu popupmenu;
    
    private final JMenuItem  menuitemcopy;
    
    public MyTable(){
        super();
        
        popupmenu = new JPopupMenu();
        
        menuitemcopy = new JMenuItem(GlobalFields.PROPERTIES.getProperty("LABEL_COPY"),
                new MyImageIcon().getImage("image/Copy_16.png"));
        
        init();
        
    }
    
    public final void init(){
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(getRowHeight() + 15);
        getTableHeader().setReorderingAllowed(false);
        
        setDefaultRenderer(Object.class, new BorderTableCellRenderer());
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                Point p = e.getPoint();
                if(SwingUtilities.isRightMouseButton(e)){
                    int rowNumber = rowAtPoint( p );
                    ListSelectionModel model = getSelectionModel();
                    model.setSelectionInterval(rowNumber, rowNumber);
		}
            }
        });
        
        menuitemcopy.addActionListener((ActionEvent e) -> {
            try{
                StringSelection stringselection = new StringSelection(
                        getValueAt(getSelectedRow(), getSelectedColumn()).toString());

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringselection,
                        null);
            }
            catch(HeadlessException exp){
                exp.printStackTrace();
            }
        });
        
        popupmenu.add(menuitemcopy);
        
        setComponentPopupMenu(popupmenu);
    }
    
    public void addMenuItem(JMenuItem menuitem){
        popupmenu.add(menuitem);
    }
    
    public void addMenuItemSeparator(){
        popupmenu.addSeparator();
    }
    
    public void removeMenuItem(JMenuItem menuitem){
        popupmenu.remove(menuitem);
    }
}

class BorderTableCellRenderer extends JLabel implements TableCellRenderer {
    
    protected Border noFocusBorder;
    
    public BorderTableCellRenderer() {
        noFocusBorder = new EmptyBorder(0, 3, 0, 0);
        setOpaque(true);
      }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } 
        else {
            if (row % 2 == 0) {
                setForeground(table.getForeground());
//                setBackground(table.getBackground());
                setBackground(Color.LIGHT_GRAY);
            }
            else{
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
        }
      setFont(table.getFont());
      
      setText((value == null) ? "" : value.toString());
      
      setBorder(noFocusBorder);
      
      return this;
    }
    
}