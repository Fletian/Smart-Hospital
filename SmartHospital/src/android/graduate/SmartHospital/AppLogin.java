package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class AppLogin extends Activity {
    /** Called when the activity is first created. */
	/**로그인 버튼 처리,확인은 다음으로 넘어감
	 * 취소는 초기화?? 어플 종료?? 로그인 정보 있으면 스킵*/
	protected String name;			//이름
	protected String security_num;		//주민번호
	//String QRcode = "1";	//QR코드 존재 확인을 위함  null로 바꿔줘야함
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        
        //birthday = ((EditText)findViewById(R.id.loginBrithday)).getText().toString();
        /** 버튼 누를때의 처리
         * 확인버튼은 name, medinum, birthday를 병원db로 보냄
         */
        //final FromDB fdb = new FromDB(this);
        final SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
        Button okbtn = (Button)findViewById(R.id.logingOk);
        okbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				//QR코드를 입력한적이 있다면 메뉴로 없다면 QR코드 입력으로 감
				//QR합칠때 수정 필요함
				name = ((EditText)findViewById(R.id.loginName)).getText().toString();
		        security_num = ((EditText)findViewById(R.id.loginMedinum)).getText().toString();
				Editor editor = mysp.edit();
				editor.putString("patient_name", name);
				Log.e("TEST","3"+name);
				editor.putString("patient_pn", security_num);
				editor.commit();
				String h_name = mysp.getString("hospital_name", "");
				Log.e("TEST","2"+h_name);
				if(h_name.equals("")){
					intent = new Intent(AppLogin.this, QRread.class);
					startActivity(intent);
				}
				else{
					intent = new Intent(AppLogin.this, Menus.class);
					startActivity(intent);
				}
			}
		});
    }
}