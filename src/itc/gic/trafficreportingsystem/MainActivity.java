package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity{
	
	ActionBar actionBar;
    ListView list;
    public static SpotAdapter adapter;
    public static ArrayList<Spot> spotList, allList, filter_list;
    public static GetListSpot list_spot;
    public static ProgressBar pb;
    public static boolean curSpotListChange = false;
    final private String ALL = "all", NO_TRAFFIC_JAM= "no traffic jam", TRAFFIC_JAM = "traffic jam";
    Location location;
    LinearLayout no_location;
    TextView no_location_confirm;
    Button btn_no_location;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);           
            
        spotList = new ArrayList<Spot>();
        allList = new ArrayList<Spot>();
        filter_list = new ArrayList<Spot>();
        /*Spot sp1 = new Spot("http://blog.lib.umn.edu/staa0027/architecture/images/Intersection-Phenomena.jpg", "sp1", "toul kok", 0, 1000,104,1100,10);
        Spot sp2 = new Spot("http://www.cefc.net/wp-content/uploads/2012/10/james-intersection.jpg", "sp2", "Chbar ompov", 1, 1000,104,1160,5);
        Spot sp3 = new Spot("http://www.urbanphoto.net/blog/wp-content/uploads/2007/01/intersection.jpg", "sp3", "Chak engrer", 2, 1000,104,1200,0);
        Spot sp4 = new Spot("http://www.skorks.com/wp-content/uploads/2010/03/intersection.jpg", "sp4", "Steng Mean Chey", 3, 1000,104,1300,10);
        
        spotList.add(sp1);
        spotList.add(sp2);
        spotList.add(sp3);
        spotList.add(sp4);
        */
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        list=(ListView)findViewById(R.id.list);
        no_location = (LinearLayout)findViewById(R.id.no_location);
        no_location_confirm = (TextView)findViewById(R.id.no_location_confirm);
        btn_no_location = (Button)findViewById(R.id.btn_refresh_location);
        
        // Getting adapter by passing xml data ArrayList
        adapter=new SpotAdapter(this, spotList);
        list.setAdapter(adapter);
        
        list_spot = new GetListSpot(pb, adapter, spotList);
        location = MyApplication.getLocation();
        if(location!=null){
        	list_spot.execute(location.getLatitude(), location.getLongitude());
        	allList = spotList;  // back up all list
        	Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
        }else{
        	Log.e(" ===>  "," Unknown Location ");
        	no_location.setVisibility(View.VISIBLE);
        }
        
        btn_no_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location loc = MyApplication.currentLocation;
	        	if (loc!=null) {
	        		list_spot = new GetListSpot(pb, adapter, spotList);
		        	list_spot.execute(loc.getLatitude(), loc.getLongitude());
		        	allList = spotList;  // back up all list
				}
			}
		});
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SpotDetailReport.class);
				Bundle b = new Bundle();              
				b.putInt("indexSpot", spotList.get(index).getId());
		        //b.putString("userdata",jsonArray.toString());
		        intent.putExtras(b);
		        intent.putExtra("Spot", spotList.get(index));
				startActivity(intent);      
			}
		});
	}
	
	
	public void onResume(){
		super.onResume();
		adapter.notifyDataSetChanged();
		
	}
	
	// Functions for managing menu    
    public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_search,menu);
		
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			//Toast.makeText(this, "Home ITEM", Toast.LENGTH_SHORT).show();
			finish();
			return true;		
			
		case R.id.refresh:
			System.err.println("========REFRESH===========");
			if(location!=null){				
				list_spot = new GetListSpot(pb, adapter, spotList);
	        	list_spot.execute(location.getLatitude(), location.getLongitude());
	        	allList = spotList;  // back up all list
	        	Log.e("Location ==> ",location.getLatitude()+"   "+location.getLongitude());
	        }else{
	        	Location loc = MyApplication.getLocation();
	        	if (loc!=null) {
	        		list_spot = new GetListSpot(pb, adapter, spotList);
		        	list_spot.execute(loc.getLatitude(), loc.getLongitude());
		        	allList = spotList;  // back up all list
				}
	        }
			return true;
			
		case R.id.map:
			Toast.makeText(this, "List View", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, SpotOnMap.class);
			
			Bundle bundle = new Bundle();
	        bundle.putSerializable("spot_list", spotList);
	        i.putExtras(bundle);
	        
			startActivity(i);
			//finish();
			return true;
		
		case R.id.filter:
			return true;		
		
		case R.id.all:
			spotList = allList;
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}
			return true;
			
		case R.id.no_traffic_jam:
			spotList = MyTool.filter_spot(allList, NO_TRAFFIC_JAM);
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status no traffic jam !");
			}
			return true;
		case R.id.traffic_jam:
			spotList = MyTool.filter_spot(allList, TRAFFIC_JAM); 
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status traffic jam !");
			}
			return true;
		/*case R.id.medium:
			spotList = MyTool.filter_spot(allList, MEDIUM);
			//spotList.clear();
			Log.e("filter spot : ", spotList.get(0).getName()+"");
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status medium !");
			}
			return true;		
		case R.id.serious:
			spotList = MyTool.filter_spot(allList, SERIOUS);
			//spotList.clear();
			if (spotList.size()>0) {
				adapter.setItems(spotList);
				adapter.notifyDataSetChanged();
			}else {
				listSpotError("No spots with traffic status serious !");
			}
			return true;*/
		}		
		
		return super.onOptionsItemSelected(item);
	}
    
	public void listSpotError(String msg){
		new AlertDialog.Builder(this)
	    .setTitle("Spot Error")
	    .setMessage(msg)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
/*	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
*/	     .show();
	}
}

