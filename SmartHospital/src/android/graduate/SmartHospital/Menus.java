package android.graduate.SmartHospital;


import java.io.*;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.graphics.*;
import android.net.*;
import android.os.Bundle;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class Menus extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus);
        
        SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
      	String id = null;
      	String h_name=null;
		try {
			id = new String(mysp.getString("patient_name", "").getBytes(),"UTF-8");
			h_name = new String(mysp.getString("hospital_name", "").getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Log.e("TEST","11"+id);
    	Log.e("TEST","22"+h_name);
    	
        String name = "김철수";
        String hname = "세인트사탄";
        String iday = "39";
        InteractionHttp ih = new InteractionHttp(this);
        String simpleinfo = id+"님 안녕하세요\n"+h_name+" 병원입니다.\n"+iday+"일째 입원중이십니다.";
        ih.execute();
        //String test = ih.hospital_name;
        //Log.e("TESTES", test);
        //TextView에 표시해줄 내용
        TextView tv = (TextView)findViewById(R.id.simpleinfo);
        tv.setText(simpleinfo);
        //tv.setText(test);
        
        
        findViewById(R.id.mystatbtn).setOnClickListener(myClickListener);
        findViewById(R.id.noticebtn).setOnClickListener(myClickListener);
        findViewById(R.id.requestbtn).setOnClickListener(myClickListener);
        findViewById(R.id.hospitalinfobtn).setOnClickListener(myClickListener);
        findViewById(R.id.qrreadbtn).setOnClickListener(myClickListener);
        findViewById(R.id.logoutbtn).setOnClickListener(myClickListener);
        findViewById(R.id.parentcallbtn).setOnClickListener(myClickListener);
        findViewById(R.id.emergencybtn).setOnClickListener(myClickListener);
        
    }
    
    Button.OnClickListener myClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			String parentPN; //보호자 전화번호
			/**각 버튼별로 해야할 일을 채워넣는다.
			 * 
			 */
			switch(v.getId()){
			case R.id.mystatbtn:
				intent = new Intent(Menus.this, Mystat.class);
				startActivity(intent);
				break;
			case R.id.noticebtn:
				intent = new Intent(Menus.this, Notice.class);
				startActivity(intent);
				break;
			case R.id.requestbtn:
				intent = new Intent(Menus.this, RequestTalk.class);
				startActivity(intent);
				break;
			case R.id.hospitalinfobtn:
				intent = new Intent(Menus.this, Hospitalinfo.class);
				startActivity(intent);
				break;
			case R.id.qrreadbtn:
				intent = new Intent(Menus.this, QRread.class);
				startActivity(intent);
				break;
			case R.id.logoutbtn:
				SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
				Editor editor = mysp.edit();
				editor.putString("patient_name", "");
				editor.putString("patient_pn", "");
				intent = new Intent(Menus.this, AppLogin.class);
				startActivity(intent);
				break;
			case R.id.parentcallbtn:
				parentPN = "01193699621";	//보호자 전화번호 DB에서 가져와야 함
				intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+parentPN));
				startActivity(intent);
				break;
			case R.id.emergencybtn:
				//병원에 긴급콜
				((Button)findViewById(R.id.emergencybtn)).setBackgroundColor(Color.GREEN);
				((Button)findViewById(R.id.emergencybtn)).setText("신고 완료");
				break;
			}
		}
	};
}