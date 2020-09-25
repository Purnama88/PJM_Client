/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.transactional.draft;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.InternalInvoiceDraft;
import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ExpensesDraft extends InternalInvoiceDraft{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Date duedate;
    
    private double subtotal;
    
    private double discount;
    
    private double tax;
    
    private double freight;
    
    private double rounding;
    
    private double rate;
    
    private Currency currency;
    
    private Partner partner;
    
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
