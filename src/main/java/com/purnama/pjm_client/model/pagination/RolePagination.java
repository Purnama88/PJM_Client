/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.pagination;

import com.purnama.pjm_client.model.nontransactional.Role;
import java.util.List;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class RolePagination extends Pagination{
    
    private List<Role> roles;
}

