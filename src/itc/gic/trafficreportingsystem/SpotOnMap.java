package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SpotOnMap extends SherlockMapActivity implements  LocationListener{
	
	private MapView mapView;
	private MapController myMapController;
	LocationManager locationManager;
	ActionBar actionBar;
	MapManagement mapManager;

    ArrayList<Spot> spotList = new ArrayList<Spot>();
	
    public static ProgressBar pb;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
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
        mapManager.addCurrentLocation();
        // Progress dialog
        pb = (ProgressBar) findViewById(R.id.progressbar);
        // Get list of spots
        spotList = (ArrayList<Spot>) getIntent().getSerializableExtra("spot_detail_list");
        
        if (spotList!=null && spotList.size()!=0) {
            mapManager.addMarkerToMap(spotList);
            //	mapManager.addCurrentLocation();
		}
        if (spotList==null) {
            mapManager.addMarkerToMap(MainActivity.spotList);
            mapManager.addCurrentLocation();
		}
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
           
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		// when our activity resumes, we want to register for location updates
		//mMyLocationOverlay.enableMyLocation();
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// when our activity pauses, we want to remove listening for location updates
		//mMyLocationOverlay.disableMyLocation();
		
		locationManager.removeUpdates(this);
	}
   
	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_map,menu);
		
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
			if(location!=null){

				GetMapSpot thread = new GetMapSpot(MainActivity.adapter, MainActivity.spotList);
				thread.execute(location.getLatitude(), location.getLongitude());
				Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
			
				
			}else{
	        	Log.e(" ===>  "," Unknown Location ");
	        }
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

		private SpotAdapter ad;
		private ArrayList array_spot = new ArrayList<Spot>();
		private JSONObject jsonObject=null, jsonData=null;
		private JSONArray jsonArray=null; 
		
		public GetMapSpot(SpotAdapter ad, ArrayList<Spot> arrayl) {
			// TODO Auto-generated constructor stub
			this.ad = ad;
			this.array_spot = arrayl;
		}
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
			return MyTool.readTextFromUrl_(Server.LIST_SPOT+location[0]+"/"+location[1]+"/5");  //show only 5 spots near 
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			MainActivity.spotList.clear();
			
			Log.d("position", "inonPostExe"+result);
			try {
				
				jsonArray = new JSONArray(result);
				jsonObject = jsonArray.getJSONObject(0);
							
				jsonArray = jsonObject.getJSONArray("data"); 

				for(int i = 0; i < jsonArray.length(); i++){
					JSONObject j = jsonArray.getJSONObject(i);	
					Log.i("json object = ", j.getInt("number_report")+"");
		     		array_spot.add(new Spot(
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
	   	     	
		     	ad.setItems(array_spot);
				ad.notifyDataSetChanged();
				
				
				// Refresh map
				mapManager.resetMap();
				mapManager.addMarkerToMap(MainActivity.spotList);
				mapManager.addCurrentLocation();
				pb.setVisibility(View.INVISIBLE);
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}


