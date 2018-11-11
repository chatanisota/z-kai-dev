package com.example.define;

import java.util.ArrayList;
import java.util.List;

//Form of button when create buttonTemplete of LINE
public class ButtonComponent {
	
	private String title;
	private String text;
	private String imageURI;
	private List<StringW> buttonList;
	
	public ButtonComponent(){
		buttonList = new ArrayList<>();
	}
	
	public void setTitle(String t) {
		title = t;
	}
	public String getTitle() {
		return title;
	}
	public void setText(String t) {
		text = t;
	}
	public String getText() {
		return text;
	}
	public void setImageURI(String t) {
		imageURI = t;
	}
	public String getimageURI() {
		return imageURI;
	}
	public void add(String display_text, String send_text ) {
		buttonList.add(new StringW(display_text, send_text ));
	}
	public StringW getButtonText(int n) {
		if(n<buttonList.size()) {
			return buttonList.get(n);
		}else {
			return null;
		}
	}
	public List<StringW> getButtonList(){
		return buttonList;
	}
}
