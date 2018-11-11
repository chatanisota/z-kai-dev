package com.example.snipet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.define.ButtonComponent;
import com.example.define.Common;
import com.example.define.ConfirmComponent;
import com.example.define.Disaster;
import com.example.define.DisasterComponent;
import com.example.define.StringW;
import com.example.snipet.ReplyMessageCreater;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;

public class Client {
	
	private String replyToken;
	private TextMessageContent recieveMessage;		//受け取った　メッセージ内テキスト
	private List<Message> replyContents;
	private UserProfileResponse userProfile;
	private String userId;
	
	
	public Client(MessageEvent<TextMessageContent> event) {
		userId 			= event.getSource().getUserId();
		userProfile 	= getUserProfile(userId);
		recieveMessage 	= event.getMessage();
		replyToken 		= event.getReplyToken();	
		replyContents	= new ArrayList<>();
	}
	
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userProfile.getDisplayName();
	}
	public String getUserComment() {
		return userProfile.getStatusMessage();
	}
	public String getReplyToken() {
		return replyToken;
	}
	public String getRecieveText() {
		return recieveMessage.getText();
	}
	
	//返信用の通常テキストを作成
	public final void addReplyText(String replyText) {
		System.out.println("<<addButtonT>>"+replyText);
		replyContents.add(
				ReplyMessageCreater.createText(replyText)
				);
	}
	//返信用の選択式有テキストを作成
	public void addReplyButton(ButtonComponent buttonComponent) {
		System.out.println("<<addButtonB>>"+buttonComponent.getText());
		replyContents.add(
				ReplyMessageCreater.createButtonsTemplate(
				buttonComponent.getTitle(),
				buttonComponent.getText(),
				buttonComponent.getimageURI(),
				buttonComponent.getButtonList())
				);		
	}
	//返信用の確認テキストを作成
	public void addReplyConfirm(ConfirmComponent confirmComponent) {
		System.out.println("<<addButtonC>>");
		replyContents.add(
				ReplyMessageCreater.createConfirmTemplate(
				confirmComponent.getQuestion(),
				confirmComponent.getYes(),
				confirmComponent.getNo()
				));
	}
	public void addReplayDisaster(DisasterComponent disasterComponent) {
		ButtonComponent buttonComponent = new ButtonComponent();

		buttonComponent.setText(disasterComponent.getReport());
		
		buttonComponent.add("大切な人に送る","SND"+disasterComponent.getId());

		this.addReplyButton(buttonComponent);
	}
	
	
	public ReplyMessage createReplyMessage() {
		return new ReplyMessage(replyToken,replyContents);
	}
	public void resetReply() {
		replyContents = new ArrayList<>();
	}
	
	//ユーザのプロフィールをLINEサーバより取得
	private final UserProfileResponse getUserProfile(String userId) {	
		LineMessagingClient server = Common.getLineMessagingClient();
		try {
			return server.getProfile(userId).get();			
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}	
	}

}

