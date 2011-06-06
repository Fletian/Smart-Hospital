package android.graduate.SmartHospital;

import java.util.*;
import java.util.zip.*;


import com.markupartist.android.widget.*;
import com.markupartist.android.widget.PullToRefreshListView.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class Notice extends ListActivity {
    /** Called when the activity is first created. */
	private ArrayList<MyItem> mListItems;
	private ArrayList<MyItem> adaptItems;
	PullToRefreshListView pullView;
	int paging = 10;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        
        mListItems = new ArrayList<MyItem>();
        adaptItems = new ArrayList<MyItem>();
        pullView = (PullToRefreshListView) getListView();
        pullView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				 new GetDataTask().execute();
				 mListItems.add(0, new MyItem("Fuck", "WTF", "OMG"));
			}
		});

        
        MyItem mi;
        mi = new MyItem("테스트용입니다 ㅎㅎㅎㅎㅎ","이정균 바보","1분전");
        for(int i=0; i<20; i++)
        	mListItems.add(mi);
        for(int i=0; i<paging; i++)
        	adaptItems.add(mListItems.get(i));
        
        MyListAdapter adapter = new MyListAdapter(this,
                R.layout.customnotice, mListItems);

        setListAdapter(adapter);
    }
    class MyListAdapter extends BaseAdapter{
    	Context context;
    	LayoutInflater Inflater;
    	ArrayList<MyItem> arNotice;
    	int layout;
    	
    	public MyListAdapter(Context acontext, int alayout, ArrayList<MyItem> aarNotice){
    		context = acontext;
    		Inflater = (LayoutInflater)acontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		arNotice = aarNotice;
    		layout = alayout;
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arNotice.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arNotice.get(position).texts;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final int pos = position;
			if(convertView == null){
				convertView = Inflater.inflate(layout, parent, false);
			}
			TextView texts = (TextView)convertView.findViewById(R.id.notice_text);
			TextView writer = (TextView)convertView.findViewById(R.id.notice_writer);
			TextView agos = (TextView)convertView.findViewById(R.id.notice_ago);
			texts.setText(arNotice.get(position).texts);
			writer.setText(arNotice.get(position).writers);
			agos.setText(arNotice.get(position).agos);
			return convertView;
		}
    	
    }
    

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.add(0, new MyItem("Fuck", "WTF", "OMG"));

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}


class MyItem{
	String texts;
	String writers;
	String agos;
	MyItem(String atexts, String awriters, String aagos){
		texts = atexts;
		writers = awriters;
		agos = aagos;
	}
}