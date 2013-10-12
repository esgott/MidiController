package hu.midicontroller.gui;

public class Main {

	private static Gui gui = new Gui();;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.createAndShowGUI();
			}
		});
	}

}
