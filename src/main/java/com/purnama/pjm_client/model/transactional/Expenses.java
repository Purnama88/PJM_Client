/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.InternalInvoice;
import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.TimeZone;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Expenses extends InternalInvoice{
    
    protected LocalDateTime duedate;
    
    private Currency currency;
    
    private double rate;
    
    private double subtotal;
    
    private double discount;
    
    private double tax;
    
    private double freight;
    
    private double rounding;
    
    private Partner partner;
    
    private String currencycode;
    
    private String currencyname;
    
    private String partnername;
    
    private String partnercode;
    
    private String partneraddress;
    
    private double paid;
    
    private double remaining;

    private double totalbeforetax;
    
    private double totalaftertax;
    
    private double totaldefaultcurrency;
    
    @JsonIgnore
    public String getFormattedDuedate(){
        
        DateFormat dateformat = new SimpleDateFormat ("dd MMM YYYY HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("UCT+7"));
        return dateformat.format(getDuedate());
    }
    
    @JsonIgnore
    public double getDiscount_percentage() {
        return GlobalFunctions.round(getDiscount()/getSubtotal() * 100);
    }

    @JsonIgnore
    public double getTotal_before_tax() {
        return GlobalFunctions.round(getSubtotal() - getDiscount() - getRounding() + getFreight());
    }

    @JsonIgnore
    public double getTax_percentage() {
        return GlobalFunctions.round(getTax()/getTotal_before_tax() *100);
    }

    @JsonIgnore
    public double getTotal_after_tax() {
        return GlobalFunctions.round(getTotal_before_tax() + getTax());
    }

    @JsonIgnore
    public String getFormattedTotal_after_tax(){
        return GlobalFields.NUMBERFORMAT.format(getTotal_after_tax());
    }
    
    @JsonIgnore
    public double getTotal_defaultcurrency() {
        return GlobalFunctions.round(getTotal_after_tax() * getRate());
    }
}
