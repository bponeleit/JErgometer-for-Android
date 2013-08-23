package de.poneleit.jergometer.gui;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.jergometer.Jergometer;
import org.jergometer.communication.BikeListener;
import org.jergometer.model.DataRecord;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.poneleit.jergometer.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hoho.android.usbserial.driver.*;
import com.hoho.android.usbserial.util.HexDump;

import de.poneleit.jergometer.R.id;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.EditText;

public class CurrentStatsActivity extends Activity implements OnClickListener {

	private UsbSerialDriver mDriver;
	private SerialInputOutputManager mSerialIoManager;
	private Jergometer mJergometer;
	private Button mStartButton;
	private Button mStopButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentstats_activity);
		mJergometer = new Jergometer(true, this);
		// mDumpTextView = (TextView) findViewById(R.id.textView2);
		mStartButton = (Button) findViewById(R.id.startButton);
		mStartButton.setOnClickListener(this);
		mStopButton = (Button) findViewById(R.id.stopButton);
		mStopButton.setOnClickListener(this);

		// // Get UsbManager from Android.
		// UsbManager manager = (UsbManager)
		// getSystemService(Context.USB_SERVICE);
		// List<UsbSerialDriver> mDrivers = null;
		// Map<String, UsbDevice> devices = manager.getDeviceList();
		// for (String key : devices.keySet()) {
		// mDumpTextView.append("key: " + key + "\n");
		//
		// }
		// for (UsbDevice device : manager.getDeviceList().values()) {
		// mDumpTextView.append("device: " + device.getDeviceName() + "\n");
		// mDrivers = UsbSerialProber.probeSingleDevice(manager, device);
		// }
		//
		// // Find the first available mDriver.
		// if (mDrivers != null) {
		// try {
		// mDriver = mDrivers.get(0);
		// } catch (IndexOutOfBoundsException e) {
		// // TODO: handle exception
		// mDumpTextView.append(e.toString() + "\n");
		// }
		// } else {
		// mDumpTextView.append("No drivers found\n");
		// }
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.startButton) {
			mStartButton.setVisibility(View.INVISIBLE);
			mStopButton.setVisibility(View.VISIBLE);
			mJergometer.startRecording();
			
		} else { 
			mStartButton.setVisibility(View.VISIBLE);
			mStopButton.setVisibility(View.INVISIBLE);
			mJergometer.stopRecording();
			
		}
		// if (mDriver != null) {
		// try {
		//
		// /** Newline. */
		// byte[] ln = {'\n'};
		// final String CMD_RESET = "RS";
		// mDriver.open();
		//
		// mDriver.setParameters(9600, UsbSerialDriver.DATABITS_8,
		// UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
		//
		//
		// mDumpTextView.append("Before" + "\n");
		// //byte buffer[] = CMD_RESET.getBytes();
		// EditText e = (EditText)findViewById(id.editText1);
		// byte buffer[] = e.getText().toString().getBytes();
		//
		// int numBytesRead = mDriver.write(buffer, 1000);
		// numBytesRead = mDriver.write(ln, 1000);
		// Log.d(TAG, "Wrote " + numBytesRead + " bytes.");
		// mDumpTextView.append("Read " + numBytesRead + " bytes." + "\n");
		// } catch (IOException e) {
		// // Deal with error.
		//
		// mDumpTextView.append(e.toString() + "\n");
		// } finally {
		// try {
		// mDriver.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		//
		// mDumpTextView.append(e.toString());
		//
		// }ch
		// }
		// }
		// else {
		// mDumpTextView.append("No Driver" + "\n");
		// }
		//
	}

}
