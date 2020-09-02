/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.gui.library.MyButton;
import com.purnama.pjm_client.gui.library.MyFrame;
import com.purnama.pjm_client.gui.library.MyImageIcon;
import com.purnama.pjm_client.gui.library.MyLabel;
import com.purnama.pjm_client.gui.library.MyPanel;
import com.purnama.pjm_client.gui.library.MyPasswordField;
import com.purnama.pjm_client.gui.library.MyTextField;
import com.purnama.pjm_client.gui.main.MainFrame;
import com.purnama.pjm_client.login.util.WarehouseComboBoxPanel;
import com.purnama.pjm_client.model.nontransactional.Login;
import com.purnama.pjm_client.model.nontransactional.User;
import com.purnama.pjm_client.model.nontransactional.Warehouse;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.util.GlobalFields;
import com.purnama.pjm_client.util.GlobalFunctions;
import com.sun.jersey.api.client.ClientResponse;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 *
 * @author p_cor
 */
public class LoginFrame extends MyFrame implements ActionListener{
    
    private final Box box;
    
    private final MyPanel usernamepanel, passwordpanel, buttonpanel, logopanel,
            actionpanel, languagepanel;
    
    private final MyButton loginbutton, helpbutton, refreshbutton;
    
    private final MyLabel logolabel;
    
    private final MyTextField textfield;
    
    private final MyPasswordField passwordfield;
    
    private final WarehouseComboBoxPanel warehousepanel;
    
    
    public LoginFrame(){
        super("Purnomo Jaya Mobil");
        
        actionpanel = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        
        languagepanel = new MyPanel(new FlowLayout(FlowLayout.LEFT));
        languagepanel.setPreferredSize(new Dimension(300, 30));
        
        logopanel = new MyPanel();
        
        helpbutton = new MyButton(new MyImageIcon().getImage("image/Help_24.png"), 35, 35);
        helpbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("LABEL_HELP"));
        refreshbutton = new MyButton(new MyImageIcon().getImage("image/Refresh_24.png"), 35, 35);
        refreshbutton.setToolTipText(GlobalFields.PROPERTIES.getProperty("LABEL_REFRESH"));
        
        logolabel = new MyLabel(new MyImageIcon().getImage("image/Login_128.png"));
        
        warehousepanel = new WarehouseComboBoxPanel();
        
        usernamepanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        textfield = new MyTextField("", 150);
        
        passwordpanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        passwordfield = new MyPasswordField("", 150);
        
        buttonpanel = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        loginbutton = new MyButton(GlobalFields.PROPERTIES.getProperty("LABEL_SUBMIT"));
        
        buttonpanel.add(loginbutton);
        
        box = Box.createVerticalBox();
        
        init();
    }
    
    public final void init(){
        setMinimumSize(new Dimension(400, 350));
        
        setLocationToCenter();
        
        actionpanel.add(languagepanel);
        
        actionpanel.add(refreshbutton);
        actionpanel.add(helpbutton);
        
        logopanel.add(logolabel);
        
        textfield.addActionListener(this);
        usernamepanel.add(new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_USERNAME")));
        usernamepanel.add(textfield);
        
        passwordfield.addActionListener(this);
        passwordpanel.add(new MyLabel(GlobalFields.PROPERTIES.getProperty("LABEL_PASSWORD")));
        passwordpanel.add(passwordfield);
        
        loginbutton.addActionListener(this);
        
        box.add(actionpanel);
        box.add(logopanel);
        box.add(warehousepanel);
        box.add(usernamepanel);
        box.add(passwordpanel);
        box.add(buttonpanel);
        
        add(box);
        
        setResizable(false);
        display();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationToCenter();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
              textfield.requestFocusInWindow();
            }
        });
        
        refreshbutton.addActionListener((ActionEvent e) -> {
            warehousepanel.load();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker<Boolean, String> numworker = new SwingWorker<Boolean, String>() {
            ClientResponse response;
        
            @Override
            protected Boolean doInBackground() throws Exception{
                Warehouse warehouse = warehousepanel.getComboBoxValue();
                Login le = new Login();
                le.setUsername(textfield.getText());
                le.setPassword(GlobalFunctions.encrypt(passwordfield.getText()));
                le.setWarehouseid(warehouse.getId());
                
                response = RestClient.postNonApi("login", le);
                
                return true;
            }
            
            @Override
            protected void done() {
                if(response == null){
                    passwordfield.setText("");
                    JOptionPane.showMessageDialog(null, GlobalFields.
                            PROPERTIES.getProperty("NOTIFICATION_TIMEDOUT"), "",
                            JOptionPane.ERROR_MESSAGE);
                }
                else if(response.getStatus() != 200) {
                    passwordfield.setText("");
                    String output = response.getEntity(String.class);
                    JOptionPane.showMessageDialog(null, 
                            output, GlobalFields.PROPERTIES.getProperty("NOTIFICATION_HTTPERROR")
                                            + response.getStatus(), 
                            JOptionPane.ERROR_MESSAGE);

                }
                else{
                    String output = response.getEntity(String.class);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        GlobalFields.USER = mapper.readValue(output, User.class);
                        GlobalFields.ROLE = GlobalFields.USER.getRole();
                        String header = response.getHeaders().getFirst("Authorization");
                        String authToken = header.substring(7);
                        GlobalFields.TOKEN = authToken;
                    } catch (IOException ex) {
                        Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    SwingUtilities.invokeLater(() -> {
                        MainFrame mainFrame = new MainFrame();
                        GlobalFields.MAINFRAME = mainFrame;
                    });

                    dispose();
                }
            }
        };
        
        numworker.execute();
    }
    
}
