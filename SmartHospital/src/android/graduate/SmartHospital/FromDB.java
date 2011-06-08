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
	Context ct;
	
	public FromDB(Context context){		
		dbhelper = new DbHelper(context);
		//dbhelper.onUpgrade(db, 0, 1);
	}
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		db = dbhelper.getWritableDatabase();
		return null;
	}
	
	protected boolean isLogin(){
		boolean result = false;
		db = dbhelper.getReadableDatabase();
		cursor = db.rawQuery("SELECT * FROM patients;", null);
		Log.e("TEST", cursor.toString());
		if(cursor == null)
		//if( (cursor.getString(0)).equals(null) )
			result = false;
		else
			result = true;
		
		//dbhelper.close();
		return result;
	}
	protected boolean isQR(){
		boolean result = false;
		db = dbhelper.getReadableDatabase();
		dbhelper.onCreate(db);
		cursor = db.rawQuery("SELECT * FROM hospitalinfo;", null);
		cursor.moveToFirst();
		Log.e("TEST", Integer.toString(cursor.getCount()));
		
		if(cursor.getString(0).equals(null))
			//if( (cursor.getString(0)).equals(null) )
			result = false;
		else
			result = true;
		
		
		//dbhelper.close();
		return result;
	}
	
	protected void hospital_insert(String name, String address){
		boolean result = false;
		db = dbhelper.getWritableDatabase();
		//dbhelper.onUpgrade(db, 0, 1);
		db.execSQL("INSERT INTO hospitalinfo VALUES ('"+ name+"','"+address+"');");				
		//dbhelper.close();
		Log.e("dbhelper","Insert success");
	}
   
}

