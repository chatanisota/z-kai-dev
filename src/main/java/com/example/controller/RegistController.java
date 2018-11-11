package com.example.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.define.ButtonComponent;
import com.example.define.ConfirmComponent;
import com.example.define.KIND;
import com.example.define.StringW;
import com.example.model.Friend;
import com.example.model.Location;
import com.example.model.User;
import com.example.snipet.Client;

@Component
public class RegistController {
	
	@Autowired
	User user;
	@Autowired
	Location location;
	@Autowired
	Friend friend;
	
	public void hello(Client client) {
		
		client.addReplyText("あんしんの糸へようこそ！！");
		client.addReplyText(""
				+"あんしんの糸は大切な誰かを守るのために、"
				+"防災に役立つ情報を受信・送信することができる"
				+"サービスです。");

		//確認テンプレート
		ConfirmComponent con = new ConfirmComponent();
		con.setQuestion(client.getUserName()+"さん。\n"
				+"さっそく大切な人を登録しましょう！");
		con.setYes("はい", "はい");
		con.setNo("いいえ", "いいえ");
		//中身↑を設定

		//確認テンプレートに中身を格納
		client.addReplyConfirm(con);
	}
	
	public void namae(Client client) {
		
		client.addReplyText("登録したい人の名前（ニックネーム）を教えてください。");
	}
	
	public void namae_kakunin(Client client, String name) {
		
		ConfirmComponent con = new ConfirmComponent();
		con.setQuestion(name+"\nでよろしいですか？");
		con.setYes("はい", "はい");
		con.setNo("いいえ", "いいえ");
		client.addReplyConfirm(con);
	}
	
	public void jis1(Client client) throws SQLException {
		
		client.addReplyText(user.getTmpName(client.getUserId())+"さんが住んでいる都道府県を教えてください。");
	}
	public void jis1_miss(Client client) throws SQLException {
		
		client.addReplyText(user.getTmpName(client.getUserId())+"さんが住んでいる都道府県を教えてください。");
		
		String body = "";
		List<StringW> list = location.getPrefectures();
		int size;
		if((size=list.size())>=1) {
			for(int i=0; i<size; i++) {
				body += "\n"+list.get(i).s1;
			}		
		}
		
		client.addReplyText(body);
		client.addReplyText("上の中からご入力ください。");
	}
	
	public void jis2(Client client, String prefecture) throws SQLException {
		
		client.addReplyText(user.getTmpName(client.getUserId())+"さんが住んでいる市町村を教えてください。\nヘルプ：「？」と送信");
	}
	
	public void jis2_miss(Client client) throws SQLException {
		
		client.addReplyText("該当する市町村が見つかりませんでした。");
		client.addReplyText(user.getTmpName(client.getUserId())+"さんが住んでいる市町村を教えてください。");
			
		String body = "";
		List<StringW> list = location.getCitys(user.getTmpPrefecture(client.getUserId())); 
		if(!CollectionUtils.isEmpty(list)) {
			for(StringW sw :list) {
				if(isBigCity(sw.s2)) {break;}
				body += "\n"+sw.s1;
			}	
			body +=" etc...";
		}else {
			body +="[error]RegistController at 99!";
		}
		client.addReplyText(body);			
	}
	
	public void jis2_multi(Client client,String name) throws SQLException {
			
		client.addReplyText(user.getTmpName(client.getUserId())+"さんが住んでいる市町村を教えてください。");
		
		List<StringW> list = location.serchCity(user.getTmpPrefecture(client.getUserId()),name);
		
		//ボタンテンプレートの中身
		ButtonComponent buttonComponent = new ButtonComponent();
		
		int size;
		//中身↓を設定
		buttonComponent.setText("検索候補");
		if((size=list.size())>=1) {
			for(int i=0; i<4&&i<size; i++) {
				buttonComponent.add(list.get(i).s1, list.get(i).s1);
			}		
		}

		//中身↑を設定

		//ボタンテンプレートに中身を格納
		client.addReplyButton(buttonComponent);
	}
	public void jis_kakunin(Client client) throws SQLException {
		
		ConfirmComponent con = new ConfirmComponent();
		con.setQuestion(""+
				user.getTmpName(client.getUserId())+"さんの住まいは\n"+
				location.getJISName(user.getTmpJIS(client.getUserId()))+"\nでよろしいですか？");
		con.setYes("はい", "はい");
		con.setNo("いいえ", "いいえ");
		client.addReplyConfirm(con);
	}
	
	
	public void jouhou1(Client client) throws SQLException {
		
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
	public void jouhou2(Client client) throws SQLException {
		
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
	public void jouhou3(Client client) throws SQLException {
		
		KIND kd = user.getTmpKind(client.getUserId());
		ButtonComponent buttonComponent = new ButtonComponent();

		buttonComponent.setText(user.getTmpName(client.getUserId())+"さんにとって不要な情報をタップして「OFF」にしてください");
		
		String text1 = "注意報警報\t" + (kd.isAlart()?"ON":"OFF");
		buttonComponent.add(text1,"注意報");
		buttonComponent.add("次へ","次へ");

		client.addReplyButton(buttonComponent);				
	}
	
	
	public void jouhou_kakunin(Client client) throws SQLException {
		
		String jyusho = location.getJISName(user.getTmpJIS(client.getUserId()));
		client.addReplyText(user.getTmpInfo(client.getUserId(), jyusho));
		
		ConfirmComponent con = new ConfirmComponent();
		con.setQuestion("上記の内容で登録します。よろしいですか？");
		con.setYes("はい", "はい");
		con.setNo("いいえ", "いいえ");
		client.addReplyConfirm(con);	
	}
	
	public void modori1(Client client) {
		ButtonComponent buttonComponent = new ButtonComponent();

		buttonComponent.setText("キャンセルメニュー");
		
		buttonComponent.add("登録をつづける","つづける");
		buttonComponent.add("「情報選択」まで戻る","情報選択");
		buttonComponent.add("「住所登録」まで戻る","住所登録");
		buttonComponent.add("登録をやめる","やめる");

		client.addReplyButton(buttonComponent);	
	}
	
	public boolean regist(Client client) throws SQLException {
		int number = user.enmptyfriendNumber(client.getUserId());
				
		if(number<0) {return false;}
		
		String t_name = user.getTmpName(client.getUserId());
		int t_jis = user.getTmpJIS(client.getUserId());
		KIND t_kind = user.getTmpKind(client.getUserId());
		
		int friend_id = friend.insertFriendInfo(client.getUserId(), t_jis, t_name, t_kind);
		
		user.setFriendId(client.getUserId(), number, friend_id);
		
		return true;
		
	}
	public void regist_upper(Client client,int num) throws SQLException {
		
		if(num<1 || num>3 ) {num = 1;}

		String t_name = user.getTmpName(client.getUserId());
		int t_jis = user.getTmpJIS(client.getUserId());
		KIND t_kind = user.getTmpKind(client.getUserId());
		
		int friend_id = friend.insertFriendInfo(client.getUserId(), t_jis, t_name, t_kind);
		
		user.setFriendId(client.getUserId(), num, friend_id);
		
	}
	public void touroku_full(Client client) throws SQLException {
		ButtonComponent buttonComponent = new ButtonComponent();

		buttonComponent.setText("登録情報がいっぱいです。上書きする情報を選んでください。");
		
		int[] ids = user.getFriendIds(client.getUserId());
		String name1 = friend.getName(ids[0]);
		String name2 = friend.getName(ids[1]);
		String name3 = friend.getName(ids[2]);
		buttonComponent.add(name1,"1");
		buttonComponent.add(name2,"2");
		buttonComponent.add(name3,"3");
		buttonComponent.add("登録をやめる","やめる");

		client.addReplyButton(buttonComponent);
	}
	public void touroku_kanryou(Client client) {
		ConfirmComponent con = new ConfirmComponent();
		con.setQuestion("登録完了！\n続けて、他の大切な人を登録しますか？");
		con.setYes("はい", "はい");
		con.setNo("いいえ", "いいえ");
		client.addReplyConfirm(con);		
	}

	
	//
	//	汎用関数
	//
	private boolean isBigCity(String s) {
		int i = Integer.parseInt(s);
		i /= 1000;
		i %= 10;
		return (i == 1);
	}




}
