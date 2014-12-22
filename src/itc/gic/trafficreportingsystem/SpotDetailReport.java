package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import server.Server;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class SpotDetailReport extends SherlockActivity {
	
	private int indexSpot=0;
	//private ArrayList<Spot> spotList = new ArrayList<Spot>();
	private Spot spot;
	private ImageView spot_img, degree_img;
	public TextView spot_name, spot_address, degree, text_number_report, number_report;
	private Button btn_report;
	ActionBar actionBar;
	public int degree_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("position running: ", "Spot detail ");
		setContentView(R.layout.spot_detail_report);
		actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);   
        
		Bundle b = getIntent().getExtras();
		indexSpot = b.getInt("indexSpot");
		Log.i("index spot = ", indexSpot+"");
		
		spot = (Spot) getIntent().getSerializableExtra("Spot");
		
		////
		spot_img = (ImageView)findViewById(R.id.spot_image);
		degree_img = (ImageView)findViewById(R.id.degree_image);
		spot_name = (TextView)findViewById(R.id.spot_name);
		spot_address = (TextView)findViewById(R.id.address);
		degree = (TextView)findViewById(R.id.degree);
		text_number_report = (TextView)findViewById(R.id.text_number_report);
		number_report = (TextView)findViewById(R.id.number_report);
		//map_image = (Button)findViewById(R.id.map_image);
		btn_report = (Button)findViewById(R.id.btn_report);
		///
		
		MyApplication.imageLoader.displayImage(spot.getImg(), spot_img, MyApplication.detailOptions);
		spot_name.setText(spot.getName());
		spot_address.setText(spot.getAddress());
		text_number_report.setText("Number Reported: ");	
		MyTool.getDegreeStatus(SpotDetailReport.this, spot.getDegree(), degree_img, degree);
		number_report.setText(spot.getNumberReport()+"");
		
		/*map_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(this, "List View", Toast.LENGTH_SHORT).show();
				ArrayList<Spot> spotList = new ArrayList<Spot>();
				spotList.add(new Spot(
						spot.getId(),
						spot.getImg(), 
						spot.getName(),
						spot.getAddress(), 
						spot.getDegree(),
						spot.getDistance(), 
						spot.getLat(), 
						spot.getLng(), 
						spot.getNumberReport()));
						
				Intent i = new Intent(SpotDetail.this, SpotOnMap.class);
				Bundle bundle = new Bundle();
		        bundle.putSerializable("spot_detail_list", spotList);
		        i.putExtras(bundle);
		        
				startActivity(i);
			}
		});*/
		//if (MyApplication.loggedIn) {
			btn_report.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// custom dialog
					
					final Dialog dialog = new Dialog(SpotDetailReport.this);
					dialog.setContentView(R.layout.reporting_from);
					dialog.setTitle("Reporting Form");
					
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.text_traffic_status);
					RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.traffic_status);
					final ProgressBar pb = (ProgressBar)dialog.findViewById(R.id.progressBar_report);
					
					rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							// TODO Auto-generated method stub
							RadioButton rb=(RadioButton)dialog.findViewById(checkedId);
							if ((rb.getText()).equals("Slight")) {
								degree_value = 1;
							}
							else if ((rb.getText()).equals("Medium")) {
								degree_value = 2;
							}
							else if ((rb.getText()).equals("Serious")) {
								degree_value = 3;
							}
						}
					});
					
					Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit_report);
					// if button is clicked, close the custom dialog
					btn_submit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {							
							SendData send_report = new SendData(SpotDetailReport.this, dialog, pb);
							//int spot_id, int user_id, int usr_accuracy, double usr_lat, double usr_lng, int degree, String comments
							/*Log.i("1", indexSpot+"");
							Log.i("2", MyApplication.userId+"");
							Log.i("3", MyApplication.currentLocation.getAccuracy()+"");
							Log.i("4", MyApplication.currentLocation.getLatitude()+"");
							Log.i("5", MyApplication.currentLocation.getLongitude()+"");
							Log.i("6", degree_value+"");*/
							
							send_report.execute(indexSpot+"", MyApplication.userId+"", MyApplication.getLocation().getAccuracy()+"", MyApplication.getLocation().getLatitude()+"", MyApplication.getLocation().getLongitude()+"", degree_value+"", "Testing");
						}
					});
					
					dialog.show();
				}
			});
		/*}else{
			btn_report.setVisibility(View.INVISIBLE);
		}*/
	}
	public boolean onCreateOptionsMenu(Menu	menu){
    	
		
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_spot_detail,menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){ 
		
			case android.R.id.home:
				finish();
				return true;
			case R.id.map:
				ArrayList<Spot> spotList = new ArrayList<Spot>();
				spotList.add(new Spot(
						spot.getId(),
						spot.getImg(), 
						spot.getName(),
						spot.getAddress(), 
						spot.getDegree(),
						spot.getDistance(), 
						spot.getLat(), 
						spot.getLng(), 
						spot.getNumberReport()));
						
				Intent i = new Intent(SpotDetailReport.this, SpotDetailOnMap.class);
				Bundle bundle = new Bundle();
		        bundle.putSerializable("spot_detail_list", spotList);
		        i.putExtras(bundle);
		        
				startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public class SendData extends AsyncTask<String, Void, String>{

		private ProgressBar pb;
		private Context context;
		private Dialog dia;
		
		public SendData(Context context, Dialog report_dia, ProgressBar pb) {
			// TODO Auto-generated constructor stub
			this.dia = report_dia;
			this.context = context;
			this.pb = pb;
		}
	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(pb!=null)
				pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(String... report) {
			// TODO Auto-generated method stub
			sendData(Integer.parseInt(report[0]), Integer.parseInt(report[1]), Double.parseDouble(report[2]),
					Double.parseDouble(report[3]), Double.parseDouble(report[4]), Integer.parseInt(report[5]), report[6]);
			return "test";
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("position", "inonPostExe"+result);
			if(pb!=null)
				pb.setVisibility(View.INVISIBLE);
			
			if (dia!=null) {
				dia.dismiss();
			}
			
			String s = number_report.getText().toString();
			int num = Integer.parseInt(s)+1;
			number_report.setText(num+"");
			//number_report.setText("Hello");
			
			for(int i=0;i<MainActivity.spotList.size();i++){
				if(MainActivity.spotList.get(i).getId()==spot.getId()){
					MainActivity.spotList.get(i).setNumberReport(MainActivity.spotList.get(i).getNumberReport()+1);
				}
			}
			
			Toast.makeText(context, "Reporting sucessfully submit", Toast.LENGTH_LONG);
		}
		
		private void sendData(int spot_id, int user_id, double usr_accuracy, double usr_lat, double usr_lng, int degree, String comments)
		{
		     // 1) Connect via HTTP. 2) Encode data. 3) Send data.
		    try
		    {
		    	//String args = spot_id+"/"+user_id+"/"+MyApplication.currentLocation.getAccuracy()+"/"+MyApplication.currentLocation.getLatitude()+"/"+MyApplication.currentLocation.getLongitude()+"/"+degree_value+"/"+"Comments Testing";
		    	String args = spot_id+"/"+user_id+"/"+usr_accuracy+"/"+usr_lat+"/"+usr_lng+"/"+degree+"/"+comments;
		    	HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new      
		        HttpPost(Server.REPORTING+args);
		        Log.i("===??",Server.REPORTING+args);
		        //httppost.setEntity(new UrlEncodedFormEntity(data));
		        HttpResponse response = httpclient.execute(httppost);
		        Log.i("postData", response.getStatusLine().toString());
		            //Could do something better with response.
		    }
		    catch(Exception e)
		    {
		        Log.e("log_tag", "Error:  "+e.toString());
		    }  
		}
	}
}