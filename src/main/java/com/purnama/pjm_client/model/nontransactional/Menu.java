/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.purnama.pjm_client.model.Nontransactional;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Menu extends Nontransactional{
    
    public static int MENU_INVOICEPURCHASE = 1;
    public static int MENU_INVOICESALES = 2;
    public static int MENU_RETURNPURCHASE = 3;
    public static int MENU_RETURNSALES = 4;
    public static int MENU_ITEMADJUSTMENT = 5;
    
    public static int MENU_DELIVERY = 6;
    public static int MENU_EXPENSES = 7;
    public static int MENU_INVOICEWAREHOUSE = 8;
    public static int MENU_INCOMINGPAYMENT = 9;
    public static int MENU_OUTGOINGPAYMENT = 10;
    
    public static int MENU_REPORT = 11;
    
            //"Invoice Purchase",
//        "Invoice Sales",
//        
//        "Return Purchase",
//        "Return Sales",
//        
//        "Item Adjustment",
//        "Delivery",
//        
//        "Expenses",
//        "Invoice Warehouse",
//        
//        "Incoming Payment",
//        "Outgoing Payment",
//        
//        "Report",
            
    private String name;
    
    @Override
    public String toString(){
        return getName();
    }
}
