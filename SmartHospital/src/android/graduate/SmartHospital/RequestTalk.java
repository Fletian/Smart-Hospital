package android.graduate.SmartHospital;

import java.io.*;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class RequestTalk extends Activity {
    /** Called when the activity is first created. */
	String talk;
	TextView requesttalk;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requesttalk);
        
        /**전에 로그인 한 정보가 있는지 검사
         * 정보가 있다면 정보를 통해서 바로 로그인
         * 없다면 로그인 창으로 
         */
        SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
        try {
			talk = new String(mysp.getString("request_detail", "").getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        requesttalk = (TextView)findViewById(R.id.requesttext);
        requesttalk.setText(talk);
        
        findViewById(R.id.btnrequestsave).setOnClickListener(myClickListener);
        findViewById(R.id.btnrequestexit).setOnClickListener(myClickListener);
        
    }
    Button.OnClickListener myClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btnrequestsave:
				//상태들을 DB로 저장해야함
				SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
				Editor editor = mysp.edit();
				requesttalk = (TextView)findViewById(R.id.requesttext);
				talk = requesttalk.getText().toString();
				editor.putString("request_detail", talk);
				editor.commit();
				//editor.putString("request_date", myEmotion);
				finish();
				break;
			case R.id.btnrequestexit:
				finish();
				break;
			}
		}
	};
}