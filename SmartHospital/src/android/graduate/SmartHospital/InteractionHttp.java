package android.graduate.SmartHospital;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.json.*;

import android.app.Activity;
import android.content.*;
import android.content.res.*;
import android.database.sqlite.*;
import android.os.*;
import android.util.*;

public class InteractionHttp extends AsyncTask<Void, Void, Void> {
	String patient_name;
	String patient_pn;
	String caretaker_id;
	String hospitalized_date;
	
	String caretaker_sn;	
	String caretaker_phone;
	
	String hospital_name;
	String hospital_address;	
	
	String state_detail;
	String feeling_detail;
	
	String request_detail;
	String request_date;
	String finish_date;
	
	String hospital_api;
		
	
	DbHelper dbhelper;
	
	
	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			JSONArray json = new JSONArray(sendData("test","test location"));
			Log.e("json", json.toString());
			JSONObject temp = new JSONObject(json.getString(0));
			Log.e("temp", temp.toString());
			JSONObject api_for = new JSONObject(temp.getString("fields"));
			Log.e("fffff", api_for.toString());
			hospital_api = api_for.getString("api_address");
			Log.e("aaaaa", hospital_api);
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void saveDB(){		
		dbhelper = new DbHelper();
		SQLiteDatabase db;
		db = dbhelper.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS patients");
		db.execSQL("DROP TABLE IF EXISTS status_reports");
		db.execSQL("DROP TABLE IF EXISTS talk_requests");
		db.execSQL("DROP TABLE IF EXISTS caretakers");
		db.execSQL("DROP TABLE IF EXISTS hospitalinfo");
		String patients_temp = patient_name +"," + patient_pn+","+ caretaker_id+","+
						"null, null, null,"+hospitalized_date;
		String status_temp = state_detail +"," + feeling_detail;
		String requests_temp = request_detail +"," + request_date+","+ finish_date;
		String caretakers_temp = caretaker_sn +"," + caretaker_phone;
		String hospitalinfo_temp = hospital_name +"," + hospital_address;
		db.execSQL("INSERT INTO patients VALUES ("+ patients_temp+")");
		db.execSQL("INSERT INTO status_reports VALUES ("+ status_temp+")");
		db.execSQL("INSERT INTO talk_requests VALUES ("+ requests_temp+")");
		db.execSQL("INSERT INTO caretakers VALUES ("+ caretakers_temp+")");
		db.execSQL("INSERT INTO hospitalinfo VALUES ("+ hospitalinfo_temp+")");
	}

	private String sendData(String name, String address) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = makeHttpPost( name, address, "http://excgate.jaram.org/hospital/request_api/" ) ;  
          
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
  
    private HttpPost makeHttpPost(String $name, String $address, String $url) throws IllegalStateException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( $url ) ;  
        
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "name", $name ) ) ;  
        nameValue.add( new BasicNameValuePair( "address", $address ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        Log.e("ENTITY", nameValue.toString());
        return request ;  
    }  
      
    private HttpEntity makeEntity( Vector<NameValuePair> $nameValue ) {  
        HttpEntity result = null ;  
        try {  
            result = new UrlEncodedFormEntity( $nameValue , "UTF-8") ;  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return result ;  
    }  
}