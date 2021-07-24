/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.transactional;

import com.purnama.pjm_client.model.ExternalItemInvoice;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ItemInvoicePurchase extends ExternalItemInvoice{
    private InvoicePurchase invoicepurchase;
}
