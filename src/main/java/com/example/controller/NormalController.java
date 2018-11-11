package com.example.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.define.ButtonComponent;
import com.example.define.ConfirmComponent;
import com.example.define.Disaster;
import com.example.define.DisasterComponent;
import com.example.define.DisasterPack;
import com.example.define.KIND;
import com.example.model.DisasterModel;
import com.example.model.Friend;
import com.example.model.Location;
import com.example.model.User;
import com.example.snipet.Client;
import com.example.snipet.JsonMapperAPI;

@Component
public class NormalController {

	@Autowired
	User user;
	@Autowired
	Location location;
	@Autowired
	Friend friend;
	@Autowired
	DisasterModel disasterModel;
	
	public void menu(Client client) {
		ButtonComponent buttonComponent = new ButtonComponent();

		buttonComponent.setTitle("メニュー");
		buttonComponent.setText("(pullは情報を更新します)");
		
		buttonComponent.add("大切な人情報を登録","登録");
		buttonComponent.add("大切な人情報を編集","編集");
		buttonComponent.add("【開発用】pull","pull");

		client.addReplyButton(buttonComponent);	
	}
	
	public void pull(Client client) throws SQLException {
		
		int[] ids = user.getFriendIds(client.getUserId());
		Timestamp prevtime = user.getTimeStamp(client.getUserId());
		
		String name;
		int locate;
		KIND kind;
		List<DisasterPack> disasterlist = new ArrayList<>();
		
		for(int i=0; i<3;i++ ) {
			if(ids[i]<0)continue;
			name = friend.getName(ids[i]);
			locate = friend.getLocate(ids[i]);
			kind = friend.getKind(ids[i]);
			disasterlist.addAll(JsonMapperAPI.getDisasterJson(String.valueOf(locate),name,kind,prevtime));			
		}
		
		//いったん格納
		for(DisasterPack pack: disasterlist) {	
			disasterModel.insertDisasterInfo(client.getUserId(),pack.getMiniReport(),pack.getReport(),0,pack.getTimestamp());
		}
		
		List<DisasterComponent> disasterComp = disasterModel.getDisaster(client.getUserId());
		
		if(disasterComp.isEmpty()) {
			client.addReplyText("最新の情報はありません。");
		}else {
			for(DisasterComponent comp: disasterComp) {
				client.addReplayDisaster(comp);
			}
		}		
		
	}
	
	public void train1() throws SQLException {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Disaster ds = new Disaster();
		ds.setDatetime(now);
		ds.setTitle("[試験運用]物資配給");
		
		String textText = ""+
		"京都工芸繊維大学、キャンパス内で14:00~で支援物資を配給いたします。\n"+
		"配給品は１家族につき、以下の通りです。\n"+
		" 飲料水　ペットボトル１\n"+
		" 菓子パン　1袋\n";
		ds.setHeadline(textText);
		ds.setToname("息子");
		
		DisasterPack pack = new DisasterPack(ds);
		
		List<String> ls = user.getAllUserIds();
		for(String user_id: ls) {		
			disasterModel.insertDisasterInfo(user_id,pack.getMiniReport(),pack.getReport(),0,pack.getTimestamp());
		}
	}
	
public void trainfree(String str) throws SQLException {
	
	String[] split = str.split(":", 0);
	if(split.length>=3){
		this.trainfreeinc(split[0], split[1], split[2]);
	}
	
}
	
private void trainfreeinc(String title,String text,String toname) throws SQLException {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Disaster ds = new Disaster();
		ds.setDatetime(now);
		ds.setTitle(title);
		
		
		ds.setHeadline(text);
		ds.setToname(toname);
		
		DisasterPack pack = new DisasterPack(ds);
		
		List<String> ls = user.getAllUserIds();
		for(String user_id: ls) {		
			disasterModel.insertDisasterInfo(user_id,pack.getMiniReport(),pack.getReport(),0,pack.getTimestamp());
		}
	}

public void train0000(Client client) {
		client.addReplyText("Train0000");
	
}

public boolean ismore(String reply) {
	if(reply.length()<3)return false;
	
	String head = reply.substring(0, 3);
	return head.equals("SND");
}

public void more(Client client,String reply) throws SQLException {
	int size = reply.length();
	String head = reply.substring(3, size);
	int id = Integer.parseInt(head);
	
	client.addReplyText("以下のメッセージを「長押し→転送」をして、大切な人に送りましょう！");
	client.addReplyText(disasterModel.getMore(id));
	
}

public void edit(Client client) throws SQLException {
	ButtonComponent buttonComponent = new ButtonComponent();

	buttonComponent.setText("災害情報通知を変更する人を選んでください。");
	
	int[] ids = user.getFriendIds(client.getUserId());
	if(ids[0]>0) {String name1 = friend.getName(ids[0]); buttonComponent.add(name1,"1");}
	if(ids[1]>0) {String name2 = friend.getName(ids[1]); buttonComponent.add(name2,"2");}
	if(ids[2]>0) {String name3 = friend.getName(ids[2]); buttonComponent.add(name3,"3");}
	buttonComponent.add("編集をやめる","やめる");

	client.addReplyButton(buttonComponent);
}
public void setTmpFriendSelect(Client client, int num) throws SQLException {
	int friend_id = user.getFriendIds(client.getUserId())[num-1];
	user.setTmpFriendId(client.getUserId(), friend_id);
	user.setTmpKind(client.getUserId(), friend.getKind(friend_id));
	
}

public void jouhou1edit(Client client) throws SQLException {
	
		
	KIND kd = user.getTmpKind(client.getUserId());
	ButtonComponent buttonComponent = new ButtonComponent();

	buttonComponent.setText(user.getTmpName(client.getUserId())+"さんにとって不要な情報をタップして「OFF」にしてください");
	
	String text1 = "地震・津波\t" + (kd.isWave()?"ON":"OFF");
	String text2 = "台風\t" + (kd.isTyphoon()?"ON":"OFF");
	String text3 = "大雨\t" + (kd.isRain()?"ON":"OFF");
	buttonComponent.add(text1,"地震・津波");
	buttonComponent.add(text2,"台風");
	buttonComponent.add(text3,"大雨");
	buttonComponent.add("次へ","次へ");

	client.addReplyButton(buttonComponent);				
}
public void jouhou2edit(Client client) throws SQLException {
	
	KIND kd = user.getTmpKind(client.getUserId());
	ButtonComponent buttonComponent = new ButtonComponent();

	buttonComponent.setText(user.getTmpName(client.getUserId())+"さんにとって不要な情報をタップして「OFF」にしてください");
	
	String text1 = "河川・洪水\t" + (kd.isRiver()?"ON":"OFF");
	String text2 = "土砂災害\t" + (kd.isSand()?"ON":"OFF");
	String text3 = "避難生活\t" + (kd.isLifeline()?"ON":"OFF");
	buttonComponent.add(text1,"河川・洪水");
	buttonComponent.add(text2,"土砂災害");
	buttonComponent.add(text3,"避難生活");
	buttonComponent.add("次へ","次へ");

	client.addReplyButton(buttonComponent);				
}
public void jouhou3edit(Client client) throws SQLException {
	
	KIND kd = user.getTmpKind(client.getUserId());
	ButtonComponent buttonComponent = new ButtonComponent();

	buttonComponent.setText(user.getTmpName(client.getUserId())+"さんにとって不要な情報をタップして「OFF」にしてください");
	
	String text1 = "注意報警報\t" + (kd.isAlart()?"ON":"OFF");
	buttonComponent.add(text1,"注意報");
	buttonComponent.add("完了","完了");
	buttonComponent.add("キャンセル","キャンセル");

	client.addReplyButton(buttonComponent);				
}

public void edit_kakunin(Client client) throws SQLException {
	int friend_id = user.getTmpFriendId(client.getUserId());
	user.setTmpName(client.getUserId(), friend.getName(friend_id));	
	String jyusho = location.getJISName(friend.getLocate(friend_id));
	client.addReplyText(user.getTmpInfo(client.getUserId(), jyusho));
	
	ConfirmComponent con = new ConfirmComponent();
	con.setQuestion("上記の内容で登録します。よろしいですか？");
	con.setYes("はい", "はい");
	con.setNo("いいえ", "いいえ");
	client.addReplyConfirm(con);	
}

public boolean edit_kanryou(Client client) throws SQLException {
	KIND kind = user.getTmpKind(client.getUserId());
			
	friend.setKind(user.getTmpFriendId(client.getUserId()), kind);
	
	client.addReplyText("変更を完了しました。");
	
	ButtonComponent buttonComponent = new ButtonComponent();

	buttonComponent.setText("災害情報通知を変更する人を選んでください。");
	
	int[] ids = user.getFriendIds(client.getUserId());
	if(ids[0]>0) {String name1 = friend.getName(ids[0]); buttonComponent.add(name1,"1");}
	if(ids[1]>0) {String name2 = friend.getName(ids[1]); buttonComponent.add(name2,"2");}
	if(ids[2]>0) {String name3 = friend.getName(ids[2]); buttonComponent.add(name3,"3");}
	buttonComponent.add("編集をやめる","やめる");

	client.addReplyButton(buttonComponent);
	
	return true;
	
}
	
	
}
