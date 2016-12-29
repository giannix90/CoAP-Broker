package com.coap.server.main;

import java.net.SocketException;
/*******************************************************************************
 *
 * Version 0.1
 * 
 ******************************************************************************/
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;

/**
 * The Class CoAPServer Brokers.
 * @author Gianni Pollina
 */
public class Server extends CoapServer {
	
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {        
        
        try {
        	
            // create new instance server of this class
            Server server = new Server();
            server.start();
           
            
        } catch (SocketException e) {
        	//Handle the error
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }
    
   
    /**
     * Instantiates a new CoAP server.
     *
     * @throws SocketException the socket exception
     */
    public Server() throws SocketException {
    	/*Constructor*/

        // provide an instance of a resource
        add(new PublishResource("ps","Radice").add(new PublishResource("Temp","Temperratura")));
    }

    /**
     * The Class PublishResource.
     */
    class PublishResource extends CoapResource {

        
        /**
         * Instantiates a new publish resource.
         */
        public PublishResource(String ri,String title) {
        	
        	/*This is a resource example*/
        	
            // set resource identifier
            super(ri);
            
            // set display name
            getAttributes().setTitle(title);
            getAttributes().setAttribute("rt", "temperature");
            getAttributes().setAttribute("if", "sensor");
            getAttributes().setAttribute("ct", "40");
            getAttributes().setObservable();
        }

        
        /**
         * The Class CoapExchange represents an exchange of a CoAP request and response
         * and provides a user-friendly API to subclasses of {@link CoapResource} for
         * responding to requests.
         */
        
        /**
         * When a request arrives, the request will be handled by the resource's executor.
         */
        
       /* 
		*	This is the listener for the POST request (Is called automatically when an incoming POST req arrives)
        */
        @Override
        public void handlePOST(CoapExchange exchange) {  
        	
        	System.out.println("POST request received");
        	
            System.out.println("I send the message"+exchange.getRequestText());          
            exchange.respond("POST_REQUEST_SUCCESS");           
        }
        
        /* 
         * Listener for GET request
         */
        @Override
        public void handleGET(CoapExchange exchange) {
        	
        	/*Implement get responce*/
        	
        	System.out.println("GET request"
        			+exchange.getRequestOptions().getURIPathString());
        	
        	//return the number of queries
        	System.out.println("Query count : "
        			+exchange.getRequestOptions().getURIQueryCount());
        	
        	for(int i=0;i<exchange.getRequestOptions().getURIQueryCount();i++){
	        
        		//return the list of queries
	        	System.out.println("Query-"+i+" : "
	        			+exchange.getRequestOptions().getURIQueries().get(i));
	        }
        	
        	//I want the list of child
        	Collection<Resource> listOfChild=getChildren();
        	
        	Iterator<Resource> i=listOfChild.iterator();
        	
        	while(i.hasNext()){
        	
        		Resource r=i.next();
        		exchange.respond("<"+r.getPath()+r.getName()+">;"+"rt=\""+r.getAttributes().getAttributeValues("rt").get(0)+"\";"
        				+"if=\""+r.getAttributes().getAttributeValues("if").get(0)+"\";"
        				+"ct=\""+r.getAttributes().getAttributeValues("ct").get(0)+"\";"
        				);            
        		
        	}
        }
        
        
        public void handlePUT(CoapExchange exchange) {
        	 
        	exchange.respond("PUT");
        	changed(); // notify all observers
        }
        	  
        
        public void handleDELETE(CoapExchange exchange) {
        	
        	delete();
        	exchange.respond("DELETE");
        }
    }

}