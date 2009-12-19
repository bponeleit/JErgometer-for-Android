package org.jergometer.diagram;

import org.jergometer.control.BikeProgram;
import org.jergometer.gui.Diagram;
import org.jergometer.model.BikeProgramData;

import java.awt.*;

/**
 * Used to visualize a bike program in the diagram.
 */
public class BikeProgramVisualizer implements DiagramVisualizer {
	private final Diagram diagram;
	private boolean stopped = false;

	public BikeProgramVisualizer(Diagram diagram) {
		this.diagram = diagram;
	}

	public void visualize(BikeProgram bikeProgram) {
		synchronized(diagram) {
			BikeProgramData data = bikeProgram.getProgramData();
			diagram.setTimeRange(new Diagram.Range(0,data.getDuration()));
			diagram.setTimeAxisType(Diagram.TimeAxisType.minute);

			diagram.clearGraphs();
			diagram.addGraph("pulse-dest", "Dest. Pulse", new Color(255,0,0), Diagram.Side.left);
			diagram.addGraph("pedalRPM-dest", "Dest. Pedal RPM", new Color(0,255,0), Diagram.Side.left);
			diagram.addGraph("power-dest", "Dest. Power", new Color(0,0,255), Diagram.Side.left);

			BikeProgramData.Action lastAction = null;

			for (BikeProgramData.TimeEvent event : data.getEvents()) {
				for (BikeProgramData.Action action : event.getActions()) {
					if (stopped) return;

					switchAction(event.getTime(), lastAction, action);
					lastAction = action;
				}
			}

			switchAction(data.getDuration(), lastAction, null);
		}
	}

	private void switchAction(int time, BikeProgramData.Action action1, BikeProgramData.Action action2) {
		if (action1 == null) {
			actionUp(time, action2);
		}
		else if (action2 == null) {
			actionDown(time, action1);
		}
		else if (action1.getType() == action2.getType()) {
			addActionPoint(time, action1.getType(), action1.getValue());
			addActionPoint(time, action2.getType(), action2.getValue());
		}
		else {
			actionDown(time, action1);
			actionUp(time, action2);
		}
	}

	private void actionUp(int time, BikeProgramData.Action action) {
		addActionPoint(time, action.getType(), 0);
		addActionPoint(time, action.getType(), action.getValue());
	}

	private void actionDown(int time, BikeProgramData.Action action) {
		addActionPoint(time, action.getType(), action.getValue());
		addActionPoint(time, action.getType(), 0);
	}

	private void addActionPoint(int time, BikeProgramData.Action.Type type, int value) {
		if (type == BikeProgramData.Action.Type.power) {
			diagram.addValue("power-dest", time, value);
		}
		else if (type == BikeProgramData.Action.Type.pulse) {
			diagram.addValue("pulse-dest", time, value);
		}
	}

// DiagramVisualizer
	public void stopVisualization() {
		stopped = true;
	}
}
