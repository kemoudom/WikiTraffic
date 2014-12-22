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

public class GetListSpotAddToItinerary extends AsyncTask<Double, Void, String>{

	private ItinerarySpotAdapter ad;
	private ArrayList array_spot = new ArrayList<Spot>();
	private JSONObject jsonObject=null, jsonData=null;
	private JSONArray jsonArray=null, jsonMore=null; 
	public static boolean loadDone = false; 
	
	public GetListSpotAddToItinerary(ItinerarySpotAdapter ad, ArrayList<Spot> arrayl) {
		// TODO Auto-generated constructor stub
		this.ad = ad;
		this.array_spot = arrayl;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		AddSpotToItinerary.pb.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected String doInBackground(Double... location) {
		// TODO Auto-generated method stub
		
		/*Log.i("1",location[0]+"");
		Log.i("2",location[1]+"");
		Log.i("3",location[2]+"");
		Log.i("4",location[3]+"");*/
		
		//Log.d("position", "doInBackground "+Server.LIST_ALL_SPOT+location[0]+"/"+location[1]+"/"+location[2]+"/"+location[3]);
		Log.d("start index:", AddSpotToItinerary.start+"");
		return MyTool.readTextFromUrl_(Server.LIST_ALL_SPOT+AddSpotToItinerary.start+"/"+AddSpotToItinerary.num_row+"/"+location[0]+"/"+location[1]);  //show only 5 spots near 
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.d("position", "inonPostExe"+result);
		try {
			
			jsonArray = new JSONArray(result);
			jsonMore = new JSONArray(result);
			jsonObject = jsonArray.getJSONObject(0);
						
			//jsonMore = jsonObject.getJSONArray("morespot");
			JSONArray jdata = jsonObject.getJSONArray("data"); 
			JSONObject morespot = jsonArray.getJSONObject(1);
			
			Log.i("string", morespot.getString("morespot"));
			if (morespot.getString("morespot").equals("0")) {
				AddSpotToItinerary.btnLoadMore.setVisibility(View.GONE);
			}
			for(int i = 0; i < jdata.length(); i++){
				JSONObject j = jdata.getJSONObject(i);	
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
			AddSpotToItinerary.pb.setVisibility(View.INVISIBLE);
			
			if (AddSpotToItinerary.start==1) {
				AddSpotToItinerary.start = AddSpotToItinerary.num_row+1;
			}
			else
			{ 
				AddSpotToItinerary.start = AddSpotToItinerary.start+AddSpotToItinerary.num_row;
			}
			
			Log.d("Value start after Async Get More:", AddSpotToItinerary.start+"");
		}catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
