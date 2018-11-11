package com.example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.define.KIND;
import com.example.define.STATUS;

@Component
public class User {
	
	@Autowired
	ModelBase modelbase;
	@Autowired
	Location location;
	@Autowired
	Friend friend;
	
	//
	//	状態の取得・変更
	//
	public STATUS getUserStatus(String user_id) throws SQLException {
		
		ResultSet rs = this.getAll(user_id);
		
		if(rs.next()) {
			//userデータがある場合
			int t1 = rs.getInt("status");
			closeSR();
			return STATUS.getSTATUS(t1);
		}else {
			closeSR();
			//userデータがない場合
			modelbase.setOneDB(
				    "INSERT INTO users (user_id, status) "
				    +"VALUES ('"+user_id+"',"+STATUS.init.getId()+") "
				);			
		}
		return STATUS.hello;	
	}	
    public void setUserStatus(String user_id,STATUS st) {   	
    	modelbase.setOneDB(
		    "INSERT INTO users (user_id, status) "
		    +"VALUES ('"+user_id+"',"+st.getId()+") "
		    +"ON CONFLICT (user_id) "
		    +"DO UPDATE SET status = "+st.getId()
		);
    }
	
    //
    //	登録時一時保管場所
    //
    public void setTmpName(String userId, String nm) {    	
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET tmp_name = '"+nm+"' " 
    			+"WHERE user_id = '"+userId+"'"
    		);
    }
    public void setTmpPrefecture(String userId, int id) {    	
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET tmp_prefecture_id = "+id+" " 
    			+"WHERE user_id = '"+userId+"'"
    		);
    }
    public void setTmpCity(String userId, int id) {    	
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET tmp_city_id = "+id+" " 
    			+"WHERE user_id = '"+userId+"'"
    		);
    }
    public void setFriendId(String userId, int num, int id) {    	
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET friend"+num+"_id = "+id+" " 
    			+"WHERE user_id = '"+userId+"'"
    		);
    }
    public void setTmpFriendId(String userId, int id) {
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET tmp_friend_id = "+id+" " 
    			+"WHERE user_id = '"+userId+"'"
    		);	
	}
    public void setTmpKind(String userId, KIND kd) { 
    	int id = kd.toInt();
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET tmp_kind = "+id+" " 
    			+"WHERE user_id = '"+userId+"'"
    		);
    }
    public String getTmpName(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	String ans = "";
    	if(rs.next()) {
	    	ans = rs.getString("tmp_name");	    	
    	}
    	closeSR();
    	return ans;
    }
    public int getTmpPrefecture(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	int ans = 1;
    	if(rs.next()) {
	    	ans = rs.getInt("tmp_prefecture_id");	    	
    	}
    	closeSR();
    	return ans;
    }
    public int getTmpCity(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	int ans = 100;
    	if(rs.next()) {
	    	ans = rs.getInt("tmp_city_id");	    	
    	}
    	closeSR();
    	return ans;
    }
    public int getTmpJIS(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	int ans = 100;
    	if(rs.next()) {
	    	ans = rs.getInt("tmp_prefecture_id")*100000+rs.getInt("tmp_city_id");	    	
    	}
    	closeSR();
    	return ans;  	
    }
    public KIND getTmpKind(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	int tmp = 0;
    	if(rs.next()) {
	    	tmp = rs.getInt("tmp_kind");	    	
    	}
    	closeSR();
    	KIND ans = new KIND(tmp);
    	return ans;
    }
    public int[] getFriendIds(String userId) throws SQLException {
    	int[] ans = {-1,-1,-1};
    	ResultSet rs = getAll(userId);
    	
    	if(rs.next()) {
	    	ans[0] = rs.getInt("friend1_id");
	    	ans[1] = rs.getInt("friend2_id");
	    	ans[2] = rs.getInt("friend3_id");
    	}
    	closeSR();
    	return ans;
    }
    public int getTmpFriendId(String userId) throws SQLException {
    	ResultSet rs = getAll(userId);
    	int ans = -1;
    	if(rs.next()) {
	    	ans = rs.getInt("tmp_friend_id");	    	
    	}
    	closeSR();
    	return ans;  	
    }
    
    public String getTmpInfo(String userId,String jyusho) throws SQLException {
    	ResultSet rs = getAll(userId);
    	String ans = "";
    	
    	if(rs.next()) {  
	    	KIND kd = new KIND(rs.getInt("tmp_kind")); 		
	    	ans += "名前:\t"+rs.getString("tmp_name")+"\n";
	    	ans += "住所:\t"+jyusho+"\n";
	    	ans += "■■通知設定■■■■■■■\n";
	    	ans += "地震:　　\t"+tuuchi(kd.isWave())+"\n";
	    	ans += "津波:　　\t"+tuuchi(kd.isWave())+"\n";
	    	ans += "台風:　　\t"+tuuchi(kd.isTyphoon())+"\n";
	    	ans += "大雨:　　\t"+tuuchi(kd.isRain())+"\n";
	    	ans += "河川洪水:\t"+tuuchi(kd.isRiver())+"\n";
	    	ans += "土砂災害:\t"+tuuchi(kd.isSand())+"\n";
	    	ans += "避難生活:\t"+tuuchi(kd.isLifeline())+"\n";
	    	ans += "警報:　　\t"+tuuchi(kd.isAlart())+"\n";   
	    	ans += "注意報:　\t"+tuuchi(kd.isAlart())+"\n";
	    	ans += "避難指示:\t"+tuuchi(kd.isAlart())+"\n";
	    	ans += "■■■■■■■■■■■■■■■";
    	}
    	closeSR();   	
    	return ans;
    }
    
    public int enmptyfriendNumber(String user_id) throws SQLException {
    	ResultSet rs = getAll(user_id);
    	if(rs.next()) {
    		if(rs.getInt("friend1_id")<0 ) {return 1;}
    		if(rs.getInt("friend2_id")<0 ) {return 2;}
    		if(rs.getInt("friend3_id")<0 ) {return 3;}
    	}
    	closeSR();
    	return -1;
    }
    
    public Timestamp getTimeStamp(String user_id) throws SQLException {
    	ResultSet rs = getAll(user_id);
    	Timestamp ans = new Timestamp(System.currentTimeMillis());//初期値
    	if(rs.next()) {
    		ans = rs.getTimestamp("timestamp");
    	}
    	closeSR();
    	
    	modelbase.setOneDB(
    			"UPDATE users "
    			+"SET timestamp = '"+new Timestamp(System.currentTimeMillis())+"' "
    			+"WHERE user_id = '"+user_id+"'"
    		);
    	
    	
    	return ans;
    }
    

    

    
    //
    //	汎用関数
    //
	private ResultSet getAll(String user_id) {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT * "
    			+ "FROM users "
    			+ "WHERE user_id = '"+ user_id+"'"
    			);
		return rs;
	}
	private String tuuchi(boolean b) {
		if(b) {
			return "通知あり";
		}else {
			return " - ";
		}
	}
	
	private void closeSR() {
		modelbase.closeR();
		modelbase.closeS();
	}
	
	//
	// 試験運用
	//
	public List<String> getAllUserIds() throws SQLException{
		List<String> ls = new ArrayList<>();
		
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT * "
    			+ "FROM users "
    			);
		while(rs.next()) {
			ls.add(rs.getString("user_id"));
		}
		return ls;
	}
	
}