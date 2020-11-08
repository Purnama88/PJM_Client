/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.purnama.pjm_client.model.nontransactional.Item;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Price {
    
    private int id;
            
    private double quantity;
    
    private double price;
   
    private Item item;
}
