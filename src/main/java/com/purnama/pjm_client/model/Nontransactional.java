/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import lombok.Data;
/**
 *
 * @author p_cor
 */

@Data
public class Nontransactional implements Serializable{
    
    protected int id;
    
    protected boolean status;
    
    protected String note;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Date createddate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Date lastmodified;
    
    @JsonIgnore
    public String getFormattedLastmodified(){
        
        DateFormat dateformat = new SimpleDateFormat ("dd MMM YYYY HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("UCT+7"));
        return dateformat.format(getLastmodified());
    }
    
    @JsonIgnore
    public String getFormattedCreateddate(){
        
        DateFormat dateformat = new SimpleDateFormat ("dd MMM YYYY HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("UCT+7"));
        return dateformat.format(getCreateddate());
    }
}
