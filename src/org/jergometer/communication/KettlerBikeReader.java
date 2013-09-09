package org.jergometer.communication;

import org.jergometer.model.DataRecord;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

import de.poneleit.jergometer.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * It reads incoming messages from the bike.
 */
public class KettlerBikeReader extends Thread {

	// static
	private static final String TAG = KettlerBikeReader.class.getSimpleName();

	public static enum PrintAvailable {
		none, characters, decimals, hexadecimal
	}

	/** Client commands. */
	public static final String CMD_ACK = "ACK";
	public static final String CMD_ERROR = "ERROR";
	public static final String CMD_RUN = "RUN";

	// dynamic

	/** Input stream. */
	// private InputStream inStream;
	private boolean closed = false;

	private static final int BUFSIZ = 4096;

	private final UsbSerialDriver mDriver;

	private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUFSIZ);
	// private BufferedReader in;
	/** BikeReaderListeners. */
	private ArrayList<BikeListener> bikeListeners = new ArrayList<BikeListener>();
	/** Print available bytes (for debugging). */
	private PrintAvailable printAvailable = PrintAvailable.none;
	private int lastJergometerDesPower = 0;
	private int jergometerDestPower = 0;

	/**
	 * Creates the reader for the incoming messages of the bike.
	 * 
	 * @param in
	 *            input stream
	 */
	public KettlerBikeReader(UsbSerialDriver driver) {
		mDriver = driver;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				String dataString = null;
				StringBuffer buf = new StringBuffer();
				int len = 0;
				do {
					mDriver.read(mReadBuffer.array(), 1000);
					if (len > 0) {
						Log.d(TAG, "Read data len=" + len);
						final byte[] data = new byte[len];
						mReadBuffer.get(data, 0, len);
						dataString = new String(data);
						buf.append(dataString);
						// dataString = new String(mReadBuffer);
						Log.d(TAG, "dataString: " + dataString);
						mReadBuffer.clear();
						// mReadBuffer = new byte[BUFSIZ];
					}
				} while (len > 0);
				if (buf.length() > 0)
					dataString = buf.toString();

				if (dataString == null || closed) {
					return;
				}

				if (printAvailable == PrintAvailable.none) {
					if (dataString.contains(CMD_ACK)) {
						for (BikeListener listener : bikeListeners) {
							listener.bikeAck();
						}
					} else if (dataString.equals(CMD_ERROR)) {
						for (BikeListener listener : bikeListeners) {
							listener.bikeError();
						}
					} else if (dataString.equals(CMD_RUN)) {
						for (BikeListener listener : bikeListeners) {
							listener.bikeAck();
						}
					} else {
						DataRecord data = new DataRecord(dataString);

						if (lastJergometerDesPower == 0) {
							lastJergometerDesPower = data.getDestPower();
						}

						for (BikeListener listener : bikeListeners) {
							if (data.getDestPower() != lastJergometerDesPower
									&& data.getDestPower() != jergometerDestPower) {
								listener.bikeDestPowerChanged((data
										.getDestPower() - jergometerDestPower) / 5);
							}
							listener.bikeData(data);
						}
						lastJergometerDesPower = data.getDestPower();
					}
				} else {
					// for debugging
					byte[] bytes = dataString.getBytes();

					if (printAvailable == PrintAvailable.characters) {
						System.err.print(dataString);
					} else if (printAvailable == PrintAvailable.decimals) {
						for (byte aByte : bytes) {
							System.out.print("," + (aByte & 0xFF));
						}
					} else if (printAvailable == PrintAvailable.hexadecimal) {
						for (byte aByte : bytes) {
							System.out.format(",%X", (aByte & 0xFF));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			yield();
		}
	}

	public void close() {
		closed = true;
		this.interrupt();
		super.interrupt();
	}

	public PrintAvailable getPrintAvailable() {
		return printAvailable;
	}

	public void setPrintAvailable(PrintAvailable printAvailable) {
		this.printAvailable = printAvailable;
	}

	public void addBikeReaderListener(BikeListener listener) {
		bikeListeners.add(listener);
	}

	public void removeBikeReaderListener(BikeListener listener) {
		bikeListeners.remove(listener);
	}

	public void removeAllBikeReaderListeners() {
		bikeListeners.clear();
	}

	public void setJErgometerDestPower(int jergometerDestPower) {
		this.jergometerDestPower = jergometerDestPower;
	}
}
