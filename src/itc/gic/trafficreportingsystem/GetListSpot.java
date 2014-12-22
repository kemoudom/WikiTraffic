package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.model.Spot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class GetListSpot extends AsyncTask<Double, Void, String>{

	private SpotAdapter ad;
	private ArrayList array_spot = new ArrayList<Spot>();
	private JSONObject jsonObject=null, jsonData=null;
	private JSONArray jsonArray=null; 
	public static boolean loadDone = false; 
	private ProgressBar pb;
	
	public GetListSpot(ProgressBar pb, SpotAdapter ad, ArrayList<Spot> arrayl) {
		// TODO Auto-generated constructor stub
		this.ad = ad;
		this.pb = pb;
		this.array_spot = arrayl;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(pb!=null){
			System.err.println("===========pb no null============");
			pb.setVisibility(View.VISIBLE);
		}else {
			System.err.println("===========pb null============");
		}
	}
	
	@Override
	protected String doInBackground(Double... location) {
		// TODO Auto-generated method stub
		Log.d("position", "doInBackground "+Server.LIST_NEAREST_SPOT+location[0]+"/"+location[1]);
		return MyTool.readTextFromUrl_(Server.LIST_NEAREST_SPOT+"/100/"+location[0]+"/"+location[1]);  //show only 5 spots near 
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.d("position", "inonPostExe"+result);
		if(pb!=null)
			pb.setVisibility(View.INVISIBLE);
		try {
			
			jsonArray = new JSONArray(result);
			jsonObject = jsonArray.getJSONObject(0);
						
			jsonArray = jsonObject.getJSONArray("data"); 

			//** To add
			array_spot.clear();
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject j = jsonArray.getJSONObject(i);	
				Log.i("json object = ", j.getInt("number_report")+"");
	     		array_spot.add(new Spot(
	     				  j.getInt("id"),
	            		  j.getString("image"),
	            		  j.getString("name"), 
	            		  j.getString("address"),
	            		  j.getDouble("degree"),
	            		  j.getDouble("distance"),
	            		  j.getDouble("lat"),
	            		  j.getDouble("lng"),
	            		  j.getInt("number_report")
	            		  ) 
				);
   	     	}	
   	     	
	     	ad.setItems(array_spot);
			ad.notifyDataSetChanged();
			
			// Indicating that loading is done 
			loadDone = true;
			
			Log.d("position", "onPost");
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
