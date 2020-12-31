/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.gui.inner.form.util.SubmitPanel;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyScrollPane;
import com.purnama.pjm_client.model.nontransactional.BuyPrice;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.SellPrice;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class PricePanel extends MyPanel{
    
    private final MyScrollPane sellpricescrollpane, buypricescrollpane;
    
    private final SubmitPanel sellpricesubmitpanel, buypricesubmitpanel;
    
    private final MyPanel sellpricepanel, buypricepanel;
    
    private final MyLabel wholebuypricelabel, wholesellpricelabel;
    
    private final QuantityPricePanel 
            quantitysellpricepanel, quantitysellpricepanel2, quantitysellpricepanel3, quantitysellpricepanel4, quantitysellpricepanel5, 
            quantitybuypricepanel, quantitybuypricepanel2, quantitybuypricepanel3, quantitybuypricepanel4, quantitybuypricepanel5;
    
    private Item item;
    
    public PricePanel(){
        super(new GridLayout(1, 3, 5, 5));
        
        sellpricescrollpane = new MyScrollPane();
        buypricescrollpane = new MyScrollPane();
        
        sellpricepanel = new MyPanel();
        buypricepanel = new MyPanel();
       
        wholebuypricelabel = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_WHOLESALE"));
        wholesellpricelabel = new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_WHOLESALE"));
                
        quantitysellpricepanel = new QuantityPricePanel(1, 1);
        quantitysellpricepanel2 = new QuantityPricePanel(1, 1);
        quantitysellpricepanel3 = new QuantityPricePanel(1, 1);
        quantitysellpricepanel4 = new QuantityPricePanel(1, 1);
        quantitysellpricepanel5 = new QuantityPricePanel(1, 1);
        quantitybuypricepanel = new QuantityPricePanel(1, 1);
        quantitybuypricepanel2 = new QuantityPricePanel(1, 1);
        quantitybuypricepanel3 = new QuantityPricePanel(1, 1);
        quantitybuypricepanel4 = new QuantityPricePanel(1, 1);
        quantitybuypricepanel5 = new QuantityPricePanel(1, 1);
        
        sellpricesubmitpanel = new SubmitPanel();
        buypricesubmitpanel = new SubmitPanel();
        
        init();
    }
    
    private void init(){
        
        quantitysellpricepanel2.setPrevious(quantitysellpricepanel);
        quantitysellpricepanel3.setPrevious(quantitysellpricepanel2);
        quantitysellpricepanel4.setPrevious(quantitysellpricepanel3);
        quantitysellpricepanel5.setPrevious(quantitysellpricepanel4);
        
        quantitysellpricepanel2.setNext(quantitysellpricepanel3);
        quantitysellpricepanel3.setNext(quantitysellpricepanel4);
        quantitysellpricepanel4.setNext(quantitysellpricepanel5);
        
        quantitybuypricepanel2.setPrevious(quantitybuypricepanel);
        quantitybuypricepanel3.setPrevious(quantitybuypricepanel2);
        quantitybuypricepanel4.setPrevious(quantitybuypricepanel3);
        quantitybuypricepanel5.setPrevious(quantitybuypricepanel4);
        
        quantitybuypricepanel2.setNext(quantitybuypricepanel3);
        quantitybuypricepanel3.setNext(quantitybuypricepanel4);
        quantitybuypricepanel4.setNext(quantitybuypricepanel5);
        
        quantitysellpricepanel.setPriceTextFieldEditable(true);
        quantitysellpricepanel.removeCheckBox();
        quantitybuypricepanel.setPriceTextFieldEditable(true);
        quantitybuypricepanel.removeCheckBox();
        
        sellpricescrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), GlobalFields.PROPERTIES.getProperty("LABEL_SELLPRICE")));
        buypricescrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), GlobalFields.PROPERTIES.getProperty("LABEL_BUYPRICE")));
        
        sellpricepanel.setLayout(new BoxLayout(sellpricepanel, BoxLayout.Y_AXIS));
        buypricepanel.setLayout(new BoxLayout(buypricepanel, BoxLayout.Y_AXIS));
        
        sellpricescrollpane.getViewport().add(sellpricepanel);
        buypricescrollpane.getViewport().add(buypricepanel);
        
        sellpricepanel.add(quantitysellpricepanel);
        sellpricepanel.add(wholesellpricelabel);
        sellpricepanel.add(quantitysellpricepanel2);
        sellpricepanel.add(quantitysellpricepanel3);
        sellpricepanel.add(quantitysellpricepanel4);
        sellpricepanel.add(quantitysellpricepanel5);
        sellpricepanel.add(sellpricesubmitpanel);
        buypricepanel.add(quantitybuypricepanel);
        buypricepanel.add(wholebuypricelabel);
        buypricepanel.add(quantitybuypricepanel2);
        buypricepanel.add(quantitybuypricepanel3);
        buypricepanel.add(quantitybuypricepanel4);
        buypricepanel.add(quantitybuypricepanel5);
        buypricepanel.add(buypricesubmitpanel);
                
        sellpricesubmitpanel.getSubmitButton().addActionListener((ActionEvent e) -> {
            if(verifySellPrice()){
                submitSellPrice();
            }
        });
        
        buypricesubmitpanel.getSubmitButton().addActionListener((ActionEvent e) -> {
            if(verifyBuyPrice()){
                submitBuyPrice();
            }
        });
        
        add(sellpricescrollpane);
        add(buypricescrollpane);
    }
    
    public void load(Item item){
        this.item = item;
        
        quantitysellpricepanel.setPriceTextFieldValue(item.getSellprice());
        quantitybuypricepanel.setPriceTextFieldValue(item.getBuyprice());
        
        if(item.isBulkbuyprice()){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse clientresponse;

                @Override
                protected Boolean doInBackground(){


                    clientresponse = RestClient.get("buyprices?itemid="+item.getId());

                    return true;
                }

                @Override
                protected void done() {
                    buypricesubmitpanel.finish();

                    if(clientresponse == null){
    //                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                    }
                    else if(clientresponse.getStatus() != 200) {
    //                        upperpanel.setNotifLabel("");
    //                        
    //                        String output = clientresponse.getEntity(String.class);
    //                        
    //                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
    //                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
    //                                        + clientresponse.getStatus(), 
    //                        JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        String output = clientresponse.getEntity(String.class);

                        ObjectMapper mapper = new ObjectMapper();

                        try{
                            ArrayList list = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            BuyPrice.class));
                                            
                            int size = list.size();
                            
                            if(size >= 1){
                                BuyPrice buyprice = (BuyPrice)list.get(0);
                                quantitybuypricepanel2.setCheckBoxValue(true);
                                quantitybuypricepanel2.setPriceTextFieldValue(buyprice.getPrice());
                                quantitybuypricepanel2.setQuantityTextFieldValue(buyprice.getQuantity());
                            }
                            
                            if(size >= 2){
                                BuyPrice buyprice = (BuyPrice)list.get(1);
                                quantitybuypricepanel3.setCheckBoxValue(true);
                                quantitybuypricepanel3.setPriceTextFieldValue(buyprice.getPrice());
                                quantitybuypricepanel3.setQuantityTextFieldValue(buyprice.getQuantity());
                            }
                            
                            if(size >= 3){
                                BuyPrice buyprice = (BuyPrice)list.get(2);
                                quantitybuypricepanel4.setCheckBoxValue(true);
                                quantitybuypricepanel4.setPriceTextFieldValue(buyprice.getPrice());
                                quantitybuypricepanel4.setQuantityTextFieldValue(buyprice.getQuantity());
                            }
                            
                            if(size >= 4){
                                BuyPrice buyprice = (BuyPrice)list.get(3);
                                quantitybuypricepanel5.setCheckBoxValue(true);
                                quantitybuypricepanel5.setPriceTextFieldValue(buyprice.getPrice());
                                quantitybuypricepanel5.setQuantityTextFieldValue(buyprice.getQuantity());
                            }
                        }
                        catch(IOException e){

                        }
                    }
                }
            };

            worker.execute();
        }
        
        if(item.isBulksellprice()){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
                ClientResponse clientresponse;

                @Override
                protected Boolean doInBackground(){


                    clientresponse = RestClient.get("sellprices?itemid="+item.getId());

                    return true;
                }

                @Override
                protected void done() {
                    sellpricesubmitpanel.finish();

                    if(clientresponse == null){
    //                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                    }
                    else if(clientresponse.getStatus() != 200) {
    //                        upperpanel.setNotifLabel("");
    //                        
    //                        String output = clientresponse.getEntity(String.class);
    //                        
    //                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
    //                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
    //                                        + clientresponse.getStatus(), 
    //                        JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        String output = clientresponse.getEntity(String.class);

                        ObjectMapper mapper = new ObjectMapper();

                        try{
                            ArrayList list = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            SellPrice.class));
                                        
                            int size = list.size();
                            
                            if(size >= 1){
                                SellPrice sellprice = (SellPrice)list.get(0);
                                quantitysellpricepanel2.setCheckBoxValue(true);
                                quantitysellpricepanel2.setPriceTextFieldValue(sellprice.getPrice());
                                quantitysellpricepanel2.setQuantityTextFieldValue(sellprice.getQuantity());
                            }
                            
                            if(size >= 2){
                                SellPrice sellprice = (SellPrice)list.get(1);
                                quantitysellpricepanel3.setCheckBoxValue(true);
                                quantitysellpricepanel3.setPriceTextFieldValue(sellprice.getPrice());
                                quantitysellpricepanel3.setQuantityTextFieldValue(sellprice.getQuantity());
                            }
                            
                            if(size >= 3){
                                SellPrice sellprice = (SellPrice)list.get(2);
                                quantitysellpricepanel4.setCheckBoxValue(true);
                                quantitysellpricepanel4.setPriceTextFieldValue(sellprice.getPrice());
                                quantitysellpricepanel4.setQuantityTextFieldValue(sellprice.getQuantity());
                            }
                            
                            if(size >= 4){
                                SellPrice sellprice = (SellPrice)list.get(3);
                                quantitysellpricepanel5.setCheckBoxValue(true);
                                quantitysellpricepanel5.setPriceTextFieldValue(sellprice.getPrice());
                                quantitysellpricepanel5.setQuantityTextFieldValue(sellprice.getQuantity());
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
    
    protected void submitSellPrice(){
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse clientresponse;

            @Override
            protected Boolean doInBackground(){

                sellpricesubmitpanel.loading();

                item.setSellprice(quantitysellpricepanel.getPriceTextFieldDecimalValue());
                item.setBulksellprice(isBulkSellPrice());
                
                clientresponse = RestClient.put("items", item);

                return true;
            }

            @Override
            protected void done() {
                sellpricesubmitpanel.finish();

                if(clientresponse == null){
//                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(clientresponse.getStatus() != 200) {
//                        upperpanel.setNotifLabel("");
//                        
//                        String output = clientresponse.getEntity(String.class);
//                        
//                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
//                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
//                                        + clientresponse.getStatus(), 
//                        JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String output = clientresponse.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        Item item = mapper.readValue(output, Item.class);
//                        detail(item.getId());
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
            }
        };

        worker.execute();
        
        SwingWorker<Boolean, String> worker2 = new SwingWorker<Boolean, String>() {
            
            ClientResponse clientresponse;

            @Override
            protected Boolean doInBackground(){

                sellpricesubmitpanel.loading();

                if(isBulkSellPrice()){
                    clientresponse = RestClient.post("sellprices", getSellPriceList());
                }
                else{
                    clientresponse = RestClient.delete("sellprices?itemid=" + item.getId(), "");
                }

                return true;
            }

            @Override
            protected void done() {
                sellpricesubmitpanel.finish();

                if(clientresponse == null){
//                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(clientresponse.getStatus() != 200) {
//                        upperpanel.setNotifLabel("");
//                        
//                        String output = clientresponse.getEntity(String.class);
//                        
//                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
//                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
//                                        + clientresponse.getStatus(), 
//                        JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String output = clientresponse.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

//                        try{
//                            Brand brand = mapper.readValue(output, Brand.class);
//                            detail(brand.getId());
//                        }
//                        catch(IOException e){
//                            e.printStackTrace();
//                        }
                }
            }
        };

        worker2.execute();
    }
    
    protected void submitBuyPrice(){
        
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            
            ClientResponse clientresponse;

            @Override
            protected Boolean doInBackground(){

                sellpricesubmitpanel.loading();

                item.setBuyprice(quantitybuypricepanel.getPriceTextFieldDecimalValue());
                item.setBulkbuyprice(isBulkBuyPrice());
                
                clientresponse = RestClient.put("items", item);

                return true;
            }

            @Override
            protected void done() {
                sellpricesubmitpanel.finish();

                if(clientresponse == null){
//                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(clientresponse.getStatus() != 200) {
//                        upperpanel.setNotifLabel("");
//                        
//                        String output = clientresponse.getEntity(String.class);
//                        
//                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
//                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
//                                        + clientresponse.getStatus(), 
//                        JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String output = clientresponse.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

                    try{
                        Item item = mapper.readValue(output, Item.class);
//                        detail(item.getId());
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
            }
        };

        worker.execute();
        
        SwingWorker<Boolean, String> worker2 = new SwingWorker<Boolean, String>() {
            
            ClientResponse clientresponse;

            @Override
            protected Boolean doInBackground(){

                sellpricesubmitpanel.loading();

                if(isBulkBuyPrice()){
                    clientresponse = RestClient.post("buyprices", getBuyPriceList());
                }
                else{
                    clientresponse = RestClient.delete("buyprices?itemid=" + item.getId(), "");
                }

                return true;
            }

            @Override
            protected void done() {
                sellpricesubmitpanel.finish();

                if(clientresponse == null){
//                        upperpanel.setNotifLabel(GlobalFields.PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"));
                }
                else if(clientresponse.getStatus() != 200) {
//                        upperpanel.setNotifLabel("");
//                        
//                        String output = clientresponse.getEntity(String.class);
//                        
//                        JOptionPane.showMessageDialog(GlobalFields.MAINFRAME, 
//                        output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
//                                        + clientresponse.getStatus(), 
//                        JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String output = clientresponse.getEntity(String.class);

                    ObjectMapper mapper = new ObjectMapper();

//                        try{
//                            Brand brand = mapper.readValue(output, Brand.class);
//                            detail(brand.getId());
//                        }
//                        catch(IOException e){
//                            e.printStackTrace();
//                        }
                }
            }
        };

        worker2.execute();
    }
    
    private boolean isBulkSellPrice(){
        return (quantitysellpricepanel2.isState() || quantitysellpricepanel3.isState() 
                || quantitysellpricepanel4.isState() || quantitysellpricepanel5.isState());
    }
    
    private boolean isBulkBuyPrice(){
        return (quantitybuypricepanel2.isState() || quantitybuypricepanel3.isState() 
                || quantitybuypricepanel4.isState() || quantitybuypricepanel5.isState());
    }
    
    private boolean verifySellPrice(){
        return (quantitysellpricepanel2.verify() && quantitysellpricepanel3.verify() 
                && quantitysellpricepanel4.verify() && quantitysellpricepanel5.verify());
    }
    
    private boolean verifyBuyPrice(){
        return (quantitybuypricepanel2.verify() && quantitybuypricepanel3.verify() 
                && quantitybuypricepanel4.verify() && quantitybuypricepanel5.verify());
    }
    
    private List<SellPrice> getSellPriceList(){
        List <SellPrice> sellpricelist = new ArrayList<>();
        
        if(quantitysellpricepanel2.isState()){
            SellPrice sellprice = new SellPrice();
            sellprice.setItem(item);
            sellprice.setPrice(quantitysellpricepanel2.getPriceTextFieldDecimalValue());
            sellprice.setQuantity(quantitysellpricepanel2.getQuantityTextFieldIntegerValue());
            
            sellpricelist.add(sellprice);
        }
        
        if(quantitysellpricepanel3.isState()){
            SellPrice sellprice = new SellPrice();
            sellprice.setItem(item);
            sellprice.setPrice(quantitysellpricepanel3.getPriceTextFieldDecimalValue());
            sellprice.setQuantity(quantitysellpricepanel3.getQuantityTextFieldIntegerValue());
            
            sellpricelist.add(sellprice);
        }
        
        if(quantitysellpricepanel4.isState()){
            SellPrice sellprice = new SellPrice();
            sellprice.setItem(item);
            sellprice.setPrice(quantitysellpricepanel4.getPriceTextFieldDecimalValue());
            sellprice.setQuantity(quantitysellpricepanel4.getQuantityTextFieldIntegerValue());
            
            sellpricelist.add(sellprice);
        }
        
        if(quantitysellpricepanel5.isState()){
            SellPrice sellprice = new SellPrice();
            sellprice.setItem(item);
            sellprice.setPrice(quantitysellpricepanel5.getPriceTextFieldDecimalValue());
            sellprice.setQuantity(quantitysellpricepanel5.getQuantityTextFieldIntegerValue());
            
            sellpricelist.add(sellprice);
        }
        
        return sellpricelist;
    }
    
    private List<BuyPrice> getBuyPriceList(){
        List <BuyPrice> buypricelist = new ArrayList<>();
        
        if(quantitybuypricepanel2.isState()){
            BuyPrice buyprice = new BuyPrice();
            buyprice.setItem(item);
            buyprice.setPrice(quantitybuypricepanel2.getPriceTextFieldDecimalValue());
            buyprice.setQuantity(quantitybuypricepanel2.getQuantityTextFieldIntegerValue());
            
            buypricelist.add(buyprice);
        }
        
        if(quantitybuypricepanel3.isState()){
            BuyPrice buyprice = new BuyPrice();
            buyprice.setItem(item);
            buyprice.setPrice(quantitybuypricepanel3.getPriceTextFieldDecimalValue());
            buyprice.setQuantity(quantitybuypricepanel3.getQuantityTextFieldIntegerValue());
            
            buypricelist.add(buyprice);
        }
        
        if(quantitybuypricepanel4.isState()){
            BuyPrice buyprice = new BuyPrice();
            buyprice.setItem(item);
            buyprice.setPrice(quantitybuypricepanel4.getPriceTextFieldDecimalValue());
            buyprice.setQuantity(quantitybuypricepanel4.getQuantityTextFieldIntegerValue());
            
            buypricelist.add(buyprice);
        }
        
        if(quantitybuypricepanel5.isState()){
            BuyPrice buyprice = new BuyPrice();
            buyprice.setItem(item);
            buyprice.setPrice(quantitybuypricepanel5.getPriceTextFieldDecimalValue());
            buyprice.setQuantity(quantitybuypricepanel5.getQuantityTextFieldIntegerValue());
            
            buypricelist.add(buyprice);
        }
        
        return buypricelist;
    }
}

