/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.combine;

import com.purnama.pjm_client.model.nontransactional.Item;
import com.purnama.pjm_client.model.nontransactional.Model;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class ItemModel {
    private int id;

    private Model model;

    private Item item;
}