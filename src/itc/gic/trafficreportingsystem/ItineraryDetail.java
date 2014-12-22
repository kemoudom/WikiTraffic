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
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ItineraryDetail extends SherlockActivity{
	
	ActionBar actionBar;
    ListView list;
    public static SpotItineraryAdapter adapter;
    public static ArrayList<Spot> spotList;
    public static ProgressBar pb;
    int idItinerary;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itinerary_detail);
        
		actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);           
            
        spotList = new ArrayList<Spot>();
        
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        list=(ListView)findViewById(R.id.list);
        final SwipeDetector swipeDetector = new SwipeDetector();
        list.setOnTouchListener(swipeDetector);
        // Getting adapter by passing xml data ArrayList
        adapter=new SpotItineraryAdapter(this, spotList);
        list.setAdapter(adapter);
        
        idItinerary = getIntent().getExtras().getInt("id");
        
        String ids = DBFunction.getIdSpots(MyApplication.dbHnalder, idItinerary);
        
        if(!ids.equals("")){
	        GetListItinerary list_spot = new GetListItinerary();
	        list_spot.execute(new String[]{ids});
        }
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
				// TODO Auto-generated method stub
				 if (swipeDetector.swipeDetected()){
	                    // do the onSwipe action 
					 
					 final int i = index;
					 
					 Log.e("***==> SWIPE","SWIPT");
					 
					 final Button deleteButton = (Button) view.findViewById(R.id.delete_spot);    
					 deleteButton.setVisibility(View.VISIBLE);
					 
					 deleteButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DBFunction.deleteSpotFromItinerary(MyApplication.dbHnalder, idItinerary, spotList.get(i).getId());
							spotList.remove(i);
							adapter.notifyDataSetChanged();
						}
					 });
					 
					 final Button cancelButton = (Button) view.findViewById(R.id.cancel_button);    
					 cancelButton.setVisibility(View.VISIBLE);
					 
					 cancelButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							cancelButton.setVisibility(View.GONE);
							deleteButton.setVisibility(View.GONE);
						}
					});
					 
					 
	            } else {
	                    // do the onItemClick action
	            	Intent intent = new Intent(ItineraryDetail.this, SpotDetail.class);
					Bundle b = new Bundle();              
					b.putInt("indexSpot", spotList.get(index).getId());
			        //b.putString("userdata",jsonArray.toString());
			        intent.putExtras(b);
			        intent.putExtra("Spot", spotList.get(index));
					startActivity(intent);
	            }
				
				      
			}
		});
	}
	
	public void onResume(){
		
		super.onResume();
		
		String ids = DBFunction.getIdSpots(MyApplication.dbHnalder, idItinerary);
        if(!ids.equals("")){
	        GetListItinerary list_spot = new GetListItinerary();
	        list_spot.execute(new String[]{ids});
        }
	}
	
	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.itinerary_detail,menu);
		
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			finish();
			return true;		
			
		case R.id.refresh:
			String ids = DBFunction.getIdSpots(MyApplication.dbHnalder, idItinerary);
	        if(!ids.equals("")){
		        GetListItinerary list_spot = new GetListItinerary();
		        list_spot.execute(new String[]{ids});
	        }
			return true;
			
		case R.id.map:
			Toast.makeText(this, "List View", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, ItineraryOnMap.class);
			
			Bundle bundle = new Bundle();
	        bundle.putSerializable("spot_list", spotList);
	        i.putExtras(bundle);
	        
			startActivity(i);
			return true;
		
		case R.id.add:
			
			final CharSequence[] items = {"Select from List", "Select from Map"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Make your selection");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {

			    	// Do something with the selection
			    	if(item==0){
						Intent i = new Intent(ItineraryDetail.this, AddSpotToItinerary.class);
						
						Bundle bundle = new Bundle();
						bundle.putInt("idItinerary", idItinerary);
						i.putExtras(bundle);
				        
						startActivity(i);

			    	}else{
			    		Intent i = new Intent(ItineraryDetail.this, AddSpotToItineraryOnMap.class);
						
						Bundle bundle = new Bundle();
						bundle.putInt("idItinerary", idItinerary);
						i.putExtras(bundle);
				        
						startActivity(i);

			    	}
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
			return true;		
					
		}		
		
		return super.onOptionsItemSelected(item);
	}
    
    
    
}

