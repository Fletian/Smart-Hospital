package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.database.sqlite.*;

public class DbHelper extends SQLiteOpenHelper{

	public DbHelper(Context context){
		super( context, "SmartHospital.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS patients(name TEXT, security_number TEXT, " +
				"caretaker_id INTEGER, treatment_status TEXT, age INTEGER, blood_type TEXT, hospitalized_date DATETIME);");
		db.execSQL("CREATE TABLE IF NOT EXISTS status_reports(state_detail TEXT, feeling_detail TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS talk_requests(request_detail TEXT, request_date DATETIME, finish_date DATETIME);");
		db.execSQL("CREATE TABLE IF NOT EXISTS caretakers(security_number TEXT, contact TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS hospitalinfo(name TEXT, address TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS patients;");
		db.execSQL("DROP TABLE IF EXISTS status_reports;");
		db.execSQL("DROP TABLE IF EXISTS talk_requests;");
		db.execSQL("DROP TABLE IF EXISTS caretakers;");
		db.execSQL("DROP TABLE IF EXISTS hospitalinfo;");
		
		
	}	
}