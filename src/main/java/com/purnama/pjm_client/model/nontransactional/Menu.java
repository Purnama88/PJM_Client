/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.purnama.pjm_client.model.Nontransactional;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Menu extends Nontransactional{
    
    private String name;
    
    @Override
    public String toString(){
        return getName();
    }
}
