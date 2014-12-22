package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
import itc.gic.model.Itinerary;
import itc.gic.model.Spot;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ItineraryAdapter extends BaseAdapter{
	private Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Itinerary> items;
    //public ImageLoader imageLoader;
 
    public ItineraryAdapter(Context context, ArrayList<Itinerary> items) {
    	this.context = context;
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    @Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(items == null){
			return 0; 
		}
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	class MyTag {
		TextView name;
		TextView description;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Itinerary item = items.get(index);
		
		final MyTag holder; 
		View view = convertView;
		
		if (view == null){
			//Log.d("test", "test");
			holder = new MyTag();
			view = inflater.inflate(R.layout.list_itinerary, parent, false);
			holder.name = (TextView)view.findViewById(R.id.name);
			holder.description = (TextView)view.findViewById(R.id.description);
		    view.setTag(holder);
	        
		} else {
			holder = (MyTag)view.getTag();
		}
		holder.name.setText(item.name);
		holder.description.setText(item.description);
		
		return view;
	}
	
	public void setItems(ArrayList<Itinerary> items){
		this.items = items;
	} 
}
