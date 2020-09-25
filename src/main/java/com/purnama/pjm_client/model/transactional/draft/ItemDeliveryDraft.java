/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.transactional.draft;

import com.purnama.pjm_client.model.InternalItemInvoiceDraft;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ItemDeliveryDraft extends InternalItemInvoiceDraft{
    
    private String quantity;
    
    private String remark;
    
    private DeliveryDraft deliverydraft;
    
}
