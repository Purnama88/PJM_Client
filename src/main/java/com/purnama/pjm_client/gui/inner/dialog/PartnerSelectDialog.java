/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.gui.inner.dialog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.purnama.pjm_client.model.nontransactional.Partner;
import com.purnama.pjm_client.rest.RestClient;
import com.purnama.pjm_client.tablemodel.PartnerTableModel;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author p_cor
 */
public class PartnerSelectDialog extends SelectDialog{
    
    private final PartnerTableModel partnertablemodel;
    
    private ArrayList<Partner> list;
    
    public PartnerSelectDialog() {
        super("");
        
        partnertablemodel = new PartnerTableModel();
        
        list = new ArrayList<>();
        
        init();
    }
    
    private void init(){
        
        table.setModel(partnertablemodel);
        
    }
    
    @Override
    protected void load(){
        String keyword = getSearchKeyword();
        
        if(keyword.length() >= GlobalFields.MINIMAL_CHARACTER_ON_SEARCH){
            SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

                ClientResponse response;

                @Override
                protected Boolean doInBackground(){

                    response = RestClient.get("partners?keyword=" + keyword);

                    return true;
                }

                @Override
                protected void process(List<String> chunks) {

                }

                @Override
                protected void done() {
                    if(response == null){
                    }
                    else if(response.getStatus() != 200) {
                    }
                    else{
                        String output = response.getEntity(String.class);

                        ObjectMapper mapper = new ObjectMapper();


                        try{
                            list = mapper.readValue(output,
                                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
                                            Partner.class));
                            partnertablemodel.setPartnerList(list);
                        }
                        catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
            };
            worker.execute();
        }
    }
    
    @Override
    public Partner showDialog(){
        setVisible(true);
        
        if(table.getSelectedRow() != -1){
        
            return partnertablemodel.getPartner(table.
                        convertRowIndexToModel(table.getSelectedRow()));
        }
        
        return null;
    }
}
