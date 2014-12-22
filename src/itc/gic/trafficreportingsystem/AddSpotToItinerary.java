package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;
import itc.gic.trafficreportingsystem.database.DBFunction;

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
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class AddSpotToItinerary extends SherlockActivity{
	
	ActionBar actionBar;
    ListView list;
    public static ItinerarySpotAdapter adapter;
    public static ArrayList<Spot> spotList, allGotList;
    public static GetListSpotAddToItinerary list_spot;
    public static ProgressBar pb;
    // For resetting
    public boolean isReset = false;
    public int noMore = 0;
    final private String ALL = "all", NORMAL = "normal", SLIGHT = "slight", MEDIUM = "medium", SERIOUS = "serious";
    public static int start=1, num_row=5;
    Location location;
    
    public static ArrayList<Integer> spotToAdd;
    
    int idItinerary;
    
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
        
        idItinerary = getIntent().getExtras().getInt("idItinerary");
        
        spotToAdd = new ArrayList<Integer>();
        
        final Location location = MyApplication.getLocation();
        spotList = new ArrayList<Spot>();
        allGotList = spotList;
        // Progress dialog
        pb = (ProgressBar) findViewById(R.id.progressbar);
        
        list=(ListView)findViewById(R.id.list);
        
        // Getting adapter by passing xml data ArrayList
        adapter=new ItinerarySpotAdapter(this, spotList);
        list_spot = new GetListSpotAddToItinerary(adapter, spotList);
        
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
     					GetListSpotAddToItinerary load_more = new GetListSpotAddToItinerary(adapter, spotList);
						if(location!=null){
	     					load_more.execute(location.getLatitude(), location.getLongitude());
							allGotList = spotList;
						}else{
							load_more.execute(0.0, 0.0);
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
        	list_spot.execute(0.0, 0.0);
        	allGotList = spotList;
        	
        	Log.e(" ===>  "," Unknown Location ");
        }
       
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long id) {
				// TODO Auto-generated method stub
				
				
				
				for(int i:spotToAdd){
					
					if(spotList.get(index).getId()==i){
						RelativeLayout layout = (RelativeLayout) arg1;
						layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
						spotToAdd.remove(new Integer(spotList.get(index).getId()));
						return;
					}
				}
				
				spotToAdd.add(spotList.get(index).getId());
				RelativeLayout layout = (RelativeLayout) arg1;
				layout.setBackgroundColor(Color.WHITE);
				
				
				//startActivity(intent); 
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
		inflater.inflate(R.menu.menu_addspot,menu);
		
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
		
		
		case R.id.done:
			
			for(int i:spotToAdd){
				
				DBFunction.addSpotToItinerary(MyApplication.dbHnalder, idItinerary, i);
			}
			finish();
			
			return true;
			
		
		/*case R.id.refresh:
			Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
			Location location = MyApplication.getLocation();
			if(location!=null){
				spotList.clear();
				GetListSpotAddToItinerary thread = new GetListSpotAddToItinerary(adapter, spotList);
				thread.execute(location.getLatitude(), location.getLongitude());
				Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
				allGotList = spotList;				
			}else{
	        	Log.e(" ===>  "," Unknown Location ");
	        }
			return true;
		*/
		}				
		return super.onOptionsItemSelected(item);
	}
}

