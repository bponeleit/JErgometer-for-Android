package de.poneleit.jergometer.gui;

import org.jergometer.communication.BikeListener;
import org.jergometer.model.DataRecord;

import android.app.Activity;
import android.os.Bundle;
import de.poneleit.jergometer.R;

public class CurrentStatsActivity extends Activity implements BikeListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.currentstats_activity);
	}

	@Override
	public void bikeAck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bikeData(DataRecord data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bikeError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bikeDestPowerChanged(int change) {
		// TODO Auto-generated method stub
		
	}

}
