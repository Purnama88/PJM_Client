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
public class Role extends Nontransactional{
    
    private String name;
    
    private boolean defaultrole;
    
    private boolean dateforward;
    private boolean datebackward;
    private boolean raisebuyprice;
    private boolean raisesellprice;
    private boolean lowerbuyprice;
    private boolean lowersellprice;
    
    @Override
    public String toString(){
        return getName();
    }
}
