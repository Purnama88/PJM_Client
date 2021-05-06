/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.detail;

import com.purnama.pjm_client.util.GlobalFields;

/**
 *
 * @author p_cor
 */
public class InvoiceSalesDetail extends InvoiceDetailPanel{

    public InvoiceSalesDetail(int invoiceid) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_INVOICESALES_DETAIL"));
    }

    
}
