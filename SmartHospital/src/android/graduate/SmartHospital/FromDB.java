package android.graduate.SmartHospital;

import android.app.Activity;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.*;
import android.util.*;

public class FromDB extends AsyncTask<Void, Void, Void>{
    /** Called when the activity is first created. */
	DbHelper dbhelper;
	SQLiteDatabase db;
	Cursor cursor;
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	protected boolean isLogin(){
		boolean result = false;
		dbhelper = new DbHelper();
		db = dbhelper.getReadableDatabase();
		cursor = db.rawQuery("SELECT name, security_number FROM patients", null);
		if( (cursor.getString(0)).equals(null) )
			result = false;
		else
			result = true;
		
		dbhelper.close();
		return result;
	}
	protected boolean isQR(){
		boolean result = false;
		dbhelper = new DbHelper();
		db = dbhelper.getReadableDatabase();
		cursor = db.rawQuery("SELECT name, address FROM hospitalinfo", null);
		if( (cursor.getString(0)).equals(null) )
			result = false;
		else
			result = true;
		
		dbhelper.close();
		return result;
	}
	
	protected void hospital_insert(String name, String address){
		boolean result = false;
		dbhelper = new DbHelper();
		db = dbhelper.getWritableDatabase();
		db.execSQL("INSERT INTO hospitalinfo VALUES ("+ name+","+address+")");				
		dbhelper.close();
		Log.e("dbhelper","Insert success");
	}
   
}

