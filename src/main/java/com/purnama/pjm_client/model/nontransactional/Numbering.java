/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.model.nontransactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purnama.pjm_client.model.Nontransactional;
import lombok.Data;

/**
 *
 * @author p_cor
 */
@Data
public class Numbering extends Nontransactional{
    
    private String name;
    
    private String prefix;
    
    private int start;
    
    private int end;
    
    private int current;
    
    private Menu menu;
    
    @Override
    public String toString(){
        String x = "";
        for(int i = 0; i < getLength(); i++){
            x += "X";
        }
        
        return getPrefix() +  x; 
    }
    
    @JsonIgnore
    public String getFormat(){
        return "%0" + getLength() + "d";
    }
    
    @JsonIgnore
    public int getLength(){
        try{
            return String.valueOf(getEnd()).length();
        }
        catch(Exception e){
            return 1;
        }
    }
    
    @JsonIgnore
    public String getFormattedStart(){
        return String.format(getFormat(), getStart());
    }
    
    @JsonIgnore
    public String getFormattedEnd(){
        return String.format(getFormat(), getEnd());
    }
    
    @JsonIgnore
    public String getFormattedCurrent(){
        return String.format(getFormat(), getCurrent());
    }
    
}
