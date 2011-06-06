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
import android.os.*;
import android.util.*;

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
		
//		try {
//			Log.e("TESTES", getJson("http://excgate.jaram.org/hospital/api_test/","name",true));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			hospital_name = sendData("test","test location");
			Log.e("ENTITY", hospital_name);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//200이면 넘김
		return null;
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
        //JSONObject json = new
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
	
//    public static String getJson(String serverUrl, String postPara, boolean flagEncoding) throws Exception {
//        URL url = null;
//        HttpURLConnection conn = null;
//        PrintWriter postReq = null;
//        BufferedReader postRes = null;
//        StringBuilder json = null;
//        String line = null;
//        
//        json = new StringBuilder();
//        try {
//            if (flagEncoding) {
//                postPara = URLEncoder.encode(postPara);
//            }
//            
//            url = new URL(serverUrl);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "text/plain");
//            conn.setRequestProperty("Content-Length", 
//                                          Integer.toString(postPara.length()));
//            conn.setDoInput(true);
//            
//            postReq = new PrintWriter(
//                              new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
//            postReq.write(postPara);
//            postReq.flush();
//            
//            postRes = new BufferedReader(
//                             new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            while ((line = postRes.readLine()) != null){
//                json.append(line);
//            }
//            conn.disconnect();
//        } catch (MalformedURLException ex) {
//            throw new Exception(ex.getMessage());
//        } catch (IOException ex) {
//            throw new Exception(ex.getMessage());
//       } catch (Exception ex) {
//            throw new Exception(ex.getMessage());
//       }
//        return json.toString();    
//    }
    
	
}