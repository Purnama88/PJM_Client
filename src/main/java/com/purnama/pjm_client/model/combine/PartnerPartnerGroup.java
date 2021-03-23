/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.combine;

import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.model.nontransactional.PartnerGroup;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class PartnerPartnerGroup {
    private int id;

    private PartnerGroup partnergroup;

    private Partner partner;
}
