package itc.edu.tools;

import itc.gic.application.MyApplication;
import itc.gic.model.Spot;
import itc.gic.trafficreportingsystem.AddSpotToItineraryOnMap;
import itc.gic.trafficreportingsystem.R;
import itc.gic.trafficreportingsystem.SpotDetail;
import itc.gic.trafficreportingsystem.TrafficView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.OverlayItem;


public class CustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<CustomOverlayItem> {

	private TextView name, address, distance, degreeText;
	private ImageView image, degreeImage;
	private ImageButton detail;
	private CustomOverlayItem coi;
	private Context context;
	private ImageButton add, remove;
	
 	
	public CustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		
		this.context = context;
		
	     
	}
	
	
	// Method used for setting the view
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.ballon_custom_layout, parent);
		
		// setup fields
		name = (TextView) v.findViewById(R.id.balloon_item_name);
		address = (TextView) v.findViewById(R.id.balloon_item_address);
		distance = (TextView) v.findViewById(R.id.balloon_item_distance);
		degreeText = (TextView) v.findViewById(R.id.degreeText);
		
		image = (ImageView) v.findViewById(R.id.balloon_item_image);
		degreeImage = (ImageView) v.findViewById(R.id.degreeIcon);
		detail = (ImageButton) v.findViewById(R.id.detailMap);
		add = (ImageButton) v.findViewById(R.id.add_button);
		remove = (ImageButton) v.findViewById(R.id.remove_button);
		
	}


	// Method used for setting up the data and setting actions for components
	@Override
	protected void setBalloonData(CustomOverlayItem item, ViewGroup parent) {
		
		// For using in onClick
		coi = item;
		final Spot spot = (Spot)item.getObject();
		
		if(MyApplication.addSpotToItineraryMap){
			add.setVisibility(View.VISIBLE);
			add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AddSpotToItineraryOnMap.spotToAdd.add(spot.getId());
					
					remove.setVisibility(View.VISIBLE);
					add.setVisibility(View.GONE);
					
				}
			});
			
			remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					AddSpotToItineraryOnMap.spotToAdd.remove(new Integer(spot.getId()));
					
					remove.setVisibility(View.GONE);
					add.setVisibility(View.VISIBLE);
					
				}
			});
		}
		
		name.setText(item.getTitle());
		if(spot!=null){
			if(!item.getSnippet().trim().equals("")){
				address.setText(item.getSnippet());
			}else{
				address.setVisibility(View.GONE);
			}
			
			if(spot.getDistance()>=0){
	        	distance.setText("Distance: "+new DecimalFormat("##.##").format(spot.getDistance())+" KM");
	        }else{
	        	distance.setText("Distance: Unknown");
	        }
			
			
			
			if(Math.round(spot.getDegree())==0){
	        	degreeImage.setImageResource(R.drawable.blue2);
	        	degreeText.setText("No Traffic Jam");
	        }else if(Math.round(spot.getDegree())==1){
	        	degreeImage.setImageResource(R.drawable.yellow2);
	        	degreeText.setText("Slight Traffic Jam");
	        }else if(Math.round(spot.getDegree())==2){
	        	degreeImage.setImageResource(R.drawable.orange2);
	        	degreeText.setText("Meduim Traffic Jam");
	        }else if(Math.round(spot.getDegree())==3){
	        	degreeImage.setImageResource(R.drawable.red2);
	        	degreeText.setText("Serious Traffic Jam");
	        }
			
			
			
			MyApplication.imageLoader.displayImage(spot.getImg(), image, MyApplication.detailOptions);
		
		
		}else{	// Current location
			address.setVisibility(View.GONE);
			detail.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
			remove.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			
		}
					
			
		detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent = new Intent(context, SpotDetail.class);
				Bundle b = new Bundle();              
				b.putInt("indexSpot", spot.getId());
		        //b.putString("userdata",jsonArray.toString());
		        intent.putExtras(b);
		        intent.putExtra("Spot", spot);
				context.startActivity(intent);
			}
		});
	}

	// If image is download from the server
	
	/*private class FetchImageTask extends AsyncTask<String, Integer, Bitmap> {
	    @Override
	    protected Bitmap doInBackground(String... arg0) {
	    	Bitmap b = null;
	    	try {
				 b = BitmapFactory.decodeStream((InputStream) new URL(arg0[0]).getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	        return b;
	    }	
	}*/
	
}
