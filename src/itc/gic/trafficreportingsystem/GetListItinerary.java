package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Spot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

class GetListItinerary extends AsyncTask<String, Void, String>{

	private JSONObject jsonObject=null, jsonData=null;
	private JSONArray jsonArray=null; 
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(ItineraryDetail.pb!=null){
			ItineraryDetail.pb.setVisibility(View.VISIBLE);
		}else {
			System.err.println("===========pb null============");
		}
	}
	
	@Override
	protected String doInBackground(String... idSpot) {
		// TODO Auto-generated method stub
		Log.e("====> URL: "," "+idSpot[0]);
		Location location = MyApplication.getLocation();	
		String url = Server.LIST_SPOT_ITINERARY+idSpot[0];
		
		if(location!=null){
			url+="/"+location.getLatitude()+"/"+location.getLongitude();
		}
		
		return MyTool.readTextFromUrl_(url);  //show only 5 spots near 
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.d("position", "inonPostExe"+result);
		if(ItineraryDetail.pb!=null)
			ItineraryDetail.pb.setVisibility(View.INVISIBLE);
		try {
			
			jsonArray = new JSONArray(result);
			jsonObject = jsonArray.getJSONObject(0);
						
			jsonArray = jsonObject.getJSONArray("data"); 

			//** To add
			ItineraryDetail.spotList.clear();
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject j = jsonArray.getJSONObject(i);	
				Log.i("json object = ", j.getInt("number_report")+"");
				if(!j.getString("distance").equals("unknown")){
					ItineraryDetail.spotList.add(new Spot(
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
				}else{
					ItineraryDetail.spotList.add(new Spot(
		     				  j.getInt("id"),
		            		  j.getString("image"),
		            		  j.getString("name"), 
		            		  j.getString("address"),
		            		  j.getDouble("degree"),
		            		  -1,
		            		  j.getDouble("lat"),
		            		  j.getDouble("lng"),
		            		  j.getInt("number_report")
		            		  ) 
					);
				}
				
   	     	}	
   	     	
			ItineraryDetail.adapter.setItems(ItineraryDetail.spotList);
			ItineraryDetail.adapter.notifyDataSetChanged();
						
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
}