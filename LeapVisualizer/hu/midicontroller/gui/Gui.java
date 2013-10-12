package hu.midicontroller.gui;

import hu.midicontroller.Constants;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Gui {

	private JFrame frame = new JFrame("Visualizer");
	private FingerPanel[] fingerPanels = new FingerPanel[Constants.NUM_OF_FINGERS];

	public Gui() {
		for (int i = 0; i < Constants.NUM_OF_FINGERS; i++) {
			fingerPanels[i] = new FingerPanel(i + 1);
		}
	}

	public void createAndShowGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.setSize(500, 350);
		frame.setVisible(true);
	}

	private void addComponentsToPane(Container mainPane) {
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		for (FingerPanel fingerPanel : fingerPanels) {
			mainPane.add(fingerPanel);
		}
	}

}
