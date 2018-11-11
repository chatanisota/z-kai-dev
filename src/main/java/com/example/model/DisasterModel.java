package com.example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.define.DisasterComponent;
import com.example.define.KIND;
import com.example.define.STATUS;

@Component
public class DisasterModel {
	
	@Autowired
	ModelBase modelbase;

    public void insertDisasterInfo(String user_id,String mini_report,String report,int sended,Timestamp timestamp) throws SQLException {

    	modelbase.setOneDB(
		    "INSERT INTO disaster (user_id, mini_report, report, sended, timestamp ) "
		    +"VALUES ('"+user_id+"','"+mini_report+"','"+report+"',"+sended+",'"+timestamp+"');"
		);	
    }
    
    public List<DisasterComponent> getDisaster(String user_id) throws SQLException {
    	ResultSet rs = getAll(user_id);
    	List<DisasterComponent> list = new ArrayList<>();
    	if(rs.next()) {
    		 list.add(new DisasterComponent(rs.getInt("id"),rs.getString("mini_report")));
    	}
    	closeSR();
    	
    	modelbase.setOneDB(
    			"UPDATE disaster "
    			+"SET sended = "+1+" "
    			+"WHERE user_id = '"+user_id+"'"
    	);
    	
    	return list;   	
    }
    
    //
    //	汎用関数
    //
	private ResultSet getAll(String user_id) {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT * "
    			+ "FROM disaster "
    			+ "WHERE user_id = '"+user_id+"' AND sended = 0 "
    			+ "ORDER BY timestamp DESC LIMIT 4"
    			);
		return rs;
	}
	
	private void closeSR() {
		modelbase.closeR();
		modelbase.closeS();
	}

	public String getMore(int id) throws SQLException {
		String ans = "";
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT report "
    			+ "FROM disaster "
    			+ "WHERE id = "+id
    			);
		if(rs.next()) {
			ans = rs.getString("report");
		}
		closeSR();
		return ans;
	}
	


    


}