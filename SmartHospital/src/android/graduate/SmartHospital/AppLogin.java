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
	protected String pre_num;		//주민번호
	protected String post_num;
	//String QRcode = "1";	//QR코드 존재 확인을 위함  null로 바꿔줘야함
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        
        //birthday = ((EditText)findViewById(R.id.loginBrithday)).getText().toString();
        /** 버튼 누를때의 처리
         * 확인버튼은 name, securityid를 병원db로 보냄
         */
        final EditText first_num=((EditText)findViewById(R.id.loginPreNum));
        final EditText second_num=((EditText)findViewById(R.id.loginPostNum));
        first_num.setOnKeyListener(new OnKeyListener() {
		
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(first_num.getText().length() == 6)
					second_num.requestFocus();
				return false;
			}
		});
        //((EditText)findViewById(R.id.loginPostNum)).getText().toString();
        final InteractionHttp ih = new InteractionHttp(this,"LOGIN");
        //final FromDB fdb = new FromDB(this);
        final SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
        name = mysp.getString("patient_name", "");
        if(!name.equals(""))
        	((EditText)findViewById(R.id.loginName)).setText(name);
        Button okbtn = (Button)findViewById(R.id.logingOk);
        okbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				//QR코드를 입력한적이 있다면 메뉴로 없다면 QR코드 입력으로 감
				//QR합칠때 수정 필요함
				name = "";
				pre_num ="";
				post_num="";
				name = ((EditText)findViewById(R.id.loginName)).getText().toString();
				pre_num = ((EditText)findViewById(R.id.loginPreNum)).getText().toString();
		        post_num = ((EditText)findViewById(R.id.loginPostNum)).getText().toString();
				Editor editor = mysp.edit();
				editor.putString("patient_name", name);
				editor.putString("patient_pn", pre_num+"-"+post_num);
				editor.commit();
				ih.execute();
				
				intent = new Intent(AppLogin.this, Menus.class);
				startActivity(intent);
				
			}
		});
    }
}