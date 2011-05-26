package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;

public class SmartHospital extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**전에 로그인 한 정보가 있는지 검사
         * 정보가 있다면 정보를 통해서 바로 로그인
         * 없다면 로그인 창으로 
         */
     
        Intent intent = new Intent(SmartHospital.this, AppLogin.class);
		startActivity(intent);
    }
}