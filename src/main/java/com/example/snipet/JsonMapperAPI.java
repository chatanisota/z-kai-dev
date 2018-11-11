package com.example.snipet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.define.Disaster;
import com.example.define.DisasterPack;
import com.example.define.KIND;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapperAPI {

	    public static List<DisasterPack> getDisasterJson(String areacode,String toname, KIND kind, Timestamp prevtime){
	    	
	    	List<DisasterPack> disasterList = new ArrayList<>();
	    	
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	        	String st = AccessAPI.getAPIFromAITC(areacode, kind, prevtime);
	        	
	            JsonNode rootnode = mapper.readTree(st);
	            
	            System.out.println("<<Result>>"+st);
	            
	            //int size = rootnode.get("paging").get("totalCount").asInt();
	            //if(size<1)return null;
	            int i=0;
	            
	            for(JsonNode node : rootnode.get("data")) {
	            	System.out.println("<<Json>>"+i+"番目");
	            	i++;
	            	
	            	Disaster ds = new Disaster();
	            	
	            	//JsonNode node = rootnode.get("data").get(i);
	
		            String datetimestr = node.get("datetime").asText();
		            try {
		            	//0123456789012345678
		            	//yyyy-mm-ddThh:mm:ss
		            	String tmp = "";
		            	tmp += datetimestr.substring(0,4);
		            	tmp += "/";
		            	tmp += datetimestr.substring(5,7);
		            	tmp += "/";
		            	tmp += datetimestr.substring(8,10);
		            	tmp += " ";
		            	tmp += datetimestr.substring(11,13);
		            	tmp += ":";
		            	tmp += datetimestr.substring(14,16);
		            	
						Timestamp datetimeTS = new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(tmp).getTime());
						ds.setDatetime(datetimeTS);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            ds.setHeadline(node.get("headline").get(0).asText());
		            ds.setTitle(node.get("title").asText());
		            ds.setToname(toname);
		            
		            disasterList.add(new DisasterPack(ds));
		            
		            
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return disasterList;


	    }

	    

	
}
