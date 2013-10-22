package hu.midicontroller.gui;

public class GuiUpdater implements Runnable {
	
	private final FingerPanel panel;
	private final int position;
	private final boolean tap;

	public GuiUpdater(FingerPanel panel, int position, boolean tap) {
		this.panel = panel;
		this.position = position;
		this.tap = tap;
	}

	@Override
	public void run() {
		panel.setNewData(position, tap);
	}

}
