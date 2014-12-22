package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;

import android.app.AlertDialog;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TrafficView extends SherlockActivity{
	
	ActionBar actionBar;
    ListView list;
    public static SpotAdapter adapter;
    public static ArrayList<Spot> spotList, allGotList;
    public static GetListSpotTrafficView list_spot;
    public static ProgressBar pb;
    // For resetting
    public boolean isReset = false;
    public int noMore = 0;
    final private String ALL = "all", NORMAL = "normal", SLIGHT = "slight", MEDIUM = "medium", SERIOUS = "serious";
    public static int start=1, num_row=5;
    Location location;
    
    View v;
    public static Button btnLoadMore;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traffic_view);
		
		actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);           
        
        final Location location = MyApplication.getLocation();
        spotList = new ArrayList<Spot>();
        allGotList = spotList;
        // Progress dialog
        pb = (ProgressBar) findViewById(R.id.progressbar);
        
        list=(ListView)findViewById(R.id.list);
        
        // Getting adapter by passing xml data ArrayList
        adapter=new SpotAdapter(this, spotList);
        list_spot = new GetListSpotTrafficView(adapter, spotList);
        
        // inflate footer view
     		v = getLayoutInflater().inflate(R.layout.load_more, null);
     		btnLoadMore = (Button) v.findViewById(R.id.bthLoadMore);
     		btnLoadMore.setText("Load More");
     		btnLoadMore.setOnClickListener(new OnClickListener() {
     			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				//loadMore();
     				if (list_spot!=null) {
     					GetListSpotTrafficView load_more = new GetListSpotTrafficView(adapter, spotList);
						if(location!=null){
	     					load_more.execute(location.getLatitude(), location.getLongitude());
							allGotList = spotList;
						}else{
				        	list_spot.execute(0.0,0.0);
				        	allGotList = spotList;

						}
     				}
     			}
     		});
     		btnLoadMore.setClickable(true);
     		
     		
     		//v.setVisibility(View.GONE);
     		list.addFooterView(v);

     		list.setAdapter(adapter);
       
        
        if(location!=null){
        	list_spot.execute(location.getLatitude(), location.getLongitude());
        	allGotList = spotList;
        	Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
        }else{
        	list_spot.execute(0.0,0.0);
        	allGotList = spotList;
        	
        	Log.e(" ===>  "," Unknown Location ");
        }
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TrafficView.this, SpotDetail.class);
				Bundle b = new Bundle();              
				b.putInt("indexSpot", spotList.get(index).getId());
		        //b.putString("userdata",jsonArray.toString());
		        intent.putExtras(b);
		        intent.putExtra("Spot", spotList.get(index));
				startActivity(intent); 
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		start = 1;
		num_row = 5;
	}
	
	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_search,menu);
		
		return true;
	}
    
    public void listSpotError(String msg){
		new AlertDialog.Builder(this)
	    .setTitle("Spot Error")
	    .setMessage(msg)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	/*	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	*/	     .show();
	}
    
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			//Toast.makeText(this, "Home ITEM", Toast.LENGTH_SHORT).show();
			finish();
			return true;
		
			
		case R.id.map:
			Toast.makeText(this, "List View", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, TrafficOnMap.class);
			
			Bundle bundle = new Bundle();
	        bundle.putSerializable("spot_list", spotList);
	        i.putExtras(bundle);
	        
			startActivity(i);
			//finish();
			return true;
		
		case R.id.refresh:
			Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
			Location location = MyApplication.getLocation();
			if(location!=null){
				spotList.clear();
				GetListSpotTrafficView thread = new GetListSpotTrafficView(adapter, spotList);
				thread.execute(location.getLatitude(), location.getLongitude());
				Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
				allGotList = spotList;				
			}else{
	        	Log.e(" ===>  "," Unknown Location ");
	        }
			
			return true;
			
		case R.id.all:
			spotList = allGotList;
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}
			return true;
			
		case R.id.normal:
			spotList = MyTool.filter_spot(allGotList, NORMAL);
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status normal !");
			}
			return true;
		case R.id.slight:
			spotList = MyTool.filter_spot(allGotList, SLIGHT); 
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status slight !");
			}
			return true;
		case R.id.medium:
			spotList = MyTool.filter_spot(allGotList, MEDIUM);
			//spotList.clear();
			//Log.e("filter spot : ", spotList.get(0).getName()+"");
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status medium !");
			}
			return true;		
		case R.id.serious:
			spotList = MyTool.filter_spot(allGotList, SERIOUS);
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status serious !");
			}
			return true;
		}				
		return super.onOptionsItemSelected(item);
	}
}

