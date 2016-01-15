import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class MapMaker {
	
	private static JFrame window;
	private static JScrollPane mapPanelHolder;
	private static MapLayoutPanel mapPanel;
	private static OptionsPanel optionsPanel;
	
	private static void createAndShowGui() {
		window = new JFrame("Map Creator");
		
		mapPanel = new MapLayoutPanel(Global.MapWidth, Global.MapHeight);
		
		optionsPanel = new OptionsPanel(mapPanel, Global.DEFAULT_WINDOW_H);
		optionsPanel.setPreferredSize(new Dimension(Global.DEFAULT_OPTION_W,
				Global.DEFAULT_WINDOW_H));
		
		mapPanelHolder = new JScrollPane(mapPanel);
		
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				optionsPanel, mapPanelHolder);
		splitpane.setEnabled(false);
		splitpane.setDividerSize(2);
		splitpane.setResizeWeight(0);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(Global.DEFAULT_WINDOW_W, Global.DEFAULT_WINDOW_H);
		window.getContentPane().add(splitpane, BorderLayout.CENTER);
//		window.pack();
		window.setVisible(true);
	};
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}