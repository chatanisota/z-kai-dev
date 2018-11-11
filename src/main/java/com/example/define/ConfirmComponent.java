package com.example.define;

public class ConfirmComponent {
	String question;
	StringW yes;
	StringW no;
	
	public ConfirmComponent() {
		yes = new StringW("", "");
		no = new StringW("", "");
	}
	
	public void setQuestion(String s) {
		question = s;
	}
	public void setYes(String display, String send) {
		yes.s1 = display;
		yes.s2 = send;
	}
	public void setNo(String display, String send) {
		no.s1 = display;
		no.s2 = send;
	}
	public String getQuestion() {
		return question;
	}
	public StringW getYes() {
		return yes;
	}
	public StringW getNo() {
		return no;
	}
}
