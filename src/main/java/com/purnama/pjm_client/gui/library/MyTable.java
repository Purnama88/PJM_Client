/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.library;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author p_cor
 */
public class MyTable extends JTable{
    
    public MyTable(){
        super();
        
        init();
        
    }
    
    public final void init(){
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(getRowHeight() + 15);
        getTableHeader().setReorderingAllowed(false);
        
        setDefaultRenderer(Object.class, new BorderTableCellRenderer());
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