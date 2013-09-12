package org.jergometer.communication;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import de.poneleit.jergometer.R;

public class UsbSerialInputStream extends InputStream {
	
	private static final String TAG = UsbSerialInputStream.class.getSimpleName();
	
	private UsbSerialDriver driver;
	private byte[] buffer = new byte[64];	
	private static final int MILLI_SECONDS = 1000;
	
	public UsbSerialInputStream(UsbSerialDriver driver) throws IOException {
		this.driver = driver;
	}

	@Override
	public int read() throws IOException {
		int r = driver.read(buffer, MILLI_SECONDS);
		if (r > 0)
			Log.d(TAG, "read");
//		return driver.read(buffer, MILLI_SECONDS);
		return r;
	}

	@Override
	public void close() throws IOException {
		Log.d(TAG, "close");
		driver.close();
	}


	@Override
	public int read(byte[] buffer) throws IOException {
		Log.d(TAG, "read buffer");
		return driver.read(buffer, MILLI_SECONDS);
	}
	
	

}
