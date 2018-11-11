package com.example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.define.KIND;
import com.example.define.STATUS;

@Component
public class Friend {
	
	@Autowired
	ModelBase modelbase;

    public int insertFriendInfo(String user_id,int locate_id,String name,KIND k) throws SQLException {
        
    	int kind = k.toInt();
    	int friend_id = -1;
    	
    	modelbase.setOneDB(
		    "INSERT INTO friend (user_id, locate_id, name, kind ) "
		    +"VALUES ('"+user_id+"',"+locate_id+",'"+name+"',"+kind+");"
		);
    	
    	ResultSet rs = modelbase.getOneDB(""+
    			"SELECT * FROM friend "+
    			"WHERE user_id = '"+user_id+"' "+
    			"AND locate_id = "+locate_id+" "+
    			"AND name = '"+name+"' "+
    			"AND kind = "+kind+" ");
    	if(rs.next()) {
    		friend_id = rs.getInt("id");
    	}
    	closeSR();
    	return friend_id;
    	
    }
    
    public String getName(int id) throws SQLException {
    	ResultSet rs = getAll(id);
    	String str = "";
    	if(rs.next()) {
    		str = rs.getString("name");
    	}
    	closeSR();
    	return str;   	
    }
    public KIND getKind(int id) throws SQLException {
    	ResultSet rs = getAll(id);
    	KIND ans = new KIND(0);
    	if(rs.next()) {
    		ans = new KIND(rs.getInt("kind"));
    	}
    	closeSR();
    	return ans;   	
    }
    public int getLocate(int id) throws SQLException {
    	ResultSet rs = getAll(id);
    	int ans=0;
    	if(rs.next()) {
    		ans = rs.getInt("locate_id");
    	}
    	closeSR();
    	return ans;   	
    }
    public void setKind(int id,KIND kind) {
    	modelbase.setOneDB(
    			"UPDATE friend "
    			+"SET kind = "+kind.toInt()+" " 
    			+"WHERE id = "+id+""
    		);
    }
    
    //
    //	汎用関数
    //
	private ResultSet getAll(int id) {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT * "
    			+ "FROM friend "
    			+ "WHERE id = "+id+""
    			);
		return rs;
	}
	
	private void closeSR() {
		modelbase.closeR();
		modelbase.closeS();
	}
    

    


}