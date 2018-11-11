package com.example.snipet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.define.KIND;

public final class AccessAPI {

    private static final String API_URL  = "http://api.aitc.jp/jmardb-api/search?";    //叩くAPI
    private static URL               url = null;
    private static BufferedReader    br  = null;
    private static InputStreamReader isr = null;

    /* 実際にAPIを叩いて得たJSONを文字列として返す 
       適当に組んでいるのでエラーは投げてます
       当然のことながら実際に実装する際は各自で適切な処理を*/
    public static String getAPI(String urlstr){
        try {	
			url = new URL( urlstr );	
			String readLine     = "";
			String writeContent = "";
		
		    /* ここから読み込むための定型文 */
			isr = new InputStreamReader( url.openStream() );
			br  = new BufferedReader( isr );
			while ( ( readLine = br.readLine() ) != null ) writeContent = writeContent + readLine;//ここで読み込んだ内容を変数に保存している
			return writeContent;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	

	public static String getAPIFromAITC(String areacode, KIND kind, Timestamp prevtime) {
		
    	Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        String timestr1 = sdf.format(timestamp1);
        String timestr2 = sdf.format(prevtime);
        //http://api.aitc.jp/jmardb-api/search?areacode=2520200&order=new
        String st = API_URL;
        
        st += "&datetime="+timestr1;
        st += "&datetime="+timestr2;
        st += "&areacode="+areacode;
        st += "&areacode="+areacode.substring(0,2)+"0000";
        
        
        if( kind.isAlart() ) {st += "&title=気象警報・注意報（Ｈ２７）";} 
        if( kind.isSand() ) {st += "&title=土砂災害警戒情報";}
        if( kind.isRiver() ) {st += "&title=指定河川洪水予報";}
        if( kind.isRain() ) {st += "&title=記録的短時間大雨情報";}
        
        st += "&order=new";
        st += "&limit=2";
        
        System.out.println("<<API>>"+st);
              
        return getAPI(st);
	}
}

