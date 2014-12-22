package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Itinerary;
import itc.gic.model.Spot;
import itc.gic.trafficreportingsystem.database.ConnectDB;
import itc.gic.trafficreportingsystem.database.DBFunction;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.Server;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class GetItinerary extends AsyncTask<Void, Void, String>{

	private ItineraryAdapter ad;
	private ProgressBar pb;
	
	public GetItinerary(ProgressBar pb, ItineraryAdapter ad) {
		// TODO Auto-generated constructor stub
		this.ad = ad;
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
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.d("position", "inonPostExe"+result);
		if(pb!=null)
			pb.setVisibility(View.INVISIBLE);
	     	ad.setItems(ItineraryList.itineraryList);
			ad.notifyDataSetChanged();
			
	}
	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		ItineraryList.itineraryList = DBFunction.getItinerary(MyApplication.dbHnalder);
		
		return "";
	}
}
