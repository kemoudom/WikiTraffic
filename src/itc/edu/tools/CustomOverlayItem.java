package itc.edu.tools;

import android.content.Context;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayItem extends OverlayItem {

	public Context context;
	public String imageURL;
	public int id;
	private Object obj;
	public boolean fromDetailPlace = false;
	
	public CustomOverlayItem(Context c, GeoPoint point, String title, String address,  String imageURL, Object obj) {
		super(point, title, address);
		this.imageURL = imageURL;
		context = c;
		//this.id = id;
		this.obj = obj;
	}
	
	public Object getObject(){	
		return obj;
	}
	
	public void setObject(Object obj){
		
		this.obj = obj;
	}

	
}
