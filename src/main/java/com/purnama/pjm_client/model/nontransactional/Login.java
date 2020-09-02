/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Login implements Serializable{
    
    private String username;
    private String password;
    private int warehouseid;
}