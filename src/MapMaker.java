import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.*;

public class MapMaker {
	private final static int DEFAULT_MAP_W = 41;
	private final static int DEFAULT_MAP_H = 41;
	private final static int DEFAULT_WINDOW_W = 975;
	private final static int DEFAULT_WINDOW_H = 620;
	
	private static JFrame window;
	private static JScrollPane mapPanelHolder;
	private static MapLayoutPanel mapPanel;
	private static OptionsPanel optionsPanel;
	
	private static void createAndShowGui() {
		window = new JFrame("Map Creator");
		
		mapPanel = new MapLayoutPanel(DEFAULT_MAP_W, DEFAULT_MAP_H);
		
		optionsPanel = new OptionsPanel(mapPanel, DEFAULT_WINDOW_H);
		optionsPanel.setPreferredSize(new Dimension(200, DEFAULT_WINDOW_H));
		
		mapPanelHolder = new JScrollPane(mapPanel);
		
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				optionsPanel, mapPanelHolder);
		splitpane.setEnabled(false);
		splitpane.setDividerSize(2);
		splitpane.setResizeWeight(0);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(DEFAULT_WINDOW_W, DEFAULT_WINDOW_H);
		window.getContentPane().add(splitpane, BorderLayout.CENTER);
//		window.setLocationByPlatform(false);
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