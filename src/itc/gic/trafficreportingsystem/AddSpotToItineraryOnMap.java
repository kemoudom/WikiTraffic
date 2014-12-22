package itc.gic.trafficreportingsystem;

import itc.edu.tools.CustomBalloonOverlayView;
import itc.edu.tools.CustomItemizedOverlay;
import itc.edu.tools.CustomOverlayItem;
import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;
import itc.gic.trafficreportingsystem.database.DBFunction;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AddSpotToItineraryOnMap extends SherlockMapActivity implements  LocationListener{
	
	private MapView mapView;
	private MapController myMapController;
	LocationManager locationManager;
	ActionBar actionBar;
	MapManagement mapManager;
	int lat, lng;
	public static ProgressBar pb;
	public static ArrayList<Spot> spotList;
    public static SpotAdapter adapter;
    public int idItinerary;
    public static int start=1, num_row=5;
    public boolean refresh = false;
    public static ArrayList<Integer> spotToAdd; 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        // List of spots to add
        spotToAdd = new ArrayList<Integer>();
        
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        
        
        // Instantiate Components
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);
       
        myMapController = mapView.getController();
        myMapController.setZoom(15); //Fixed Zoom Level       
     
     
        mapManager = new MapManagement(mapView, this);
        
        // Get list of spots
        idItinerary = getIntent().getExtras().getInt("idItinerary");
        
        spotList = new ArrayList<Spot>();
        
        mapManager.addCurrentLocation();
     
        // Progress dialog
        pb = (ProgressBar) findViewById(R.id.progressbar);
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
        Location location = MyApplication.getLocation();
        GetMapSpot thread = new GetMapSpot();
		
        if(location!=null){

			thread.execute(location.getLatitude(), location.getLongitude());
			Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
			
			
		}else{
			thread.execute(0.0, 0.0);	
        	Log.e(" ===>  "," Unknown Location ");
        }
            
        
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		// when our activity resumes, we want to register for location updates
		//mMyLocationOverlay.enableMyLocation();
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		MyApplication.addSpotToItineraryMap = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// when our activity pauses, we want to remove listening for location updates
		//mMyLocationOverlay.disableMyLocation();
		
		locationManager.removeUpdates(this);
		
		MyApplication.addSpotToItineraryMap = false;
	
		//????
		start = 1;
		num_row = 5;
	}
   
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		MyApplication.addSpotToItineraryMap = false;
	}

	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_map_itinerary,menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			//Toast.makeText(this, "Home ITEM", Toast.LENGTH_SHORT).show();
			finish();
			return true;
		
			
		case R.id.refresh:
			Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
			Location location = MyApplication.getLocation();
			GetMapSpot thread = new GetMapSpot();
			refresh = true;
			if(location!=null){

				thread.execute(location.getLatitude(), location.getLongitude());
				Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
			
				
			}else{
				thread.execute(0.0, 0.0);
	        	Log.e(" ===>  "," Unknown Location ");
	        }
			return true;
			
		
		case R.id.load_more:
			Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
			Location location1 = MyApplication.getLocation();
			GetMapSpot thread1 = new GetMapSpot();
			if(location1!=null){

				thread1.execute(location1.getLatitude(), location1.getLongitude());
				Log.e("Location ==> ",location1.getLatitude()+"   "+location1.getLongitude());
			
				
			}else{
				thread1.execute(0.0, 0.0);
	        	Log.e(" ===>  "," Unknown Location ");
	        }
			return true;
			
		case R.id.done:
			
			for(int i:spotToAdd){
				
				DBFunction.addSpotToItinerary(MyApplication.dbHnalder, idItinerary, i);
			}
			finish();
			
			return true;	
		
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
			
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mapManager.changeCurrentLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}    
	
	class GetMapSpot extends AsyncTask<Double, Void, String>{

		private JSONObject jsonObject=null, jsonData=null;
		private JSONArray jsonArray=null; 
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(Double... location) {
			// TODO Auto-generated method stub
			Log.d("position", "doInBackground "+Server.LIST_SPOT+location[0]+"/"+location[1]);
			if(!refresh){
				return MyTool.readTextFromUrl_(Server.LIST_ALL_SPOT+start+"/"+num_row+"/"+location[0]+"/"+location[1]);
			}else{
				return MyTool.readTextFromUrl_(Server.LIST_ALL_SPOT+0+"/"+(start-1)+"/"+location[0]+"/"+location[1]);
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(refresh){
				spotList.clear();
			}
			
			Log.d("position", "inonPostExe"+result);
			try {
				
				jsonArray = new JSONArray(result);
				jsonObject = jsonArray.getJSONObject(0);
							
				jsonArray = jsonObject.getJSONArray("data"); 

				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject j = jsonArray.getJSONObject(i);	
					Log.i("json object = ", j.getInt("number_report")+"");
		     		spotList.add(new Spot(
		     				  j.getInt("id"),
		            		  j.getString("image"),
		            		  j.getString("name"), 
		            		  j.getString("address"),
		            		  j.getDouble("degree"),
		            		  j.getDouble("distance"),
		            		  j.getDouble("lat"),
		            		  j.getDouble("lng"),
		            		  j.getInt("number_report")
		            		  ) 
					);
	   	     	}	
	   	     	
				
				// Refresh map
				mapManager.resetMap();
				mapManager.addCurrentLocation();
				mapManager.addItinerarySpotsToMap(spotList);
				pb.setVisibility(View.INVISIBLE);
				
				if(!refresh){
					if (start==1) {
						start =num_row+1;
					}
					else
					{ 
						start = start+num_row;
					}
				}else{
					refresh = false;
				}
				
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	
}


