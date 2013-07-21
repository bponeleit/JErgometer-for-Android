package org.jergometer.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.jergometer.translation.I18n;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

/**
 * KettlerBikeConnector connects to the bike via serial port (e.g. RS232 or
 * USB). It is used to receive data from the bike and to control it.
 */
public class KettlerBikeConnector implements BikeConnector {

	// dynamic

	private Context context = null;
	private UsbSerialDriver mDriver = null;
	public KettlerBikeReader reader = null;
	public KettlerBikeWriter writer = null;
	private int power;

	public void connect(String serialName, BikeListener listener) throws BikeException, IOException {
		// Get UsbManager from Android.
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
				//mDumpTextView.append(e.toString() + "\n");
			}
		} else {
			//mDumpTextView.append("No drivers found\n");
		}
		reader.addBikeReaderListener(listener);
		reader.start();
	}

	public void connect(UsbSerialDriver serialPort) throws  IOException {
		serialPort.setParameters(9600, UsbSerialDriver.DATABITS_8,
				UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);

		// set reader and writer
		UsbSerialOutputStream usbWriter = new UsbSerialOutputStream(serialPort);
		writer = new KettlerBikeWriter(true, usbWriter);
		UsbSerialInputStream usbReader = new UsbSerialInputStream(serialPort);
		reader = new KettlerBikeReader(usbReader);
	}

	@Override
	public void sendHello() throws IOException {
		writer.sendHello();
	}

	@Override
	public void sendReset() throws IOException {
		writer.sendReset();
	}

	@Override
	public void sendGetId() throws IOException {
		writer.sendGetId();
	}

	@Override
	public void sendGetData() throws IOException {
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
