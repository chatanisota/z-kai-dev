package com.example.define;

import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;

//参考URL
//https://qiita.com/kazuki43zoo/items/0ce92fce6d6f3b7bf8eb
@Component
public final class Common {

    public static String CHANNEL_TOKEN = "hF2WUC+LyASDafN7nWOKmHMs0PLpjNRcE4nQ0DhSI+xiogPEGZDXGoQ1y5pVfY5CRCtCsQzS0YhtICrgoRwT4yHiMJtJ8pZuu5D0j1Nyjk6jIf4cKMnZtwf3Y2T36t9KHrftL7FSMsW1MvFIZeSPFQdB04t89/1O/w1cDnyilFU=";
    // ↑チャンネルアクセストークン（ロングターム）   
    
    public static LineMessagingClient getLineMessagingClient() {
	    return LineMessagingClient
	            .builder(Common.CHANNEL_TOKEN)
	            .build();
    }
    
    
}