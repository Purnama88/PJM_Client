/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.util.GlobalFields;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ExternalItemInvoice {
    protected int id;
    
    protected Item item;
    
    protected String description;
    
    protected double quantity;
    
    protected double price;
    
    protected double discount;
    
    protected String box;
    
    @JsonIgnore
    public double getSubtotal() {
        return getQuantity() * getPrice();
    }
    
    @JsonIgnore
    public double getDiscount_percentage() {
        return getDiscount() / getSubtotal() * 100;
    }
    
    @JsonIgnore
    public double getTotal() {
        return getSubtotal() - getDiscount();
    }
    
    @JsonIgnore
    public String getFormattedPrice(){
        return GlobalFields.NUMBERFORMAT.format(getPrice());
    }
    
    @JsonIgnore
    public String getFormattedDiscount(){
        return GlobalFields.NUMBERFORMAT.format(getDiscount());
    }
    
    @JsonIgnore
    public String getFormattedDiscount_percentage(){
        return "(" + GlobalFields.NUMBERFORMAT.format(getDiscount_percentage()) + "%)";
    }
    
    @JsonIgnore
    public String getFormattedTotal(){
        return GlobalFields.NUMBERFORMAT.format(getTotal());
    }
    
    @JsonIgnore
    public String getFormattedQuantity(){
        return GlobalFields.NUMBERFORMAT.format(getQuantity());
    }
}
