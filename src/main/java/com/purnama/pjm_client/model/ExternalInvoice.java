/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ExternalInvoice extends Transactional{
    
    protected Date duedate;
    
    protected String currencycode;
    
    protected String currencyname;
    
    protected String partnercode;
    
    protected String partnername;
    
    protected String partneraddress;
    
    protected double subtotal;
    
    protected double discount;
    
    protected double tax;
    
    protected double freight;
    
    protected double rounding;
    
    protected double paid;
    
    protected double rate;
    
    protected Partner partner;
    
    protected Currency currency;
    
    protected double remaining;
    
    protected double totalbeforetax;
    
    protected double totalaftertax;
    
    protected double totaldefaultcurrency;
    
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
