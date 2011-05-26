package android.graduate.SmartHospital;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class RequestTalk extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requesttalk);
        
        /**전에 로그인 한 정보가 있는지 검사
         * 정보가 있다면 정보를 통해서 바로 로그인
         * 없다면 로그인 창으로 
         */
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
				finish();
				break;
			case R.id.btnrequestexit:
				finish();
				break;
			}
		}
	};
}