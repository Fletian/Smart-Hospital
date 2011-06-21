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
import android.content.SharedPreferences.Editor;
import android.content.res.*;
import android.database.sqlite.*;
import android.os.*;
import android.preference.*;
import android.text.*;
import android.util.*;

public class InteractionHttp extends AsyncTask<Void, Void, Void> {
	String patient_name;
	String patient_pn;
	String secret_key;
	String treatment_status;
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
	Context context;
	String flag;
	
	public InteractionHttp(Context ct, String t_flag){
		context = ct;
		flag = t_flag;
	}
	@Override
	protected Void doInBackground(Void... params) {
		try {
			SharedPreferences mysp = context.getSharedPreferences("mySP", context.MODE_PRIVATE);
			Editor editor = mysp.edit();
			hospital_api = mysp.getString("hospital_api", "");
			
			if(flag.equals("QR")){
				JSONArray json = new JSONArray(sendData("test","test location"));
				JSONObject temp = new JSONObject(json.getString(0));
				JSONObject api_for = new JSONObject(temp.getString("fields"));
				hospital_api = api_for.getString("api_address");
				Log.e("aaaaa", hospital_api);
				editor.putString("hospital_api", hospital_api);
			}
			else if(flag.equals("LOGIN")){				
				patient_name = mysp.getString("patient_name", "");
				patient_pn = mysp.getString("patient_pn", "");
				JSONObject loginJson = new JSONObject(sendLoginData(patient_name, patient_pn));
				Log.e("inter", loginJson.toString());
				JSONObject loginfields = new JSONObject(loginJson.getString("fields"));
				secret_key = loginfields.getString("secret_key");
				caretaker_id = loginfields.getString("caretaker");
				hospitalized_date = loginfields.getString("hospitalized_date");
				treatment_status = loginfields.getString("treatment_status");
				
				editor.putString("secret_key", secret_key);
				editor.putString("caretaker_id", caretaker_id);
				editor.putString("hospitalized_date", hospitalized_date);
				editor.putString("treatment_status", treatment_status);
				Log.e("inter", treatment_status);
			}
			else if(flag.equals("STATUS")){
				secret_key = mysp.getString("secret_key", "");
				state_detail = mysp.getString("state_detail", "");
				feeling_detail = mysp.getString("feeling_detail", "");
				
				JSONObject loginfields = new JSONObject(sendStatus(secret_key, state_detail, feeling_detail));
			}
			else if(flag.equals("TALK")){
				secret_key = mysp.getString("secret_key", "");
				request_detail = mysp.getString("request_detail", "");
				
				JSONObject loginfields = new JSONObject(sendTalkReq(secret_key,request_detail));
			}
			else if(flag.equals("EMER")){
				secret_key = mysp.getString("secret_key", "");
				
				JSONObject loginfields = new JSONObject(sendEmergency(secret_key));
			}
			else if(flag.equals("MASSAGE")){
				secret_key = mysp.getString("secret_key", "");
				
				JSONObject loginfields = new JSONObject(sendMessageReq(secret_key));
			}
			
			
			
			//saveDatas(context);
			
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
	private void saveDatas(Context context){
		SharedPreferences mysp = context.getSharedPreferences("mySP", context.MODE_PRIVATE);
		Editor editor = mysp.edit();
		editor.putString("hospital_api", hospital_api);
		String id = mysp.getString("patient_name", "");
		//Log.e("TEST","ddsgwwww"+id);
    	if(id.equals("")){
    		//Log.e("TEST","drrrrrrr"+id);
			editor.putString("patient_name", patient_name);
			editor.putString("patient_pn", patient_pn);
			editor.putString("caretaker_id", caretaker_id);
			editor.putString("hospitalized_date", hospitalized_date);
			
			editor.putString("caretaker_sn", caretaker_sn);
			editor.putString("caretaker_phone", caretaker_phone);
			
			editor.putString("hospital_name", hospital_name);
			editor.putString("hospital_address", hospital_address);
			
			editor.putString("state_detail", state_detail);
			editor.putString("feeling_detail", feeling_detail);
			
			editor.putString("request_detail", request_detail);
			editor.putString("request_date", request_date);
			editor.putString("finish_date", finish_date);
	    	
			editor.commit();
    	}
	}
	private String sendLoginData(String $name, String $number) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/admin/request_patient/" ) ;   
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "name", $name ) ) ;  
        nameValue.add( new BasicNameValuePair( "security_number", $number ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	
	private String sendStatus(String $key,String $body,String $mind) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/report/write_status/" ) ;         
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "secret_key", $key ) ) ;  
        nameValue.add( new BasicNameValuePair( "body_status", $body ) ) ;  
        nameValue.add( new BasicNameValuePair( "mind_status", $mind ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	private String sendTalkReq(String $key,String $detail) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/report/write_talk/" ) ;         
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "secret_key", $key ) ) ;  
        nameValue.add( new BasicNameValuePair( "detail", $detail ) ) ;
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	
	private String sendMessageReq(String $key) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/amdin/request_message/" ) ;         
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "secret_key", $key ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	private String sendCaretaker(String $key) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/admin/request_caretaker/" ) ;         
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "secret_key", $key ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	private String sendEmergency(String $key) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( "http://"+hospital_api+"/admin/request_emergency/" ) ;      
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "secret_key", $key ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	private String sendData(String $name, String $address) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost("http://excgate.jaram.org/hospital/request_api/" ) ;  
        
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "name", $name ) ) ;  
        nameValue.add( new BasicNameValuePair( "address", $address ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
	
    /*private HttpPost makeHttpPost(String $name, String $address, String $url) throws IllegalStateException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( $url ) ;  
        
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        nameValue.add( new BasicNameValuePair( "name", $name ) ) ;  
        nameValue.add( new BasicNameValuePair( "address", $address ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        Log.e("ENTITY", nameValue.toString());
        return request ;  
    }  */
      
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