package com.example.define;

import java.sql.Timestamp;

public class DisasterPack {

	String mini_report;
	String report;
	Timestamp timestamp;
	
	
	public DisasterPack(Disaster ds) {
		
		mini_report = ""+			
				""+ds.getTitle()+"\n"+
				""+ds.getMiniDate()+"\n\n"+
				ds.getMiniHeadline();
		
		report = ""+ds.getToname()+"より以下の情報をもらいました。\n"+
				"[ "+ds.getTitle()+" ]\n\n"+
				""+ds.getDate()+"\n"+
				"-----------------\n"+
				ds.getHeadline()+"\n"+
				"------------------"+
			    "あんしんの糸";
		timestamp = ds.getTimestamp();
	}
	
	public String getReport() {
		return report;
	}

	public String getMiniReport() {
		return mini_report;
	}
	public Timestamp getTimestamp() {
		
		return timestamp;
	}	
	
}
