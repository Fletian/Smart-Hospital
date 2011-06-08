package android.graduate.SmartHospital;

import java.io.*;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class Mystat extends Activity {
    /** Called when the activity is first created. */
	String myBodyStat;
	String myEmotion;
	EditText bodystat;
	EditText emotionstat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mystat);
        
        SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
        //상태들을 DB로부터 받아와야 함
        try {
			myBodyStat = new String(mysp.getString("state_detail", "").getBytes(),"UTF-8");
			myEmotion = new String(mysp.getString("feeling_detail", "").getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //상태를 EditText에 표시
           
        bodystat = (EditText)findViewById(R.id.bodystat);
        emotionstat = (EditText)findViewById(R.id.emotionstat);    
        bodystat.setText(myBodyStat);
        emotionstat.setText(myEmotion);
        findViewById(R.id.btnstatsave).setOnClickListener(myClickListener);
        findViewById(R.id.btnstatexit).setOnClickListener(myClickListener);
        
    }
    Button.OnClickListener myClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btnstatsave:
				//상태들을 DB로 저장해야함
				bodystat = (EditText)findViewById(R.id.bodystat);
		        emotionstat = (EditText)findViewById(R.id.emotionstat);     
				myBodyStat = bodystat.getText().toString(); 
				myEmotion = emotionstat.getText().toString();
				SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
				Editor editor = mysp.edit();
				editor.putString("state_detail", myBodyStat);
				editor.putString("feeling_detail", myEmotion);
				editor.commit();
				finish();
				break;
			case R.id.btnstatexit:
				finish();
				break;
			}
		}
	};
}