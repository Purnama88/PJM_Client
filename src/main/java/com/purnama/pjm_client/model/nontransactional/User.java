/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.Nontransactional;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class User extends Nontransactional{
    
    private String username;
    
    private String password;
    
    private String code;
    
    private String name;
    
    private double maximumdiscount;
    
    private String email;
    
    private Role role;
  
    private Set<Warehouse> warehouses;
    
    @JsonIgnore
    public String getFormattedDiscount(){
        if(getMaximumdiscount() >= 0){
            return GlobalFields.NUMBERFORMAT.format(getMaximumdiscount()) + "%";
        }
        else{
            return "~";
        }
    }
}
