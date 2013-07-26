package org.jergometer.communication;

import java.io.IOException;
import java.io.InputStream;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class UsbSerialInputStream extends InputStream {
	
	private UsbSerialDriver driver;
	private byte[] buffer;	
	private static final int MILLI_SECONDS = 1000;
	
	public UsbSerialInputStream(UsbSerialDriver driver) {
		this.driver = driver;
	}

	@Override
	public int read() throws IOException {
		return driver.read(buffer, MILLI_SECONDS);
	}

	@Override
	public void close() throws IOException {
	}


	@Override
	public int read(byte[] buffer) throws IOException {
		return driver.read(buffer, MILLI_SECONDS);
	}
	
	

}
