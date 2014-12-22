package itc.gic.trafficreportingsystem;

import itc.gic.application.MyApplication;
import itc.gic.model.Itinerary;
import itc.gic.trafficreportingsystem.database.DBFunction;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.Window;

public class AddItineraryDialog extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_itinerary);
		
		final EditText name = (EditText) findViewById(R.id.name);
		final EditText description = (EditText) findViewById(R.id.description);
		
		Button okBtn = (Button) findViewById(R.id.ok);
    	okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				ItineraryList.itineraryList.add(new Itinerary(id, name, description))
	
				DBFunction.addItinerary(name.getText().toString(), description.getText().toString(), MyApplication.dbHnalder);
				
				finish();
				
			}
		});
    	
    	Button cancelBtn = (Button) findViewById(R.id.cancel);
    	cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				finish();
			}
		});

		
	}

	
}