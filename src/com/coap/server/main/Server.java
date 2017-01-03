package com.coap.server.main;

import java.net.SocketException;
/*******************************************************************************
 *
 * Version 0.1
 * 
 * This Coap Broker Accept only the request with content format 40 (application/link-format) 
 * 
 ******************************************************************************/
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import com.coap.server.main.BrokerUtility;
/**
 * The Class CoAPServer Brokers.
 * 
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
        add(new PublishResource("ps","Radice","","","",false).add(new PublishResource("Temp","Temp","temperature-c","sensor","40",true)).add(new PublishResource("ligth","ligth","ligth-lux","sensor","40",true)));
    }

    /**
     * The Class PublishResource.
     */
    class PublishResource extends CoapResource {

        private int mValue=30; //This is the value of the resource
        
        public void setValue(int value){
        	this.mValue=value;
        }
        
        public int getValue(){
        	return(this.mValue);
        }
        
        /**
         * Instantiates a new publish resource.
         */
        public PublishResource(String ri,String title,String rt,String if1,String ct,boolean obs) {
        	
        	/*This is a resource example*/
        	
            // set resource identifier
            super(ri);
            
            // set display name
            getAttributes().setTitle(title);
      
            if(rt.length()>0)
            	getAttributes().setAttribute("rt", rt);
            if(if1.length()>0)
            	getAttributes().setAttribute("if", if1);
            if(ct.length()>0)
            	getAttributes().setAttribute("ct", ct);
            if(obs)
            	setObservable(true);
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
        	
        		String measurement = exchange.getRequestText().trim();
	        	
        		//This are the index of ; and = respectively
	        	int iend= measurement.indexOf(";");
	        	int iend2= measurement.indexOf("=");
	        	
	        	if(iend != -1 && iend2!=-1){
		        
	        		//This case is for bad request => when not appear ; and =
	        		
		        	String nameOfResource = measurement.substring(0, iend);
		        	
		        	//I take the list of child resources
		        	Collection<Resource> listOfChild=getChildren();
		        	Iterator<Resource> i=listOfChild.iterator();
		        	
		        	while(i.hasNext()){
			        	
		        		//I control if the resource already exist
		        		Resource r=i.next();
		        		
		        		if(r.getName().equals(nameOfResource)){
		        				exchange.respond(ResponseCode.FORBIDDEN);
		        				return;
		        		}
		        	}
		        	
		        	
		        	
		        	String option1= measurement.substring(iend2+1,iend2+3);
		        	if(!option1.equals("40")){
		        		exchange.respond(ResponseCode.NOT_ACCEPTABLE);
		        		return;
		        	}
		        		
		        	
		        	
		        	add(new PublishResource(nameOfResource,nameOfResource,nameOfResource+"_sensor","sensor",option1,true));
		        	exchange.respond(ResponseCode.CREATED, "Location: "+exchange.getRequestOptions().getURIPathString()+"/"+option1);
		        
	        	}
	        	else{
	        		//This is the case of malformed request
	        		exchange.respond(ResponseCode.BAD_REQUEST);
	        	}
        	}
        
        /* 
         * Listener for GET request
         */
        @Override
        public void handleGET(CoapExchange exchange) {
        	
        	/*
        	 * N.B. the observing (and then Subscribe-Unsubscribe) is automatically handled by Californium
        	 * */
        	
        	System.out.println("GET request"
        			+exchange.getRequestOptions().getURIPathString());
        	
        	String path=exchange.getRequestOptions().getURIPathString();
        	
        	if(path.equals("ps/")||path.equals("ps")){

            	/**-----Implement DISCOVER------**/
            	
        		
        		/*This is the DISCOVER because the path request has form ps or ps/*/
        		
        		//return the number of queries
	        	System.out.println("Query count : "
	        			+exchange.getRequestOptions().getURIQueryCount());
	        	
	        	int nQueries=exchange.getRequestOptions().getURIQueryCount();
	        	
	        	for(int i=0;i<nQueries;i++){
		        
	        		//return the list of queries
		        	System.out.println("Query-"+i+" : "
		        			+exchange.getRequestOptions().getURIQueries().get(i));
		        }
	        	
	        	//I want the list of child
	        	Collection<Resource> listOfChild=getChildren();
	        	
	        	Iterator<Resource> i=listOfChild.iterator();
	        	
	        	String res=null;
	        	String str=null;
	        	StringBuilder builder=new StringBuilder();//this is a builder used to concatenate multiple string
	        	
	        	//scroll all child resources founded
	        	while(i.hasNext()){
	        	
	        		Resource r=i.next();
	
	        		res=BrokerUtility.buildResponce(r,nQueries,exchange.getRequestOptions().getURIQueries());
	        		
	        		if(res!=null){
	        			builder.append(res).append("\n");
	        		}
	        		
	        	}
	        	
	        	if(res!=null)
	        		str=builder.toString();
	    		
	        	exchange.respond(str==null?ResponseCode.NOT_FOUND:ResponseCode.CONTENT,str==null?"":str);            
	        }
        	else{
        		/**-----Implement READ*----*/
        		String value=new String(Integer.toString(mValue));
        			
        		System.out.println(exchange.getRequestOptions().getContentFormat());
        		if(exchange.getRequestOptions().getContentFormat()!=40){
        				//I accept only the content type 40 ==> application/link-format
        				exchange.respond(ResponseCode.UNSUPPORTED_CONTENT_FORMAT,"");
        		}
        		else{
	        		//N.B. In case of not found resource Californium automatically set the Code 4.04
	        		
	        		//If the value is not included i return REQUEST_ENTITY_INCOMPLETE code
	        		exchange.respond(value.length()==0?ResponseCode.REQUEST_ENTITY_INCOMPLETE:ResponseCode.CONTENT,Integer.toString(mValue));
        		}
        	}
        }
        
        
        public void handlePUT(CoapExchange exchange) {
        	
        	System.out.println("PUT request"
        			+exchange.getRequestOptions().getURIPathString());
        	
        	setValue(Integer.parseInt((exchange.getRequestText().trim())));
        	exchange.respond(ResponseCode.CHANGED);
        	changed(); // notify all observers
        	
        }
        	  
        
        public void handleDELETE(CoapExchange exchange) {
        	
        	delete();
        	exchange.respond(ResponseCode.DELETED,"DELETE");
        }
    }

}