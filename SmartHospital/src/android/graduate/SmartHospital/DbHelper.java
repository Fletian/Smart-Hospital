package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.database.sqlite.*;

public class DbHelper extends SQLiteOpenHelper{

	public DbHelper(){
		super( null, "SmartHospital.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE patients(name TEXT, security_number TEXT, " +
				"caretaker_id INTEGER, treatment_status TEXT, age INTEGER, blood_type TEXT, hospitalized_date DATETIME);");
		db.execSQL("CREATE TABLE status_reports(state_detail TEXT, feeling_detail TEXT);");
		db.execSQL("CREATE TABLE talk_requests(request_detail TEXT, request_date DATETIME, finish_date DATETIME);");
		db.execSQL("CREATE TABLE caretakers(security_number TEXT, contact TEXT);");
		db.execSQL("CREATE TABLE hospitalinfo(name TEXT, address TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS patients");
		
	}	
}