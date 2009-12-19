package org.jergometer.model;

import org.jergometer.translation.I18n;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * TabelModel for the session table.
 */
public class SessionTableModel extends AbstractTableModel {
	private String[] columnNames = new String[]{ I18n.getString("property.date"), I18n.getString("property.program"), I18n.getString("property.pulse"), I18n.getString("property.power"), I18n.getString("property.duration") };
	private ArrayList<BikeSession> sessions;

	public SessionTableModel(ArrayList<BikeSession> sessions) {
		this.sessions = sessions;
	}

	public String getColumnName(int column) { return columnNames[column]; }
	public int getRowCount() { return sessions.size(); }
	public int getColumnCount() { return columnNames.length; }
	public boolean isCellEditable(int row, int column) { return false; }
	public void setValueAt(Object value, int row, int col) { }

	public Object getValueAt(int row, int col) {
		BikeSession bikeSession = sessions.get(row);

		switch(col) {
			case 0: return String.format("%1$td.%1$tm.%1$ty %1$tH:%1$tM", bikeSession.getStartTime());
			case 1: return bikeSession.getProgramName();
			case 2: return String.format("%.1f", bikeSession.getAveragePulse());
			case 3: return String.format("%.1f", bikeSession.getAveragePower());
			case 4:
				int duration = Math.min(bikeSession.getDuration(), bikeSession.getProgramDuration());
				int sec = duration % 60;
				int min = (duration / 60) % 60;
				int h = (duration / 3600);
				return String.format("%1$d:%2$02d:%3$02d", h, min, sec);
			default: return "";
		}
	}

	public BikeSession getSessionAtRow(int row) {
		return sessions.get(row);
	}
}