/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.home.util;

import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyTextField;
import com.purnama.pjm_client.util.GlobalFields;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 *
 * @author p_cor
 */
public class UpperPanel extends MyPanel{
    
    private final MyPanel leftpanel, middlepanel, rightpanel;
    
    private final MyTextField textfield;
    
    private final MyLabel notiflabel, searchlabel, pagelabel, currentpagelabel, oflabel,
            totalpagelabel;;
    private final JProgressBar progressbar;
    
    private final MyButton addbutton, firstbutton, previousbutton, nextbutton, lastbutton;
    
    public UpperPanel(){
        super(new GridLayout(1, 3));
        
        leftpanel = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        middlepanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        rightpanel = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        
        textfield = new MyTextField("", 200);
        searchlabel = new MyLabel(new MyImageIcon().getImage("image/Search_16.png"));
        
        addbutton = new MyButton(new MyImageIcon().getImage("image/Add_16.png"), 24, 24);
        
        firstbutton = new MyButton(new MyImageIcon().getImage("image/Arrow2Left_16.png"), 24, 24);
        previousbutton = new MyButton(new MyImageIcon().getImage("image/Arrow1Left_16.png"), 24, 24);
        nextbutton = new MyButton(new MyImageIcon().getImage("image/Arrow1Right_16.png"), 24, 24);
        lastbutton = new MyButton(new MyImageIcon().getImage("image/Arrow2Right_16.png"), 24, 24);
        
        progressbar = new JProgressBar();
        
        notiflabel = new MyLabel();
        pagelabel = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_PAGE"));
        currentpagelabel = new MyLabel();
        oflabel = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_OF"));
        totalpagelabel = new MyLabel();
        
        init();
    }
    
    public final void init(){
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        progressbar.setIndeterminate(true);
        progressbar.setVisible(false);
        
        addbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_ADD"));
        firstbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_FIRSTPAGE"));
        previousbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_PREVIOUSPAGE"));
        nextbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_NEXTPAGE"));
        lastbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("TOOLTIP_LASTPAGE"));
        
        leftpanel.add(searchlabel);
        leftpanel.add(textfield);
        leftpanel.add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.LINE_START);
        leftpanel.add(addbutton);
        
        middlepanel.add(firstbutton);
        middlepanel.add(previousbutton);
        middlepanel.add(pagelabel);
        middlepanel.add(currentpagelabel);
        middlepanel.add(oflabel);
        middlepanel.add(totalpagelabel);
        middlepanel.add(nextbutton);
        middlepanel.add(lastbutton);
        
        rightpanel.add(notiflabel);
        rightpanel.add(progressbar);
        
        add(leftpanel);
        add(middlepanel);
        add(rightpanel);
    }
    
    public void setNotifLabel(String value){
        notiflabel.setText(value);
    }
    
    public void setCurrentPageLabel(String page){
        currentpagelabel.setText(page);
    }
    
    public void setTotalPageLabel(String totalpage){
        totalpagelabel.setText(totalpage);
    }
    
    public void hideProgressBar(){
        progressbar.setVisible(false);
    }
    
    public void showProgressBar(){
        progressbar.setVisible(true);
    }
    
    public MyButton getAddButton(){
        return addbutton;
    }
    
    public MyButton getFirstPageButton(){
        return firstbutton;
    }
    
    public MyButton getPreviousPageButton(){
        return previousbutton;
    }
    
    public MyButton getNextPageButton(){
        return nextbutton;
    }
    
    public MyButton getLastPageButton(){
        return lastbutton;
    }
    
    public MyTextField getSearchTextField(){
        return textfield;
    }
    
    public String getSearchKeyword(){
        String text = textfield.getText();
        String newtext = text.replaceAll(" ", "%20");
        return newtext;
    }
    
}
