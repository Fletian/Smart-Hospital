package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class AppLogin extends Activity {
    /** Called when the activity is first created. */
	/**로그인 버튼 처리,확인은 다음으로 넘어감
	 * 취소는 초기화?? 어플 종료?? 로그인 정보 있으면 스킵*/
	protected String name;			//이름
	protected String medinum;		//의료보험번호
	protected String birthday;		//생년월일
	String QRcode = "1";	//QR코드 존재 확인을 위함  null로 바꿔줘야함
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        name = ((EditText)findViewById(R.id.loginName)).getText().toString();
        medinum = ((EditText)findViewById(R.id.loginMedinum)).getText().toString();
        birthday = ((EditText)findViewById(R.id.loginBrithday)).getText().toString();
        /** 버튼 누를때의 처리
         * 확인버튼은 name, medinum, birthday를 병원db로 보냄
         */
        
        Button okbtn = (Button)findViewById(R.id.logingOk);
        okbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				//QR코드를 입력한적이 있다면 메뉴로 없다면 QR코드 입력으로 감
				//QR합칠때 수정 필요함
				if(QRcode.equals(null)){
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