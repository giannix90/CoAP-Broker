package com.coap.server.main;

/*******************************************************************************
 *
 * Version 0.1
 * 
 ******************************************************************************/

import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;

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
        add(new PublishResource());
    }

    /**
     * The Class PublishResource.
     */
    class PublishResource extends CoapResource {

        
        /**
         * Instantiates a new publish resource.
         */
        public PublishResource() {
        	
            // set resource identifier
            super("temperatura");
            
            // set display name
            getAttributes().setTitle("Risorsa Temperatura");
        }

       /* 
		*	This is the listener for the POST request (Is called automatically when an incoming POST req arrives)
        */     
        public void handlePOST(CoapExchange exchange) {  
        	
        	System.out.println("POST request received");
        	
            System.out.println("I send the message"+exchange.getRequestText());          
            exchange.respond("POST_REQUEST_SUCCESS");           
        }
        
        /* 
         * Listener for GET request
         */
        public void handleGET(CoapExchange exchange) {
        	
        	System.out.println("POST request received");
        	
            exchange.respond("GET_REQUEST_SUCCESS");            
        }
    }

}