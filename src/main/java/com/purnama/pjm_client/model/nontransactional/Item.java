/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.Nontransactional;
import com.purnama.pjm_client.util.GlobalFields;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Item extends Nontransactional{
    
    private String code;
    
    private String name;
    
    private String description;
    
    private double cost;
    
    private Label label;
    
    private double sellprice;
    
    private double buyprice;
    
    private boolean bulksellprice;
    
    private boolean bulkbuyprice;
    
    Set<ItemGroup> itemgroups = new HashSet<>();
    
    Set<Model> models = new HashSet<>();
    
    @JsonIgnore
    public String getFormattedCost(){
        return GlobalFields.NUMBERFORMAT.format(getCost());
    }
}
