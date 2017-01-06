package com.coap.server.main;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.californium.core.CoapResource;

import com.coap.server.main.AgeList.Age;

public class AgeList {

	class Age{
		private long d; //this is the timestamp of creation
		private int a; //this is the Max Age
		private CoapResource r;
		
		Age(long d,int a,CoapResource r){
			this.d=d;
			this.a=a;
			this.r=r;
		}
		
		long getAge(){
			return this.a;
		}
		long getTime(){
			return this.d;
		}
		
	}
	private final Map<CoapResource,Age> age= new ConcurrentHashMap<CoapResource,Age>();
	
	public void insert(long d,int a,CoapResource r){
		age.put(r, new Age(d,a,r));
		
	}
	
	public Age getAge(CoapResource r){
		return this.age.get(r);
	}
	
	public Age delete(CoapResource r){
		
		return this.age.remove(r);
	}
	
	public boolean hasResource(CoapResource r){
		return this.age.containsKey(r);
	}
	
	public long getRemainingTime(CoapResource r){
		
		long passedTime=(new Date().getTime()-this.getAge(r).getTime());
		System.out.println("Data :"+new Date().getTime() +"\n passed time"+passedTime);
		
		long remainingTime=((long)this.getAge(r).getAge()*1000)-passedTime;
		
		return remainingTime>0?(remainingTime/1000):0; //return in second
	}
	
	public void checkForDelete() {//elimina valori più vecchi di secondi
          Collection<Age> ageCollection=this.age.values();
          Iterator<Age> it = ageCollection.iterator(); 
          while (it.hasNext()) {
              Age a=(Age)it.next();//scorro i valori della map
              
              long passed_time= new Date().getTime()-a.d;//tempo attuale in ms- tempo prelev dati
              long seconds_to_ms=(long) a.a*1000; //secondi converto in ms
              
              
              if(passed_time-seconds_to_ms>0)
              {
            	  //valore più vecchio di seconds_to_ms millisec
            	  	 a.r.delete(); // i delete the coap resource
                     //it.remove();//rimuove il valore corrente letto da iterator
                     
              }
       
          }
      }
}

