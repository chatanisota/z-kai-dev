package com.example.controller;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import com.example.define.ButtonComponent;
import com.example.define.KIND;
import com.example.define.STATUS;
import com.example.model.Friend;
import com.example.model.Location;
import com.example.model.ModelBase;
import com.example.model.User;
import com.example.snipet.AccessAPI;
import com.example.snipet.Client;
import com.example.snipet.JsonMapperAPI;
import com.example.view.ViewLINE;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

@Component
public final class Controller {
	
	@Autowired
	ModelBase modelbase;
	@Autowired
	User user;
	@Autowired
	Location location;
	@Autowired
	Friend friend;
	@Autowired
	RegistController registController;
	@Autowired
	NormalController normalController;
	
	@Autowired
	ViewLINE viewLINE;
	
	//When receive MESSAGE
	public void recieveMsgMain(MessageEvent<TextMessageContent> event) throws SQLException {
		
		modelbase.initDB();
		
		//クライアントの情報をclientに格納
        Client client = new Client(event);
        String reply = client.getRecieveText();
        STATUS s=STATUS.normal;
        
        System.out.println("<<MyStatus>>"+getStatus(client).name());
        
        //状態遷移
		switch(s=getStatus(client)) {
		case init:
				s=setStatus(client,STATUS.hello);
			break;
		case more:
		case pull:
		case menu:
				s=STATUS.menu;
				if(normalController.ismore(reply)) {s=setStatus(client,STATUS.more);}
				if(e(reply,"登録"))	{s=setStatus(client,STATUS.namae);}
				if(e(reply,"編集"))	{s=setStatus(client,STATUS.edit);}
				if(e(reply,"pull")) {s=setStatus(client,STATUS.pull);}
				if(e(reply,"train1")) {normalController.train1();}
				if(e(reply,"trainfree")) {s=setStatus(client,STATUS.train0000);}
			break;
		case hello:
				s=STATUS.menu;
				if(e(reply,"はい")) 	{s=setStatus(client,STATUS.namae);}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.menu);}
			break;
		case namae:
				user.setTmpName(client.getUserId(), reply);
				s=setStatus(client,STATUS.namae_kakunin);
			break;
		case namae_kakunin:
				s=STATUS.namae_kakunin;
				if(e(reply,"はい")) 	{s=setStatus(client,STATUS.jis1);}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.namae);}
			break;
		case jis1_miss:
		case jis1:				
				if(1 == location.getNumOfSerchPrefecture(reply)) {
					user.setTmpPrefecture(client.getUserId(), Integer.parseInt(location.serchPrefecture(reply).get(0).s2));
					s=setStatus(client,STATUS.jis2);
				}else {
					s=setStatus(client,STATUS.jis1_miss);
				}	
			break;
		case jis2:
		case jis2_miss:{
				int num;
				if(1 == (num=location.getNumOfSerchCity(user.getTmpPrefecture(client.getUserId()),reply))) {
					user.setTmpCity(client.getUserId(), Integer.parseInt(location.serchCity(user.getTmpPrefecture(client.getUserId()),reply).get(0).s2));
					s=setStatus(client,STATUS.jis_kakunin);
				}else if(num > 1) {
					s=setStatus(client,STATUS.jis2_multi);
				}else {
					s=setStatus(client,STATUS.jis2_miss);
				}
			break;}
		case jis2_multi:{
				int num;
				if(1 == (num=location.getNumOfSerchCityG(user.getTmpPrefecture(client.getUserId()),reply))) {
					user.setTmpCity(client.getUserId(), Integer.parseInt(location.serchCityG(user.getTmpPrefecture(client.getUserId()),reply).get(0).s2));
					s=setStatus(client,STATUS.jis_kakunin);
				}else{
					if(1 == (num=location.getNumOfSerchCity(user.getTmpPrefecture(client.getUserId()),reply))) {
						user.setTmpCity(client.getUserId(), Integer.parseInt(location.serchCity(user.getTmpPrefecture(client.getUserId()),reply).get(0).s2));
						s=setStatus(client,STATUS.jis_kakunin);
					}else if(num > 1) {
						s=setStatus(client,STATUS.jis2_multi);
					}else {
						s=setStatus(client,STATUS.jis2_miss);
					}
				}
			break;}
		case jis_kakunin:
				s=STATUS.jis_kakunin;
				if(e(reply,"はい")) 	{s=setStatus(client,STATUS.jouhou1);}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.jis1);}
			break;
		case jouhou1:{
				s=STATUS.jouhou1;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"台風"))	{user.setTmpKind(client.getUserId(), kd.setTyphoon(!kd.isTyphoon()));}
				if(e(reply,"地震・津波"))	{user.setTmpKind(client.getUserId(), kd.setWave(!kd.isWave()));}
				if(e(reply,"大雨"))	{user.setTmpKind(client.getUserId(), kd.setRain(!kd.isRain()));}
				s=STATUS.jouhou1;
				if(e(reply,"次へ")) {s=setStatus(client,STATUS.jouhou2);}
			break;}
		case jouhou2:{
				s=STATUS.jouhou2;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"河川・洪水"))	{user.setTmpKind(client.getUserId(), kd.setRiver(!kd.isRiver()));}
				if(e(reply,"土砂災害"))	{user.setTmpKind(client.getUserId(), kd.setSand(!kd.isSand()));}
				if(e(reply,"避難生活"))	{user.setTmpKind(client.getUserId(), kd.setLifeline(!kd.isLifeline()));}
				s=STATUS.jouhou2;
				if(e(reply,"次へ")) {s=setStatus(client,STATUS.jouhou3);}
			break;}
		case jouhou3:{
				s=STATUS.jouhou3;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"注意報"))	{user.setTmpKind(client.getUserId(), kd.setAlart(!kd.isAlart()));}
				s=STATUS.jouhou3;
				if(e(reply,"次へ")) {s=setStatus(client,STATUS.jouhou_kakunin);}
			break;}
		case jouhou_kakunin:
				s=STATUS.jouhou_kakunin;
				if(e(reply,"はい")) 	{
					if(registController.regist(client)) {
						//登録成功
						s=setStatus(client,STATUS.touroku_kanryou);
					}else {
						//３人以上より失敗
						s=setStatus(client,STATUS.touroku_full);
					}
				}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.modori1);}
			break;
		case touroku_full:
				s=STATUS.touroku_full;
				if(e(reply,"1")) {registController.regist_upper(client, 1); s=setStatus(client,STATUS.touroku_kanryou);}
				if(e(reply,"2")) {registController.regist_upper(client, 2); s=setStatus(client,STATUS.touroku_kanryou);}
				if(e(reply,"3")) {registController.regist_upper(client, 3); s=setStatus(client,STATUS.touroku_kanryou);}
				if(e(reply,"やめる")) {s=setStatus(client,STATUS.menu);}
			break;
		case touroku_kanryou:
				if(e(reply,"はい")) {s=setStatus(client,STATUS.namae);}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.menu);}
			break;
		case modori1:
				s=STATUS.modori1;
				if(e(reply,"つづける")) 	{s=setStatus(client,STATUS.touroku);}
				if(e(reply,"情報選択")) {s=setStatus(client,STATUS.jouhou1);}
				if(e(reply,"住所登録")) {s=setStatus(client,STATUS.jis1);}
				if(e(reply,"やめる")) {s=setStatus(client,STATUS.menu);}
			break;
		case edit_kanryou:
		case edit:	
				if(e(reply,"やめる")){s=setStatus(client,STATUS.menu);}
				if(e(reply,"1")||e(reply,"2")||e(reply,"3")) 	{
					normalController.setTmpFriendSelect(client,Integer.parseInt(reply));
					s=setStatus(client,STATUS.jouhou1edit);}
			break;
		case jouhou1edit:{
				s=STATUS.jouhou1edit;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"台風"))	{user.setTmpKind(client.getUserId(), kd.setTyphoon(!kd.isTyphoon()));}
				if(e(reply,"地震・津波"))	{user.setTmpKind(client.getUserId(), kd.setWave(!kd.isWave()));}
				if(e(reply,"大雨"))	{user.setTmpKind(client.getUserId(), kd.setRain(!kd.isRain()));}
				s=STATUS.jouhou1edit;
				if(e(reply,"次へ")) {s=setStatus(client,STATUS.jouhou2edit);}
			break;}
		case jouhou2edit:{
				s=STATUS.jouhou2edit;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"河川・洪水"))	{user.setTmpKind(client.getUserId(), kd.setRiver(!kd.isRiver()));}
				if(e(reply,"土砂災害"))	{user.setTmpKind(client.getUserId(), kd.setSand(!kd.isSand()));}
				if(e(reply,"避難生活"))	{user.setTmpKind(client.getUserId(), kd.setLifeline(!kd.isLifeline()));}
				s=STATUS.jouhou2edit;
				if(e(reply,"次へ")) {s=setStatus(client,STATUS.jouhou3edit);}
			break;}
		case jouhou3edit:{
				s=STATUS.jouhou3edit;
				KIND kd = user.getTmpKind(client.getUserId());
				if(e(reply,"注意報"))	{user.setTmpKind(client.getUserId(), kd.setAlart(!kd.isAlart()));}
				s=STATUS.jouhou3edit;
				if(e(reply,"完了")) {s=setStatus(client,STATUS.edit_kakunin);}
				if(e(reply,"やめる")) {s=setStatus(client,STATUS.edit);}
			break;}	
		case edit_kakunin:
				s=STATUS.edit_kakunin;
				if(e(reply,"はい")) 	{s=setStatus(client,STATUS.edit_kanryou);}
				if(e(reply,"いいえ")) {s=setStatus(client,STATUS.edit);}
			break;
		case train0000:
				normalController.trainfree(reply);
				s=setStatus(client,STATUS.menu);
			break;
		default:
				//nothing
			break;
			
		}
		if(e(reply,"@取消")) {client.resetReply(); s=setStatus(client,STATUS.menu);}
		if(e(reply,"やめる")) {client.resetReply(); s=setStatus(client,STATUS.menu);}
		
		System.out.println("<<ChangeMyStatus>>"+s.name());

		
		//状態
		switch(s) {
		case hello:
				registController.hello(client);
			break;
		case menu:
				normalController.menu(client);
			break;
		case pull:
				normalController.pull(client);
			break;
		case more:
				normalController.more(client,reply);
			break;
		case namae:
				registController.namae(client);
			break;
		case namae_kakunin:
				registController.namae_kakunin(client,reply);
			break;
		case jis1:
				registController.jis1(client);
			break;
		case jis1_miss:
				registController.jis1_miss(client);
			break;
		case jis2:
				registController.jis2(client,reply);
			break;
		case jis2_miss:
				registController.jis2_miss(client);
			break;
		case jis2_multi:
				registController.jis2_multi(client, reply);
			break;
		case jis_kakunin:
				registController.jis_kakunin(client);
			break;
		case jouhou1:
				registController.jouhou1(client);
			break;
		case jouhou2:
				registController.jouhou2(client);
			break;
		case jouhou3:
				registController.jouhou3(client);
			break;
		case jouhou_kakunin:
				registController.jouhou_kakunin(client);
			break;
		case touroku_kanryou:
				registController.touroku_kanryou(client);
			break;
		case touroku_full:
				registController.touroku_full(client);
			break;
		case edit:
				normalController.edit(client);
			break;
		case jouhou1edit:
				normalController.jouhou1edit(client);
			break;
		case jouhou2edit:
				normalController.jouhou2edit(client);
			break;
		case jouhou3edit:
				normalController.jouhou3edit(client);
			break;
		case edit_kakunin:
				normalController.edit_kakunin(client);
			break;
		case edit_kanryou:
				normalController.edit_kanryou(client);
			break;
		case modori1:
				registController.modori1(client);
			break;
		case train0000:
				normalController.train0000(client);
			break;
		default:
			break;
		}
		
        
        //ボタンテンプレートを表示
        modelbase.closeDB();
        //ボタンテンプレートを表示
        viewLINE.sendMessage(client);
	
	}
	
	//汎用関数
	private STATUS getStatus(Client client) throws SQLException {
		return user.getUserStatus(client.getUserId());
	}
	private STATUS setStatus(Client client, STATUS status) {
		user.setUserStatus(client.getUserId(), status);
		return status;
	}
	private boolean e(String reply,String key) {
		return reply.equals(key);
	}
	
}
