package com.example.define;

import java.util.ArrayList;
import java.util.List;

//Form of button when create buttonTemplete of LINE
public class DisasterComponent {
	
	private int id;
	private String report;

	public DisasterComponent(int id, String st) {
		this.id = id;
		this.report = st;
	}
	
	public int getId() {
		return id;
	}
	public String getReport() {
		return report;
	}
}
