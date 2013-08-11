package org.jergometer.communication;

import de.poneleit.jergometer.R;

/**
 * Unconfigured serial port exception.
 */
public class UnconfiguredSerialPortException extends Exception {
	public UnconfiguredSerialPortException() {
		super("The serial port has not been configured yet.");
	}
}
