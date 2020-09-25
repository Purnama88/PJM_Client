/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.nontransactional.Numbering;
import com.purnama.pjm_client.model.nontransactional.User;
import com.purnama.pjm_client.model.nontransactional.Warehouse;
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
public class InternalInvoiceDraft extends Nontransactional{
    
    protected String draftid;
    
    protected User user;
    
    protected Numbering numbering;
    
    protected Warehouse warehouse;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Date transactiondate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Date invoicedate;
    
    @JsonIgnore
    public String getFormattedTransactiondate(){
        
        DateFormat dateformat = new SimpleDateFormat ("dd MMM YYYY HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("UCT+7"));
        return dateformat.format(getTransactiondate());
    }
    
    @JsonIgnore
    public String getFormattedInvoicedate(){
        
        DateFormat dateformat = new SimpleDateFormat ("dd MMM YYYY HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("UCT+7"));
        return dateformat.format(getInvoicedate());
    }
}
