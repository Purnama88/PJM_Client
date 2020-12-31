/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.purnama.pjm_client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purnama.pjm_client.login.LoginFrame;
import com.purnama.pjm_client.util.GlobalFields;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
//import net.purnama.gui.login.LoginFrame;
//import net.purnama.util.GlobalFields;

/**
 *
 * @author p_cor
 */
public class RestClient {
    
    public static ClientResponse getNonApi(String url){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + url);
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource webResource = client
               .resource(GlobalFields.SERVER + url);
            
            WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);
            
            response = builder
               .get(ClientResponse.class);
            
            return response;
        }
        catch(ClientHandlerException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ClientResponse get(String url){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource webResource = client
               .resource(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            
            WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);
            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + GlobalFields.TOKEN);
            
            response = builder
               .get(ClientResponse.class);
            
            if(response.getStatus() == 401){
                try{
                    GlobalFields.MAINFRAME.dispose();
                    GlobalFields.TOKEN = "";
                    GlobalFields.ROLE = null;
                    GlobalFields.USER = null;
                    LoginFrame loginframe = new LoginFrame();
                }
                catch(Exception e){
                    
                }
            }
            else{
                String header = response.getHeaders().getFirst("Authorization");
                String authToken = header.substring(7);
                GlobalFields.TOKEN = authToken;
            }
            
        }
        catch(ClientHandlerException ex){
            ex.printStackTrace();
        }
        
        return response;
    }
    
    public static ClientResponse postNonApi(String url, Object object){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + url);
            ObjectMapper mapper = new ObjectMapper();
            String input = mapper.writeValueAsString(object);
            
            System.out.println(input);
            
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource resource = client.
                    resource(GlobalFields.SERVER + url);
            
            WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);

            response =  builder.post(ClientResponse.class, input);
            
            
        }
        catch(RuntimeException | IOException e){
            e.printStackTrace();
        }
        
        return response;
    }
    
    public static ClientResponse post(String url, Object object){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            ObjectMapper mapper = new ObjectMapper();
            String input = mapper.writeValueAsString(object);
            
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource resource = client.
                    resource(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            
            WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);
            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + GlobalFields.TOKEN);

            response =  builder.post(ClientResponse.class, input);
            
            if(response.getStatus() == 401){
                try{
                    GlobalFields.MAINFRAME.dispose();
                    GlobalFields.TOKEN = "";
                    GlobalFields.ROLE = null;
                    GlobalFields.USER = null;
                    LoginFrame loginframe = new LoginFrame();
                }
                catch(Exception e){
                    
                }
            }
            else{
                String header = response.getHeaders().getFirst("Authorization");
                String authToken = header.substring(7);
                GlobalFields.TOKEN = authToken;
            }
        }
        catch(RuntimeException | IOException e){
            e.printStackTrace();
        }
        
        return response;
    }
    
    public static ClientResponse put(String url, Object object){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            ObjectMapper mapper = new ObjectMapper();
            String input = mapper.writeValueAsString(object);
            
            System.out.println(input);
            
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource resource = client.
                    resource(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            
            WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);
            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + GlobalFields.TOKEN);

            response =  builder.put(ClientResponse.class, input);
            
            if(response.getStatus() == 401){
                try{
                    GlobalFields.MAINFRAME.dispose();
                    GlobalFields.TOKEN = "";
                    GlobalFields.ROLE = null;
                    GlobalFields.USER = null;
                    LoginFrame loginframe = new LoginFrame();
                }
                catch(Exception e){
                    
                }
            }
            else{
                String header = response.getHeaders().getFirst("Authorization");
                String authToken = header.substring(7);
                GlobalFields.TOKEN = authToken;
            }
        }
        catch(RuntimeException | IOException e){
            e.printStackTrace();
        }
        
        return response;
    }
    
    public static ClientResponse delete(String url, Object object){
        ClientResponse response = null;
        
        try{
            System.out.println(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            ObjectMapper mapper = new ObjectMapper();
            String input = mapper.writeValueAsString(object);
            
            System.out.println(input);
            
            Client client = Client.create();
            client.setConnectTimeout(GlobalFields.TIMEOUT);
            client.setReadTimeout(GlobalFields.TIMEOUT);
            
            WebResource resource = client.
                    resource(GlobalFields.SERVER + GlobalFields.API + GlobalFields.VERSION  + url);
            
            WebResource.Builder builder = resource.accept(MediaType.APPLICATION_JSON);
            builder.type(MediaType.APPLICATION_JSON);
            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + GlobalFields.TOKEN);

            response =  builder.delete(ClientResponse.class, input);
            
            if(response.getStatus() == 401){
                try{
                    GlobalFields.MAINFRAME.dispose();
                    GlobalFields.TOKEN = "";
                    GlobalFields.ROLE = null;
                    GlobalFields.USER = null;
                    LoginFrame loginframe = new LoginFrame();
                }
                catch(Exception e){
                    
                }
            }
            else{
                String header = response.getHeaders().getFirst("Authorization");
                String authToken = header.substring(7);
                GlobalFields.TOKEN = authToken;
            }
        }
        catch(RuntimeException | IOException e){
            e.printStackTrace();
        }
        
        return response;
    }
    
}
