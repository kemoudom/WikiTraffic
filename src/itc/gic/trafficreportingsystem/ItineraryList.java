package itc.gic.trafficreportingsystem;

import itc.gic.model.Itinerary;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ItineraryList extends SherlockActivity{
	
	ActionBar actionBar;
    ListView list;
    public static ItineraryAdapter adapter;
    public static ArrayList<Itinerary> itineraryList;
    public static ProgressBar pb;
    TextView no_location_confirm;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);           
            
        itineraryList = new ArrayList<Itinerary>();
        
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        list=(ListView)findViewById(R.id.list);
        no_location_confirm = (TextView)findViewById(R.id.no_location_confirm);
        
        adapter=new ItineraryAdapter(this, itineraryList);
        list.setAdapter(adapter);
        
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ItineraryList.this, ItineraryDetail.class);
				Bundle b = new Bundle();              
				
				Log.e("==> ID: ",""+itineraryList.get(index).getId());
				b.putInt("id", itineraryList.get(index).getId());
		        b.putString("name",itineraryList.get(index).name);
		        intent.putExtras(b);
		        //intent.putExtra("Spot", spotList.get(index));
				startActivity(intent);      
			}
		});
	}
	
	
	public void onResume(){
	  	super.onResume();
	  	GetItinerary list_initerary = new GetItinerary(pb, adapter);
        
        list_initerary.execute();
         	
	}
	
	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_itinerary,menu);
		
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			//Toast.makeText(this, "Home ITEM", Toast.LENGTH_SHORT).show();
			finish();
			return true;
			
		case R.id.add:
			Intent i = new Intent(this, AddItineraryDialog.class);
			startActivity(i);
			return true;
		}		
		
		return super.onOptionsItemSelected(item);
	}
    
	
}

