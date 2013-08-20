package de.poneleit.jergometer.gui;

import org.jergometer.Jergometer;
import org.jergometer.communication.BikeListener;
import org.jergometer.model.DataRecord;

import android.app.Activity;
import android.os.Bundle;
import de.poneleit.jergometer.R;

public class CurrentStatsActivity extends Activity {
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.currentstats_activity);
	    new Jergometer(true, this);
	}

	
}
