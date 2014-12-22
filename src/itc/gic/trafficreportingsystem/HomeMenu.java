package itc.gic.trafficreportingsystem;

import itc.gic.application.MyApplication;
import itc.gic.trafficreportingsystem.database.DBFunction;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.NavUtils;

public class HomeMenu extends SherlockActivity{
	
	  ActionBar actionBar;
	  GridView grid_main;
      Context context;
      ImageAdapter grid;
      
      @Override
      public void onCreate(Bundle savedInstanceState){
          super.onCreate(savedInstanceState);
          
          actionBar = getSupportActionBar();
          actionBar.setDisplayHomeAsUpEnabled(false);
          actionBar.setDisplayShowHomeEnabled(false);
          actionBar.setDisplayShowTitleEnabled(true);
         
          // Initializing the components
          context = this;
          setContentView(R.layout.menu_home);

          // Test if user already login --------------------------------------------------------------------------
         // DBFunction.verifyLoginUser();
          
         
  
			
          grid_main = (GridView)findViewById(R.id.GridView1);
          /*grid_main.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if(arg1.getAction() == MotionEvent.ACTION_MOVE){
        	            return true;
        	        }
        	        return false;
				}

        	});*/

          grid_main.setAdapter(new ImageAdapter(this));
          grid= new ImageAdapter(context);
          
          grid_main.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView parent, View v, int position, long id) {
                  

            	  if(position==0){
            		  Intent i = new Intent(HomeMenu.this, MainActivity.class);
            		  startActivity(i);

            	  }else if(position==1){
            		  Intent i = new Intent(HomeMenu.this, TrafficView.class);
            		  startActivity(i);
            	  }else if(position==2){
            		  Intent i = new Intent(HomeMenu.this, ItineraryList.class);
            		  startActivity(i);
            	  }
              }
          });
          
          
    	
          


      }
      
      
   
      
      public class ImageAdapter extends BaseAdapter{
          Context mContext;
          public static final int ACTIVITY_CREATE = 7;
          ImageView iv;
          TextView tv;
          
          Integer[] image = {
                        R.drawable.report, R.drawable.traffic,
                        R.drawable.place
                        /*R.drawable.khmer, R.drawable.us,
                        R.drawable.logout*/
                       
                };
             String[] Icon;
                
          public ImageAdapter(Context c){
              mContext = c;
              Icon = new String[] { "Report Spots", "Traffic view",
	          			"My Itinerary"
	                  };
          }
          
          @Override
          public int getCount() {
              // TODO Auto-generated method stub
              return 3;
          }
          public Object getItem(int position) {
              return position;
          }

          public long getItemId(int position) {
              return position;
          }
          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
              // TODO Auto-generated method stub
              View v;
            //  if(convertView==null){
                  LayoutInflater li = getLayoutInflater();                    
                  v = li.inflate(R.layout.item_grid, null);
                  
                  tv = (TextView)v.findViewById(R.id.grid_item_label);
                  
                  iv = (ImageView)v.findViewById(R.id.grid_item_image);
                  
                  tv.setText(Icon[position]);
                 
                  
                  iv.setImageResource(image[position]);
                  
          
                  return v;
          }
         
      	}
      
    @Override
  	public void onDestroy(){
    	super.onDestroy();
  	}
      
    public void onResume(){
    	super.onResume();
     	
    }
       
}
