package com.example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.define.StringW;

@Component
public class Location {

	@Autowired
	ModelBase modelbase;
	
	public List<StringW> getPrefectures() throws SQLException {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT DISTINCT prefecture_id, prefecture "
    			+ "FROM location ORDER BY prefecture_id"
    			);
		List<StringW> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new StringW(rs.getString("prefecture"),String.valueOf(rs.getInt("prefecture_id"))));
		}
		closeSR();
		return list;
	}
	
	public List<StringW> getCitys(int prefecture_id) throws SQLException {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT city_id, city "
    			+ "FROM location "
    			+ "WHERE prefecture_id = "+prefecture_id+""
    			);
		List<StringW> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new StringW(rs.getString("city"),rs.getString("city_id")));
		}
		closeSR();
		return list;
	}
	public List<StringW> serchCity(int prefecture_id, String cityname) throws SQLException {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT city_id, city "
    			+ "FROM location "
    			+ "WHERE prefecture_id = "+prefecture_id+" AND city LIKE '%"+cityname+"%'"
    			);
		List<StringW> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new StringW(rs.getString("city"),rs.getString("city_id")));
		}
		closeSR();
		return list;
	}
	//より厳格
	public List<StringW> serchCityG(int prefecture_id, String cityname) throws SQLException {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT city_id, city "
    			+ "FROM location "
    			+ "WHERE prefecture_id = "+prefecture_id+" AND city LIKE '"+cityname+"'"
    			);
		List<StringW> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new StringW(rs.getString("city"),rs.getString("city_id")));
		}
		closeSR();
		return list;
	}
	
	public List<StringW> serchPrefecture(String prefecturename) throws SQLException {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT DISTINCT prefecture_id, prefecture "
    			+ "FROM location "
    			+ "WHERE prefecture LIKE '%"+prefecturename+"%'"
    			);
		List<StringW> list = new ArrayList<>();
		
		while(rs.next()) {
			list.add(new StringW(rs.getString("prefecture"),String.valueOf(rs.getInt("prefecture_id"))));
		}
		closeSR();
		return list;
	}
	
    public int getNumOfSerchPrefecture(String name) throws SQLException {    	
    	return serchPrefecture(name).size();    	
    }
    public int getNumOfSerchCity(int prefecture_id, String name) throws SQLException {    	
    	return serchCity(prefecture_id, name).size();    	
    }
    //より厳格に
    public int getNumOfSerchCityG(int prefecture_id, String name) throws SQLException {    	
    	return serchCityG(prefecture_id, name).size();    	
    }
    
    public String getJISName(int id) throws SQLException {
    	ResultSet rs = getAll(id);
    	String ans = "該当住所なし";
    	while(rs.next()) {
    		ans = rs.getString("prefecture") + rs.getString("city");
    	}	
    	return ans;
    }

	
	
    //
    //	汎用関数
    //
	private ResultSet getAll(int id) {
		ResultSet rs = modelbase.getOneDB(""
    			+ "SELECT * "
    			+ "FROM location "
    			+ "WHERE location_id = "+id+""
    			);
		return rs;
	}
	
	private void closeSR() {
		modelbase.closeR();
		modelbase.closeS();
	}
}
