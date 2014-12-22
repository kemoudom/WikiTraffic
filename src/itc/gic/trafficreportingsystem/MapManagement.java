package itc.gic.trafficreportingsystem;

import itc.edu.tools.CustomItemizedOverlay;
import itc.edu.tools.CustomOverlayItem;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapManagement {
	
		List<Overlay> mapOverlays;
		public ArrayList<CustomItemizedOverlay>  overlayDegreeNormal, overlayDegreeMild, overlayDegreeSerious, overlayDegreeBlocking;  
		public CustomItemizedOverlay myLocationOverlay;
		Context context;
		MapView mapView;
		
	    public MapManagement(MapView mapView, Context context){
			
			this.context = context;
			this.mapView = mapView;
			mapOverlays = this.mapView.getOverlays();
			
			overlayDegreeBlocking = new ArrayList<CustomItemizedOverlay>();
			overlayDegreeMild = new ArrayList<CustomItemizedOverlay>();
			overlayDegreeNormal = new ArrayList<CustomItemizedOverlay>();
			overlayDegreeSerious = new ArrayList<CustomItemizedOverlay>();
			
			
			// Instatiate myLoactionOverlay
			myLocationOverlay = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.myposition), mapView);
			addCurrentLocation();
			
			 
	    }
	    
	   
	    
	   
	    
	    public void addCurrentLocation(){
	    	Location location = MyApplication.getLocation();
			if(location==null){
				Toast.makeText(context, "Location unknown", Toast.LENGTH_SHORT).show();
				mapOverlays.add(myLocationOverlay);
			}else{
				Toast.makeText(context, "HAVE CUR", Toast.LENGTH_LONG);
				GeoPoint point = new GeoPoint((int)(location.getLatitude()*1000000),(int)((location.getLongitude()*1000000)));
				//String myLocation = context.getString(R.string.my_location);
				CustomOverlayItem overlayitem = new CustomOverlayItem(context, point, "My Location", "", "", null);
			    myLocationOverlay.addOverlay(overlayitem);
			    
			    //myLocationOverlay.onTap(0);
			    
			    mapOverlays.add(myLocationOverlay);
			    mapView.getController().animateTo(point);
			}
	    }
	    
	    
	    public void changeCurrentLocation(Location location){
	    	
	    	myLocationOverlay.clear();
	    	GeoPoint point = new GeoPoint((int)(location.getLatitude()*1000000),(int)((location.getLongitude()*1000000)));
			//String myLocation = context.getString(R.string.my_location);
			CustomOverlayItem overlayitem = new CustomOverlayItem(context, point, "My Location", "", "",  null);
		    myLocationOverlay.addOverlay(overlayitem);
		    
		    Log.e("Change location:==> ","This is the new location.");
		    myLocationOverlay.hideBalloon();
		    mapView.invalidate();
		    
		    //myLocationOverlay.onTap(0);
	    }
	    
	   
	    
	    
	   
	    
	    public void addMarkerToMap(ArrayList<Spot> spots){
	    	if(spots.size()>0){
	    		GeoPoint point = new GeoPoint((int)(spots.get(0).getLat()*1000000),(int)((spots.get(0).getLng()*1000000)));
	    		mapView.getController().animateTo(point);
	    		
		    	for(int i=0; i< spots.size(); i++){
		    		point = new GeoPoint((int)(spots.get(i).getLat()*1000000),(int)((spots.get(i).getLng()*1000000)));
		    		
		    		CustomOverlayItem overlayitem = new CustomOverlayItem(context, point, spots.get(i).getName(), spots.get(i).getAddress(), spots.get(i).getImg(), spots.get(i));
				    
		    		CustomItemizedOverlay tmp;
		    		
		    		if(Math.round(spots.get(i).getDegree())==1){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.yellow2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeMild.add(tmp);
				    
		    		}else if(Math.round(spots.get(i).getDegree())==2){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.orange2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeSerious.add(tmp);
				    
		    		}else if(Math.round(spots.get(i).getDegree())==3){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.red2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeBlocking.add(tmp);
		    			
				    }else{
				    	
				    	tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.blue2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeNormal.add(tmp);
				    }
		    		
		    	}
	    	
		    	if(overlayDegreeNormal.size()>0){
		    		
		    		for(int i=0; i<overlayDegreeNormal.size();i++){
		    			mapOverlays.add(overlayDegreeNormal.get(i));
		    		}
		    			
		    	
		    	}
		    	if(overlayDegreeMild.size()>0){
		    		for(int i=0; i<overlayDegreeMild.size();i++){
		    			mapOverlays.add(overlayDegreeMild.get(i));
		    		}
		    	}
		    	if(overlayDegreeSerious.size()>0){
		    		for(int i=0; i<overlayDegreeSerious.size();i++){
		    			mapOverlays.add(overlayDegreeSerious.get(i));
		    		}
		    	}
		    	if(overlayDegreeBlocking.size()>0){
		    		for(int i=0; i<overlayDegreeBlocking.size();i++){
		    			mapOverlays.add(overlayDegreeBlocking.get(i));
		    		};
		    	}
	    	
	    	
	    	}
	    }
	    
	    public void addItinerarySpotsToMap(ArrayList<Spot> spots){
	    	if(spots.size()>0){
	    		GeoPoint point = new GeoPoint((int)(spots.get(0).getLat()*1000000),(int)((spots.get(0).getLng()*1000000)));
	    		mapView.getController().animateTo(point);
	    		
		    	for(int i=0; i< spots.size(); i++){
		    		point = new GeoPoint((int)(spots.get(i).getLat()*1000000),(int)((spots.get(i).getLng()*1000000)));
		    		
		    		CustomOverlayItem overlayitem = new CustomOverlayItem(context, point, spots.get(i).getName(), spots.get(i).getAddress(), spots.get(i).getImg(), spots.get(i));
				    
		    		CustomItemizedOverlay tmp;
		    		
		    		if(Math.round(spots.get(i).getDegree())==1){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.yellow2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeMild.add(tmp);
				    
		    		}else if(Math.round(spots.get(i).getDegree())==2){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.orange2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeSerious.add(tmp);
				    
		    		}else if(Math.round(spots.get(i).getDegree())==3){
		    			
		    			tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.red2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeBlocking.add(tmp);
		    			
				    }else{
				    	
				    	tmp = new CustomItemizedOverlay(context.getResources().getDrawable(R.drawable.blue2), mapView);
		    			tmp.addOverlay(overlayitem);
		    			
		    			overlayDegreeNormal.add(tmp);
				    }
		    		
		    	}
	    	
		    	if(overlayDegreeNormal.size()>0){
		    		
		    		for(int i=0; i<overlayDegreeNormal.size();i++){
		    			mapOverlays.add(overlayDegreeNormal.get(i));
		    		}
		    			
		    	
		    	}
		    	if(overlayDegreeMild.size()>0){
		    		for(int i=0; i<overlayDegreeMild.size();i++){
		    			mapOverlays.add(overlayDegreeMild.get(i));
		    		}
		    	}
		    	if(overlayDegreeSerious.size()>0){
		    		for(int i=0; i<overlayDegreeSerious.size();i++){
		    			mapOverlays.add(overlayDegreeSerious.get(i));
		    		}
		    	}
		    	if(overlayDegreeBlocking.size()>0){
		    		for(int i=0; i<overlayDegreeBlocking.size();i++){
		    			mapOverlays.add(overlayDegreeBlocking.get(i));
		    		};
		    	}
	    	
	    	
	    	}
	    }
	    
	    
/*	    public void addOnePlaceToMap(Place place){
	    	GeoPoint point = new GeoPoint((int)(place.lat*1000000),(int)((place.lng*1000000)));
    		mapView.getController().animateTo(point);
    		CustomOverlayItem overlayitem = new CustomOverlayItem(context, point, place.name, place.address, place.icon, place.id, place);
    		overlayitem.fromDetailPlace = true;
    		
    		CustomItemizedOverlay tmp;
    		
    		if(place.displayDegree==1){
    		d	tmp = new CustomItemizedOverlay<OverlayItem>(getDra(R.drawable.yellow_dot), mapView);
    		}else if(place.displayDegree==2){
    			tmp = new CustomItemizedOverlay<OverlayItem>(getDra(R.drawable.pink_dot), mapView);
    		}else if(place.displayDegree==3){
    			tmp = new CustomItemizedOverlay<OverlayItem>(getDra(R.drawable.red_dot), mapView);
    		
    		}else{
    			tmp = new CustomItemizedOverlay<OverlayItem>(getDra(R.drawable.blue_dot), mapView);
    		}
    		
			tmp.addOverlay(overlayitem);
			mapOverlays.add(tmp);
	    }
*/	    
	    
	    
	    
	    public void resetMap(){
	    	// Clear from itemizedOverlay
	    	for(int i=0;i<overlayDegreeBlocking.size();i++){
	    		overlayDegreeBlocking.get(i).hideAllBalloons();
	    		mapOverlays.remove(overlayDegreeBlocking.get(i));
	    	}
	    	overlayDegreeBlocking.clear();
	    	
	    	
	    	for(int i=0;i<overlayDegreeMild.size();i++){
	    		overlayDegreeMild.get(i).hideAllBalloons();
	    		mapOverlays.remove(overlayDegreeMild.get(i));
	    	}
	    	overlayDegreeMild.clear();
	    	
	    	for(int i=0;i<overlayDegreeNormal.size();i++){
	    		overlayDegreeNormal.get(i).hideAllBalloons();
	    		mapOverlays.remove(overlayDegreeNormal.get(i));
	    	}
	    	overlayDegreeNormal.clear();
	    	
	    	for(int i=0;i<overlayDegreeSerious.size();i++){
	    		overlayDegreeSerious.get(i).hideAllBalloons();
	    		mapOverlays.remove(overlayDegreeSerious.get(i));
	    	}
	    	overlayDegreeSerious.clear();
	    	
	    	
	    	mapView.invalidate();
	    }
		
	   
		
		
		
		
		
}
