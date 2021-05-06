/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.util;

import com.purnama.pjm_client.gui.main.MainFrame;
import com.purnama.pjm_client.gui.main.MainTabbedPane;
import com.purnama.pjm_client.model.nontransactional.Role;
import com.purnama.pjm_client.model.nontransactional.User;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 *
 * @author p_cor
 */
public class GlobalFields {
    
    public static boolean SUCCESS = true;
    public static boolean FAIL = false;
    
    public static MainFrame MAINFRAME;
    public static MainTabbedPane MAINTABBEDPANE;
    
    public static Properties PROPERTIES;
    
    public static String SERVER = "http://localhost:8080/purnomojayamobil/";
    public static String API = "api/";
    public static String VERSION = "v1/";
    
    public static int TIMEOUT = 5000; //miliseconds
    
    public static int ITEM_PER_PAGE = 20;
    public static int MINIMAL_CHARACTER_ON_SEARCH = 5;
    
    public static String TOKEN;
    public static User USER;
    public static Role ROLE;
    
    public static String LABEL_REMARK_1 = "1";
    public static String LABEL_REMARK_2 = "2";
    public static String LABEL_REMARK_3 = "3";
    
    public static NumberFormat NUMBERFORMAT = DecimalFormat.getNumberInstance();
    public  DateFormat DATEFORMAT = new SimpleDateFormat ("dd MMM YYYY");
    
    public static int DECIMALPLACES = 2;
}
