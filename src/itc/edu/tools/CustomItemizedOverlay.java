/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package itc.edu.tools;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomItemizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<CustomOverlayItem> {

	private ArrayList<CustomOverlayItem> m_overlays = new ArrayList<CustomOverlayItem>();
	private Context c;
	public boolean added = false;
	private boolean   isPinch  =  false;

	//int defaultMarker;
	int index = 0;
	
	public CustomItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		populate();
		c = mapView.getContext();
		setBalloonBottomOffset(10);
		
//		this.defaultMarker = markerID;
	}

	public void addOverlay(CustomOverlayItem overlay) {
	    m_overlays.add(overlay);
	    setLastFocusedIndex(-1);
	    populate();
	}
	
	public void removeOverlay(OverlayItem overlay) {
        m_overlays.remove(overlay);
        populate();
    }


    public void clear() {
        m_overlays.clear();
        setLastFocusedIndex(-1);
        populate();
    }


	@Override
	protected CustomOverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, CustomOverlayItem item) {
		Toast.makeText(c, "onBalloonTap for overlay index " + index,
				Toast.LENGTH_LONG).show();
		
		hideAllBalloons();
		return true;
	}

	@Override
	protected BalloonOverlayView<CustomOverlayItem> createBalloonOverlayView() {
		
		// use our custom balloon view with our custom overlay item type:
		return new CustomBalloonOverlayView<CustomOverlayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}
	
	@Override
	public void draw(android.graphics.Canvas canvas,MapView mapView,boolean shadow) {

			super.draw(canvas, mapView, false);
			/*
			if(m_overlays.size()>0&&index<m_overlays.size()){
				// Convert geo coordinates to screen pixels
			    Point screenPoint = new Point();
			    mapView.getProjection().toPixels(m_overlays.get(index).getPoint(), screenPoint);
			    Paint paint = new Paint();
			    // Read the image
			    Bitmap markerImage = BitmapFactory.decodeResource(c.getResources(), defaultMarker);
			    paint.setStrokeWidth(1);
			    paint.setARGB(150, 000, 000, 000);
			    paint.setStyle(Paint.Style.STROKE);
			    // Draw it, centered around the given coordinates
			    canvas.drawBitmap(markerImage,
			        screenPoint.x - markerImage.getWidth() / 2,
			        screenPoint.y - markerImage.getHeight() / 2, null);
			    canvas.drawText("1", screenPoint.x- markerImage.getWidth() / 2,  screenPoint.y - markerImage.getHeight() / 2 , paint);
	
				index++;
			}*/
	}

	
	/*// Get tap location
	@Override
	public boolean onTap(GeoPoint p, MapView map){
	    if ( isPinch ){
	        return false;
	    }else{
	        if ( p!=null ){
	       
	        	Toast.makeText(c, "Tap on map: "+p.getLatitudeE6()/1E6+" "+p.getLongitudeE6()/1E6,
	    				Toast.LENGTH_LONG).show();
	        	
	            return true;            // We handled the tap
	        }else{
	            return false;           // Null GeoPoint
	        }
	    }
	}*/

	/*@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView)
	{
	    int fingers = e.getPointerCount();
	    if( e.getAction()==MotionEvent.ACTION_DOWN ){
	        isPinch=false;  // Touch DOWN, don't know if it's a pinch yet
	    }
	    if( e.getAction()==MotionEvent.ACTION_MOVE && fingers==2 ){
	        isPinch=true;   // Two fingers, def a pinch
	    }
	    return super.onTouchEvent(e,mapView);
	}*/



}
