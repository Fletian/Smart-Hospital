package android.graduate.SmartHospital;

import org.json.*;

import android.graduate.SmartHospital.IntentIntegrator;
import android.graduate.SmartHospital.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.SharedPreferences.*;
//import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
import android.util.*;

public class QRread extends Activity {
    /** Called when the activity is first created. */
	String name;
	String address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrread);
//        Button button = (Button) findViewById(R.id.qrBt);
//        button.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
				// QR�ڵ�/���ڵ� ��ĳ�ʸ� �����մϴ�.
				IntentIntegrator.initiateScan(QRread.this);
//			}
//		});
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// QR�ڵ�/���ڵ带 ��ĵ�� ��� ���� �����ɴϴ�.
		boolean ff = false;
		
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		
		//Log.e("err", result.getContents());
		JSONObject qrtext;
		try {
			
			qrtext = new JSONObject(result.getContents());
			
			name = qrtext.getString("name");
			address = qrtext.getString("address");
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
	 		e.printStackTrace();
		}
		//TextView t = (TextView)findViewById(R.id.qrTxt);
		//t.setText(qrtext);
		if(result!=null){
			ff=true;
		}
		// ��� ���
		if(ff){
			//FromDB fdb = new FromDB(this);
			//fdb.hospital_insert(name, address);
			//Log.e("QRREAD", name+","+address);
			SharedPreferences mysp = getSharedPreferences("mySP", MODE_PRIVATE);
			Editor editor = mysp.edit();
			editor.putString("hospital_name", name);
			Log.e("TEST",name);
			editor.putString("hospital_address", address);
			editor.commit();
			Intent intent;
			if(mysp.getString("patient_name", "").equals(""))
				intent = new Intent(QRread.this, AppLogin.class);
			else
				intent = new Intent(QRread.this, Menus.class);
			InteractionHttp ih = new InteractionHttp(this, "QR");
			ih.execute();
			startActivity(intent);
			finish();
		}
//		if(ff){
//			new AlertDialog.Builder(this)
//				.setTitle("QR Code Text")
//				.setMessage(result.getContents() + " [" + result.getFormatName() + "]")
//				.setPositiveButton("확인", new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which)
//					{
//						dialog.dismiss();
//					}
//				})
//				.show();
//		}
//		if(ff){	//parsing �ؼ� �ּ� �ѱ�
//			Intent aa = new Intent(Intent.ACTION_WEB_SEARCH);
//			aa.setData(Uri.parse("http://google.com"));	//���ͳ� ���� �׽�Ʈ��
//			startActivity(aa);
//		}
	}
}