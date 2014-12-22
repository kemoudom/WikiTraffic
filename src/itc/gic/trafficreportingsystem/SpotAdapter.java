package itc.gic.trafficreportingsystem;

import itc.edu.tools.MyTool;
import itc.gic.application.MyApplication;
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

public class SpotAdapter extends BaseAdapter{
	private Context context;
    private static LayoutInflater inflater=null;
    ArrayList<Spot> items;
    //public ImageLoader imageLoader;
 
    public SpotAdapter(Context context, ArrayList<Spot> items) {
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
		ImageView image;
		TextView name;
		TextView address;
		TextView text_distance, distance;
		ImageView degree_image;
		TextView degree, text_number_report, number_report;
		String degree_status;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Spot item = items.get(index);
		
		final MyTag holder; 
		View view = convertView;
		
		//if (view == null){
			//Log.d("test", "test");
			holder = new MyTag();
			view = inflater.inflate(R.layout.list_row, parent, false);
			holder.image = (ImageView)view.findViewById(R.id.list_image);    
			holder.name = (TextView)view.findViewById(R.id.name);
			//holder.text_distance = (TextView)view.findViewById(R.id.text_distance);
			//holder.distance = (TextView)view.findViewById(R.id.distance);
			
			holder.address = (TextView)view.findViewById(R.id.address);
			holder.degree_image = (ImageView)view.findViewById(R.id.degree_image);
			holder.degree = (TextView)view.findViewById(R.id.degree);
			holder.text_number_report = (TextView)view.findViewById(R.id.text_number_report);
			holder.number_report = (TextView)view.findViewById(R.id.number_report);
	        view.setTag(holder);
	        
		/*} else {
			holder = (MyTag)view.getTag();
		}*/
		MyApplication.imageLoader.displayImage(item.getImg(), holder.image, MyApplication.detailOptions);
		holder.name.setText(item.getName());
		holder.address.setText(item.getAddress());
/*		if(item.getDistance()>=0){
			holder.distance.setText(new DecimalFormat("##.##").format(item.getDistance())+" km");	
		}else{
			holder.distance.setText("Unknown");
		}
*/		//holder.degree.setText(holder.degree_status);	
		MyTool.getDegreeStatus(context, item.getDegree(), holder.degree_image, holder.degree);
		holder.text_number_report.setText("Number Reported: ");		
		holder.number_report.setText(item.getNumberReport()+"");
		
		return view;
	}
	
	public void setItems(ArrayList<Spot> items){
		this.items = items;
	} 
}
