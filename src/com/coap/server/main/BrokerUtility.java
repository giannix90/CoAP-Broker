package com.coap.server.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;

public class BrokerUtility {
	
/**
 * 
 * This class utility is used to group all utility functions
 * 
 * */
	
	public static String buildResponce(Resource r,int nQueries,List<String> queries){
		
		boolean flag=false;//This flag will be used to signal if the resource have requirements specified in the query
		String res=null; //pointer to buffer which will contain the response
		
		//i control each query for the resource
		for(int i=0;i<nQueries;i++){
			
			if(!(queries.get(i).substring(3).equals(new String(r.getAttributes().getAttributeValues(queries.get(i).substring(0, 2)).get(0)))))
				flag=true;//resource don-t contain the specified values in the query
		
				//System.out.println(queries.get(i).substring(3)+"\n"+r.getAttributes().getAttributeValues(queries.get(i).substring(0, 2)).get(0));
		}
		
		if(!flag){
			
			/*Lazy initialization ==>i allocate the buffer for string only if needed*/
			
			res=new String("<"+r.getPath()+r.getName()+">"+
	        		";"+"rt=\""+r.getAttributes().getAttributeValues("rt").get(0)+"\";"
					+"if=\""+r.getAttributes().getAttributeValues("if").get(0)+"\";"
					+"ct=\""+r.getAttributes().getAttributeValues("ct").get(0)+"\""
					);
		}
		
		return flag?null:res;
	}

}
