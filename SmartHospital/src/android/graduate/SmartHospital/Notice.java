package android.graduate.SmartHospital;

import java.util.*;

import android.app.Activity;
import android.os.*;


public class Notice extends Activity {
    /** Called when the activity is first created. */
	ArrayList<MyItem> arItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        
        arItem = new ArrayList<MyItem>();
        
       
    }
}



class MyItem{
	MyItem(String texts, String writers, String agos){
		
	}
}