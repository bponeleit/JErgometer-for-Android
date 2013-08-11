package org.jergometer.communication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import de.poneleit.jergometer.R;

public class UsbSerialOutputStream extends OutputStream {
	
	private UsbSerialDriver driver; 
	private byte[] buffer;

	public UsbSerialOutputStream(UsbSerialDriver driver) {
		this.driver = driver;
	}

	@Override
	public void close() throws IOException {
		driver.close();
	}

	@Override
	public void flush() throws IOException {
		driver.write(buffer, 1000);
	}

	@Override
	public void write(byte[] buffer, int offset, int count) throws IOException {
		System.arraycopy(buffer, offset, this.buffer, this.buffer.length, count);
	}

	@Override
	public void write(byte[] buffer) throws IOException {
		System.arraycopy(buffer, 0, this.buffer, this.buffer.length, buffer.length);
	}

	@Override
	public void write(int arg0) throws IOException {
		byte[] buffer = new byte[1];
		buffer[0] = (byte)arg0;
		System.arraycopy(buffer, 0, this.buffer, this.buffer.length, buffer.length);
	}

}
