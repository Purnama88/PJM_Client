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
public class DeliveryDetail extends InvoiceDetailPanel{

    public DeliveryDetail(int deliveryid) {
        super(GlobalFields.PROPERTIES.getProperty("PANEL_DELIVERY_DETAIL"));
    }

    
}