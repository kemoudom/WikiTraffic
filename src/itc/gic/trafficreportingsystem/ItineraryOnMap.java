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

public class ItineraryOnMap extends SherlockMapActivity implements  LocationListener{
	
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
            //mapManager.addCurrentLocation();
		}
        if (spotList==null) {
            mapManager.addMarkerToMap(ItineraryDetail.spotList);
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
		inflater.inflate(R.menu.menu_map_itinerary_list,menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			//Toast.makeText(this, "Home ITEM", Toast.LENGTH_SHORT).show();
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
	

}


