package com.coap.server.main;


import java.util.List;

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
			
			
			//retrive rt
			List<String> rtl=r.getAttributes().getAttributeValues("rt");
			String rt=null;
			
			if(!rtl.isEmpty())
				rt=new String(rtl.get(0));
			
			
			//retrive if
			List<String> ifl=r.getAttributes().getAttributeValues("if");
			String iff=null;
			
			if(!ifl.isEmpty())
				iff=new String(ifl.get(0));
			
			//retrive ct
			List<String> ctl=r.getAttributes().getAttributeValues("ct");
			String ct=null;
			
			if(!ctl.isEmpty())
				ct=new String(ctl.get(0));
			
			
			
			res=new String("<"+r.getPath()+r.getName()+">"
						+(rt==null?"":(";"+"rt=\""+r.getAttributes().getAttributeValues("rt").get(0)+"\""))
						+(iff==null?"":(";"+"if=\""+r.getAttributes().getAttributeValues("if").get(0)+"\""))
						+(ct==null?"":(";"+"ct=\""+r.getAttributes().getAttributeValues("ct").get(0)+"\""))
					);
		}
		
		return flag?null:res;
	}
	
	public static boolean checkForBadQuery(String s){
		//this function is used for check query
		if(s.contains("==") || s.contains("?") || s.contains("<") || s.contains(">") || s.contains("\\") || s.contains("&&") || s.contains("$")|| s.contains("^"))
			return false;
		
		return  true;
	}
	
	public static boolean checkForBadPayload(String s){
		
		if(s.contains("==") || s.contains("?") || s.contains("<<") || s.contains(">>") || s.contains("\\") || s.contains("&") || s.contains("$")|| s.contains("^"))
			return false;
		
		return  true;
	}
	
	public static boolean checkForBadPut(String s){
		
		if(s.contains("=") || s.contains("?") || s.contains("<") || s.contains(">") || s.contains("\\\\") || s.contains("&") || s.contains("$")|| s.contains("^"))
			return false;
		
		return  true;
	}
	
	public static boolean checkForBadRequest(String s){
		
		if(s.contains("==") || s.contains("?") || s.contains("<<") || s.contains(">>") || s.contains("\\\\") || s.contains("&") || s.contains("$")|| s.contains("^"))
			return false;
		
		return  true;
	}
	
	public static String getIf(String s){
		if(s.contains("if=")){
			//The if attribute is specified in the POST request
			int positionOfif=s.indexOf("if=")+3;
			int lastIndex=s.indexOf(";", s.indexOf("if="));
			
			lastIndex=lastIndex==-1?s.length():lastIndex;
			
			return s.substring(positionOfif,lastIndex);
		}
		return null;
	}
	
	public static String getCt(String s){
		if(s.contains("ct=")){
			//The if attribute is specified in the POST request
			int positionOfif=s.indexOf("ct=")+3;
			int lastIndex=s.indexOf(";", s.indexOf("ct="));
			
			lastIndex=lastIndex==-1?s.length():lastIndex;
			
			return s.substring(positionOfif,lastIndex);
		}
		return null;
	}
	
	public static String getRt(String s){
		if(s.contains("rt=")){
			//The if attribute is specified in the POST request
			int positionOfif=s.indexOf("rt=")+3;
			int lastIndex=s.indexOf(";", s.indexOf("rt="));
			
			lastIndex=lastIndex==-1?s.length():lastIndex;
			
			return s.substring(positionOfif,lastIndex);
		}
		return null;
	}

}
