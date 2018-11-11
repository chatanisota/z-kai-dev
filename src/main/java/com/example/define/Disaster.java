package com.example.define;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Disaster {
	Timestamp datetime;
	String headline;
	String title;
	String toname;
	
	public void setDatetime(Timestamp tm) {
		datetime = tm;
	}
	public void setHeadline(String st) {
		headline = st;
	}
	public void setTitle(String st) {
		title = st;
	}
	public String getHeadline() {
		return headline;
	}
	public String getMiniHeadline() {
		int size = headline.length()-1;
		return headline.substring(0,size>25?25:size) + "...";
	}
	public String getTitle() {
		return title;
	}
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        
        String str = sdf.format(datetime) +" 発表";
        
        return str;
	}
	public String getMiniDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd日 HH:mm");
        
        String str = sdf.format(datetime);
        
        return str;
	}
	
	public void setToname(String s) {
		toname = s;
	}
	public String getToname() {
		return toname;
	}
	public Timestamp getTimestamp() {
		// TODO Auto-generated method stub
		return datetime;
	}
}
