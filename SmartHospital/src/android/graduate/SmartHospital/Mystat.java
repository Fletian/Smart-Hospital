package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
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
        
        //상태들을 DB로부터 받아와야 함
        myBodyStat = "";
        myEmotion = "";
        
        //상태를 EditText에 표시
        bodystat = (EditText)findViewById(R.id.bodystat);
        emotionstat = (EditText)findViewById(R.id.emotionstat);        
        
        
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
				myBodyStat = bodystat.getText().toString(); 
				myEmotion = emotionstat.getText().toString();
				finish();
				break;
			case R.id.btnstatexit:
				finish();
				break;
			}
		}
	};
}