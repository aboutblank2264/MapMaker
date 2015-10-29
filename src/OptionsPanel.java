import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.*;

public class OptionsPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2L; // make warning go away
	private static final String[] TileTypeOptions = {"Blank", "Door"};
	private static final String[] GridLayout = {"Square", "Rectangle"};
	
	private MapLayoutPanel map;
	
	public OptionsPanel(MapLayoutPanel m, int height) {
		setLayout(new GridBagLayout());
		
		map = m;
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0;
		c.ipadx = 3;
		c.insets = new Insets(5, 0, 5, 0);
		c.weighty = 1.0;
		
		c.gridx = 0;
		c.gridy = 0;
		JLabel wL = new JLabel("Width");
		add(wL, c);

		c.gridx = 1;
		JFormattedTextField wT = new JFormattedTextField(NumberFormat.getInstance());
		wT.setColumns(3);
		add(wT, c);

		c.gridx = 2;
		JLabel hL = new JLabel("Height");
		add(hL, c);

		c.gridx = 3;
		JFormattedTextField hT = new JFormattedTextField(NumberFormat.getInstance());
		hT.setColumns(3);
		add(hT, c);
		
		/*
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		c.weightx = 1;
		JButton update = new JButton("Update");
		update.setActionCommand("update");
		update.addActionListener(this);
		*/
		
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1.0;
		c.weightx = 1;
		JButton clear = new JButton("Clear All");
		clear.setActionCommand("clear");
		clear.addActionListener(this);
		add(clear, c);
		
		c.gridwidth = 2;
		c.gridx = 2;
		c.weightx = 1;
		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("refresh");
		refresh.addActionListener(this);
		add(refresh, c);

		c.gridx = 1;
		c.gridy++;
		c.weightx = 1;
		JButton generate = new JButton("Generate");
		generate.setActionCommand("generate");
		generate.addActionListener(this);
		add(generate, c);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy++;
		JCheckBox edit = new JCheckBox("Edit");
		edit.setActionCommand("edit");
		edit.addActionListener(this);
		add(edit, c);
		
		c.gridx = 2;
		JCheckBox pad = new JCheckBox("Rectangle Rooms");
		pad.setSelected(true);
		pad.setActionCommand("padding");
		pad.addActionListener(this);
		add(pad, c);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch(action) {
		case "clear":
			map.clearMap();
			break;
		case "refresh":
			map.repaint();
			break;
		case "edit":
			map.canEdit();
			break;
		case "padding":
			map.usePadding();
			break;
		case "generate":
//			Thread th = new Thread(new GenerateRunner());
//			th.start();
			map.generateMap();
			break;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, getHeight());
	}
	
	public class GenerateRunner implements Runnable {
		
		public void run() {
			System.out.println("Begin Generating");
			map.generateMap();
			System.out.println("End Generating");
		}
	}
}
