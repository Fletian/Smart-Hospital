package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.database.sqlite.*;
import android.os.Bundle;
import android.preference.*;
import android.util.*;

public class SmartHospital extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent;
        /**전에 로그인 한 정보가 있는지 검사
         * 정보가 있다면 정보를 통해서 바로 로그인
         * 없다면 로그인 창으로 
         */
        
        Log.e("Context", this.getBaseContext().toString());
        //FromDB fdb = new FromDB(this);
        //boolean islogin = fdb.isLogin();
        if(!isQrread()){        	
	        if(!isLogin())
	        	intent = new Intent(SmartHospital.this, AppLogin.class);       
	        else
	        	intent = new Intent(SmartHospital.this, Menus.class);
        }
        else
        	intent = new Intent(SmartHospital.this, QRread.class);
        //intent = new Intent(SmartHospital.this, AppLogin.class);
		startActivity(intent);
    }
    
    private boolean isLogin(){
     	SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
      	String id = mysp.getString("patient_pn", "");
    	Log.e("TEST","!!!!"+id);
    	if(id.equals(""))
    		return false;
    	else{
    		return true;
    	}
    }
    private boolean isQrread(){
     	SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
      	String api = mysp.getString("hospital_api", "");
    	Log.e("TEST","!!!!"+api);
    	if(api.equals(""))
    		return false;
    	else{
    		return true;
    	}
    }
}