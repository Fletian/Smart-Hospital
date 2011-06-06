package android.graduate.SmartHospital;

import java.io.*;
import java.util.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import android.app.Activity;
import android.content.*;
import android.content.res.*;
import android.os.*;

public class InteractionHttp extends AsyncTask<Void, Void, Void> {
	String patient_name;
	String patient_pn;
	String caretaker_phone;
	
	String hospital_name;
	String hospital_address;
	String hospitalized_date;
	
	String body_stats;
	String emotions;
	
	String question;
		
	
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		//HttpPost request = makeHttpPost(url);
		//200이면 넘김
		return null;
	}

	private String sendData(String name, String sex) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = makeHttpPost( name, sex, "http://excgate.jaram.org/hospital/api_test/" ) ;  
          
        HttpClient client = new DefaultHttpClient() ;  
        ResponseHandler<string> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result ;  
    }  
  
    private HttpPost makeHttpPost(String $name, String $sex, String $url) {  
        // TODO Auto-generated method stub  
        HttpPost request = new HttpPost( $url ) ;  
        Vector<namevaluepair> nameValue = new Vector<namevaluepair>() ;  
        nameValue.add( new BasicNameValuePair( "name", $name ) ) ;  
        nameValue.add( new BasicNameValuePair( "sex", $sex ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        return request ;  
    }  
      
    private HttpEntity makeEntity( Vector<namevaluepair> $nameValue ) {  
        HttpEntity result = null ;  
        try {  
            result = new UrlEncodedFormEntity( $nameValue ) ;  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return result ;  
    }  
    
	
}