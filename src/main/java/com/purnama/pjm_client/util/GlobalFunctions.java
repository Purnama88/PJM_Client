/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.util;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

/**
 *
 * @author p_cor
 */
public class GlobalFunctions {
    
    public static double round(double value){
        DecimalFormat df;
        
        if(GlobalFields.DECIMALPLACES == 3){
            df = new DecimalFormat("#.###");
        }
        else if(GlobalFields.DECIMALPLACES == 4){
            df = new DecimalFormat("#.####");
        }
        else{
            df = new DecimalFormat("#.##");
        }
        
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        return GlobalFunctions.convertToDouble(df.format(value));
    }
    
    public static String encrypt(String x) throws Exception {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return Arrays.toString(d.digest());
    }
    
    public static String toSuperscript(String include, String exclude){
        return "<HTML>" + 
                exclude + " " +
                "<SUP>" + include + "</SUP></HTML>";
    }
    
    public static double convertToDouble(String value){
        try{
            NumberFormat format = DecimalFormat.getNumberInstance();
            Number number = format.parse(value);
            double d = number.doubleValue();
            
            return d;
        }
        catch(ParseException e){
            return 0;
        }
    }
    
    public static int convertToInteger(String value){
        try{
            NumberFormat format = DecimalFormat.getNumberInstance();
            Number number = format.parse(value);
            int d = number.intValue();
            
            return d;
        }
        catch(ParseException e){
            return 0;
        }
    }
    
}
