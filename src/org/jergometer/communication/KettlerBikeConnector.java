package org.jergometer.communication;

import java.io.IOException;
import android.app.Activity;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.jergometer.translation.I18n;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import de.poneleit.jergometer.R;

/**
 * KettlerBikeConnector connects to the bike via serial port (e.g. RS232 or
 * USB). It is used to receive data from the bike and to control it.
 */
public class KettlerBikeConnector implements BikeConnector {
	
	private static final String TAG = KettlerBikeConnector.class.getSimpleName();

	// dynamic

	public Context context = null;
	private UsbSerialDriver mDriver = null;
	public KettlerBikeReader reader = null;
	public KettlerBikeWriter writer = null;
	private int power;

	public void connect(String serialName, BikeListener listener) throws BikeException, IOException {
		Log.d(TAG, "Connect to " + serialName + "...");
		// Get UsbManager from Android.
		TextView mDumpTextView = (TextView)((Activity)context).findViewById(R.id.textView2);
		UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		List<UsbSerialDriver> mDrivers = null;
		Map<String, UsbDevice> devices = manager.getDeviceList();
		for (UsbDevice device : manager.getDeviceList().values()) {			
			mDrivers = UsbSerialProber.probeSingleDevice(manager, device);
		}

		// Find the first available mDriver.
		if (mDrivers != null) {
			try {
				mDriver = mDrivers.get(0);
				connect(mDriver);
			} catch (IndexOutOfBoundsException e) {
				
				// TODO: handle exception
				Log.e(TAG, "No in list driver found", e);
				mDumpTextView.setText(e.toString() + "\n");
				throw new IOException(e);
			}
		} else {
			Log.e(TAG, "No drivers found");
			mDumpTextView.setText("No drivers found\n");
			throw new IOException("No drivers found!");
		}
//		mDriver.open();
		reader.addBikeReaderListener(listener);
//		((Activity)context).runOnUiThread(reader);
		reader.start();
	}

	public void connect(UsbSerialDriver serialPort) throws  IOException {
		Log.d(TAG, "connect serialPort " + serialPort.toString());
		serialPort.open();
		serialPort.setParameters(9600, UsbSerialDriver.DATABITS_8,
				UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);

		// set reader and writer
		UsbSerialOutputStream usbWriter = new UsbSerialOutputStream(serialPort);
		writer = new KettlerBikeWriter(true, usbWriter);
//		UsbSerialInputStream usbReader = new UsbSerialInputStream(serialPort);
		reader = new KettlerBikeReader(serialPort, context);
	}

	@Override
	public void sendHello() throws IOException {
//		if (!reader.isAlive()) {
//			reader.start();
//		}
		if (writer != null)
		writer.sendHello();
	}

	@Override
	public void sendReset() throws IOException {
//		if (!reader.isAlive()) {
//			reader.start();
//		}
		if (writer != null)
			writer.sendReset();
	}

	@Override
	public void sendGetId() throws IOException {
		writer.sendGetId();
	}

	@Override
	public void sendGetData() throws IOException {
		if (writer == null)
			return;
		if (power != 0) {
			writer.sendSetPower(power);
			power = 0;
		} else {
			writer.sendGetData();
		}
	}

	@Override
	public void sendSetPower(int power) throws IOException {
		reader.setJErgometerDestPower(power);
		this.power = power;
	}

	@Override
	public void close() throws IOException {
		// stop reader and writer
		if (reader != null) {
			reader.removeAllBikeReaderListeners();
			reader.close();
			reader = null;
		}
		if (writer != null) {
			writer = null;
		}

		// close streams and socket
		if (mDriver != null) {
			mDriver.close();
		}
	}

	@Override
	public String getName() {
		return "Kettler-USBSerialDriver";
	}

	@Override
	public String toString() {
		return getName();
	}
}
