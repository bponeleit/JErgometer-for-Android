package org.jergometer.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class UsbSerialOutputStream extends OutputStream {
	private static final String TAG = UsbSerialOutputStream.class.getSimpleName();
	
	private UsbSerialDriver driver; 
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	public UsbSerialOutputStream(UsbSerialDriver driver) {
		this.driver = driver;
	}

	@Override
	public void close() throws IOException {
		Log.d(TAG, "close");
		driver.close();
	}

	@Override
	public void flush() throws IOException {
		Log.d(TAG, "flush");
		driver.write(buffer.toByteArray(), 1000);
		buffer.reset();
	}

	@Override
	public void write(byte[] buffer, int offset, int count) throws IOException {
		Log.d(TAG, "write " + offset + " " + count);
		this.buffer.write(buffer, offset, count);
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		Log.d(TAG, "write ");
		this.buffer.write(buffer);
	}

	@Override
	public void write(int arg0) throws IOException {
		Log.d(TAG, "write " + arg0);
		this.buffer.write(arg0);
	}

}
