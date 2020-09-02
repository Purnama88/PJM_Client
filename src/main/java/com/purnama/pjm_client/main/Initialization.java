/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.main;

import com.purnama.pjm_client.gui.main.MainFrame;
import com.purnama.pjm_client.login.LoginFrame;
import com.purnama.pjm_client.util.GlobalFields;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author p_cor
 */
public class Initialization {
   
    public Initialization(){
        initLanguage();
        initGUI();
    }
    
    public final void initGUI(){
        SwingUtilities.invokeLater(() -> {
//            MainFrame mainframe = new MainFrame();
            LoginFrame loginframe = new LoginFrame();
        });
    }
    
    public final void initLanguage(){
        Properties properties = new Properties();
	InputStream input = null;

	try{
            
            input = getClass().getResourceAsStream("/language/English.properties");
            properties.load(input);
            
            GlobalFields.PROPERTIES = properties;
	}
        catch (IOException ex){
            ex.printStackTrace();
            
            JOptionPane.showMessageDialog(null, "Cannot find any configuration file", "Error", JOptionPane.ERROR_MESSAGE);
	}
        finally{
            if (input != null){
                try{
                    input.close();
                }
                catch (IOException e) {
                }
            }
	}
    }
}
