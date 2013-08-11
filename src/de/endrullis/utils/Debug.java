package de.endrullis.utils;

import de.poneleit.jergometer.R;
import java.io.PrintStream;

/**
 * Output.
 */
public class Debug {
  public static PrintStream out;

  public static void setOutput(PrintStream out) {
    Debug.out = out;
  }
}
