/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.purnama.pjm_client.model.Nontransactional;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Partner extends Nontransactional{
    
    private String code;
    
    private String name;
    
    private String contactname;
    
    private String address;
    
    private String phonenumber;
    
    private String phonenumber2;
    
    private String faxnumber;
    
    private String faxnumber2;
    
    private String mobilenumber;
    
    private String mobilenumber2;
    
    private String email;
    
    private String email2;
    
    private double balance;
    
    private double maximumdiscount;
    
    private double maximumbalance;
    
    private int paymentdue;
    
    private boolean customer;
    
    private boolean supplier;
    
    private boolean nontrade;
    
    Set<PartnerGroup> partnergroups = new HashSet<>();
    
    @Override
    public String toString(){
        return getName();
    }
    
//    public String getFormattedMaximumdiscount(){
//        if(getMaximumdiscount() >= 0){
//            return GlobalFields.NUMBERFORMAT.format(getMaximumdiscount());
//        }
//        else{
//            return GlobalFields.PROPERTIES.getProperty("LABEL_UNLIMITED");
//        }
//    }
//    
//    public String getFormattedMaximumbalance(){
//        if(getMaximumbalance() >= 0){
//            return GlobalFields.NUMBERFORMAT.format(getMaximumbalance());
//        }
//        else{
//            return GlobalFields.PROPERTIES.getProperty("LABEL_UNLIMITED");
//        }
//    }
    
//    public String getFormattedBalance(){
//        
//        if(partnertype.getParent() == PartnerTypeEntity.CUSTOMER){
//            if(getBalance() < 0){
//                
//                return "<HTML><FONT COLOR=RED>" + 
//                        GlobalFields.NUMBERFORMAT.format(Math.abs(getBalance())) + 
//                        "</FONT></HTML>";
//            }
//            else{
//                return GlobalFields.NUMBERFORMAT.format(getBalance());
//            }
//        }
//        else{
//            if(getBalance() < 0){
//                return GlobalFields.NUMBERFORMAT.format(Math.abs(getBalance()));
//            }
//            else{
//                return "<HTML><FONT COLOR=RED>" + 
//                        GlobalFields.NUMBERFORMAT.format(getBalance()) + 
//                        "</FONT></HTML>";
//            }
//        }
//    }
}
