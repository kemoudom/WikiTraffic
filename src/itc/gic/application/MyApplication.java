package itc.gic.application;

import itc.gic.trafficreportingsystem.R;
import itc.gic.trafficreportingsystem.database.ConnectDB;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyApplication extends Application implements LocationListener{
	
	public static boolean networkStatus;
	public static LocationManager locationManager = null ;
	public static Location currentLocation = null;
	public static LocationListener listener=null;
		
	// ImageLoader
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions detailOptions;
		
	
	// User's info
	public static boolean loggedIn = true;
	public static int userId = 1;
	public static String userName = null;
	public static boolean restart = false;
	
	// database handler
	public static ConnectDB dbHnalder;
	
	// For showing itinerary spots on map
	public static boolean addSpotToItineraryMap = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		

		dbHnalder = new ConnectDB(this);
		
		// GPS capture location
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		listener = ((MyApplication)getApplicationContext());
		// Update every different 100 meters
		setDistanceUpdateLocation(100);
		
		/////////////////////
		Location loc1 = new Location("test");
		loc1.setLatitude(11.569738);
		loc1.setLongitude(104.90011);
		
		Location loc2 = new Location("test");
		loc2.setLatitude(104.9);
		loc2.setLongitude(11.569736666666666);
		
		Log.e("Distance : ", loc1.distanceTo(loc2)+"");
		//////////////////////
		// Image loader
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		
		detailOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.no_image)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}	
	
		
	public static void setDistanceUpdateLocation(float distance){
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, distance, listener);    		
		if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, distance, listener);
		}
	}
	
	public static Location getLocation() {
	 
		Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
	    Location networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);

	    //if we have only one location available, the choice is easy
	    if (gpslocation != null && networkLocation != null) {
	        if (gpslocation.getTime() > networkLocation.getTime()) {
		        Log.d("================>", "Both are old, returning gps(newer)");
		        return gpslocation;
		    } else {
		        Log.d("================>", "Both are old, returning network(newer)");
		        return networkLocation;
		    }
	        
	    }else{
	    
	    	if (gpslocation == null) {
		        
		        if (networkLocation == null) {
			        return null;
			    }else{
			    	Log.d("==============>", "No GPS Location available. Take network");
			    	return networkLocation;
			    }
		        
		    }else{
		    	Log.d("==============>", "GPS Location available. Take GPS"); 
		    	return gpslocation;
		    }
	    }
	}
	
	public static Location getLocationByProvider(String provider) {
	    Location location = null;
	 
	    try {
	        if (locationManager.isProviderEnabled(provider)) {

	            location = locationManager.getLastKnownLocation(provider);
	            
	        }
	    } catch (IllegalArgumentException e) {
	        Log.d("", "Cannot acces Provider " + provider);
	    }
	    return location;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
