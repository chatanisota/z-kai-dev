package com.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

@Component
public class ModelBase {
	
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

	public void initDB() {
		
        try {
			connection = DriverManager.getConnection("jdbc:postgresql://ec2-107-20-211-10.compute-1.amazonaws.com:5432/de299en8to37r2?sslmode=require", // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]"
			        "sjcmdxqqncnvor", // ログインロール
			        "b84581c7f555446754248910c4ef83839eb011a60704fbcd90314cd757c749ee");//パスワード
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public ResultSet getOneDB(String st) {
		
		try {
			
			statement = connection.createStatement();
			
			System.out.println("<<SQL>>:"+st);
			
			resultSet = statement.executeQuery(st);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return resultSet;
	}
	
	public void setOneDB(String st) {
		
		try {
			statement = connection.createStatement();
			
			System.out.println("<<SQL>>:"+st);
			
			statement.execute(st);
			
			closeS();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeDB() {

	    if (connection != null) {
	        try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public void closeS() {
	    if (statement != null) {
	        try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	public void closeR() {
	    if (resultSet != null) {
	        try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	

}
