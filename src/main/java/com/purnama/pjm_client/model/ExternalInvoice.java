/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.purnama.pjm_client.model.nontransactional.Currency;
import com.purnama.pjm_client.model.nontransactional.Partner;
import java.util.Date;
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
}
