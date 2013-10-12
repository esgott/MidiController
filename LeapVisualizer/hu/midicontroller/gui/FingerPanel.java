package hu.midicontroller.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class FingerPanel extends JPanel {

	private static final long serialVersionUID = -5383550741484631965L;

	private final JLabel label = new JLabel();
	private final JProgressBar progressBar = new JProgressBar();
	private final JButton hitButton = new JButton("Hit");

	public FingerPanel(int fingerNum) {
		label.setText(String.format("%02d:", fingerNum));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(label);
		this.add(progressBar);
		this.add(hitButton);
	}

}
