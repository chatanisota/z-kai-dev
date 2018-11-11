package com.example.view;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.controller.Controller;
import com.example.define.Common;
import com.example.snipet.Client;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;

import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;


@LineMessageHandler
@Component
public class ViewLINE{
	
	@Autowired
	Controller controller;
	  
    //メッセージ（文字）をクライアントから受け取ったとき
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
           	
        try {
			controller.recieveMsgMain(event);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    @EventMapping
    protected void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
    	
    }
    @EventMapping
    protected void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
    	
    }
    
    public void sendMessage(Client client) {
    	final BotApiResponse botApiResponse;
    	final LineMessagingClient server = Common.getLineMessagingClient();
    	try {
    	    botApiResponse = server.replyMessage(client.createReplyMessage()).get();
    	} catch (InterruptedException | ExecutionException e) {
    	    e.printStackTrace();
    	    return;
    	}
    	
    }
    
   


	@EventMapping
    public void handleDefaultMessageEvent(Event event) {
		System.out.println(event.toString());
    }
}