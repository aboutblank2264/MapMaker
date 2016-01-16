import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OptionsPanel extends JPanel implements ActionListener, ChangeListener {
	
	private static final long serialVersionUID = 2L; // make warning go away
	
	private final Font defaultFont = new Font("Dialog", Font.BOLD, 10);
	private final Font checkFont = new Font("Dialog", Font.PLAIN, 9);
	private final Font comboFont = new Font("Dialog", Font.PLAIN, 10);
	
	private final Insets textInset = new Insets(0, 5, 5, 0);
	private final Insets checkInset = new Insets(5, 5, 5, 5);
	private final Insets sliderInset = new Insets(0, 0, 5, 0);
	private final Insets comboInset = new Insets(0, 5, 5, 0);
	private final Insets buttonInset = new Insets(0, 5, 5, 0);
	
	private MapLayoutPanel map;
	
	private boolean equals = false;
	
	private Thread genThread;
	
	public OptionsPanel(MapLayoutPanel m, int height) {
		map = m;
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		add(createOptionPanel(), BorderLayout.NORTH);
		add(createButtonPanel(), BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
//		System.out.println(action);
		
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
			Global.RoomUsePadding = !Global.RoomUsePadding;
			break;
		case "equal":
			equals = !equals;
			break;
		case "path_length":
			Global.PathMinLength = ((Global.PathLength)path_combo.getSelectedItem()).length;
			break;
		case "path_straight":
			Global.PathSameDirectionPercent =
			((Global.PathStraightness)straight_combo.getSelectedItem()).percent;
			break;
		case "deadends":
			Global.PathDeadEndPercent =
			((Global.DeadEndPercent)dead_combo.getSelectedItem()).percent;
			break;
		case "door_type":
			break;
		case "room_size":
			Global.RoomSizes = ((Global.RoomSize)room_combo.getSelectedItem());
			break;
		case "choose":
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Choose a text map file");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
			fc.setCurrentDirectory(new File("src/../"));
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				txtImport.setText(fc.getSelectedFile().getAbsolutePath());
				GridMap m = MapImportExport.importMap(fc.getSelectedFile());
				if(m != null) {
					map.setMapLayout(m);
				}
			}
			break;
		case "generate":
			map.generateMap();
//			if(genThread == null || !genThread.isAlive()) {
//				genThread = new Thread(new GenerateRunner());
//				genThread.start();
//			} else {
//				System.out.println(genThread.getName() + " " + genThread.getState());
//				genThread.interrupt();
//			}
			break;
		}
	}
	

	@Override
	public void stateChanged(ChangeEvent event) {
		Component c = (Component)event.getSource();
		String name = c.getName();
		
		switch (name) {
			case "width_slider":
				width_spinner.setValue(width_slider.getValue());
				if(equals) {
					height_spinner.setValue(width_spinner.getValue());
				}
				break;
			case "width_spinner":
				width_slider.setValue((int)width_spinner.getValue());
				break;
			case "height_slider":
				height_spinner.setValue(height_slider.getValue());
				if(equals) {
					width_spinner.setValue(height_spinner.getValue());
				}
				break;
			case "height_spinner":
				height_slider.setValue((int)height_spinner.getValue());
				break;
		};
		
		Global.MapWidth = width_slider.getValue();
		Global.MapHeight = height_slider.getValue();
	}

	JSlider width_slider = new JSlider();
	JSpinner width_spinner = new JSpinner();
	JSlider height_slider = new JSlider();
	JSpinner height_spinner = new JSpinner();
	
	JComboBox<Global.DoorTypes> door_combo = new JComboBox<>(Global.DoorTypes.values());
	JComboBox<Global.DeadEndPercent> dead_combo = new JComboBox<>(Global.DeadEndPercent.values());
	JComboBox<Global.RoomSize> room_combo = new JComboBox<>((Global.RoomSize[])Global.RoomSize.getValues().toArray());
	JComboBox<Global.PathLength> path_combo = new JComboBox<>(Global.PathLength.values());
	JComboBox<Global.PathStraightness> straight_combo = new JComboBox<>(Global.PathStraightness.values());

	JTextField txtImport = new JTextField();
	
	private JPanel createOptionPanel() {
		JPanel optionpanel = new JPanel();
		optionpanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gridbag = new GridBagConstraints();
		gridbag.anchor = GridBagConstraints.NORTHWEST;
		gridbag.weightx = 1;
		gridbag.weighty = 1;
		gridbag.fill = GridBagConstraints.HORIZONTAL;
		
		gridbag.gridx = 0;
		gridbag.gridy = 0;

		JLabel lblWidth= new JLabel("Width");
		lblWidth.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(lblWidth, gridbag);

		width_slider.setMinimum(Global.MINIMUM_MAP_SIZE);
		width_slider.setMaximum(Global.MAXIMUM_MAP_SIZE);
		width_slider.setValue(Global.MapWidth);
		width_slider.setName("width_slider");
		width_slider.addChangeListener(this);
		gridbag.insets = sliderInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		gridbag.ipadx = 30;
		optionpanel.add(width_slider, gridbag);

		width_spinner.setValue(Global.MapWidth);
		width_spinner.setName("width_spinner");
		width_spinner.addChangeListener(this);
		gridbag.insets = textInset;
		gridbag.gridx = 1;
		gridbag.ipadx = 0;
		optionpanel.add(width_spinner, gridbag);

		JLabel lblHeight = new JLabel("Height");
		lblHeight.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(lblHeight, gridbag);

		height_slider.setMinimum(Global.MINIMUM_MAP_SIZE);
		height_slider.setMaximum(Global.MAXIMUM_MAP_SIZE);
		height_slider.setValue(Global.MapHeight);
		height_slider.setName("height_slider");
		height_slider.addChangeListener(this);
		gridbag.insets = sliderInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		gridbag.ipadx = 30;
		optionpanel.add(height_slider, gridbag);

		height_spinner.setValue(Global.MapHeight);
		height_spinner.setName("height_spinner");
		height_spinner.addChangeListener(this);
		gridbag.insets = comboInset;
		gridbag.gridx = 1;
		gridbag.ipadx = 0;
		optionpanel.add(height_spinner, gridbag);

		JCheckBox equal_check = new JCheckBox("Equal");
		equal_check.setActionCommand("equal");
		equal_check.addActionListener(this);
		equal_check.setFont(checkFont);
		gridbag.insets = checkInset;
		gridbag.gridx = 1;
		gridbag.gridy++;
		optionpanel.add(equal_check, gridbag);

		JLabel lblPathLength = new JLabel("Path Length");
		lblPathLength.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(lblPathLength, gridbag);
		
		JLabel lblPathWind = new JLabel("Path Straightness");
		lblPathWind.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 1;
		optionpanel.add(lblPathWind, gridbag);
		
		path_combo.addActionListener(this);
		path_combo.setActionCommand("path_length");
		path_combo.setFont(comboFont);
		gridbag.insets = comboInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(path_combo, gridbag);

		straight_combo.addActionListener(this);
		straight_combo.setActionCommand("path_straight");
		straight_combo.setFont(comboFont);
		gridbag.insets = comboInset;
		gridbag.gridx = 1;
		optionpanel.add(straight_combo, gridbag);
		
		JLabel lblDeadEnd = new JLabel("Dead Ends");
		lblDeadEnd.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(lblDeadEnd, gridbag);

		JLabel lblDoors = new JLabel("Door Types");
		lblDoors.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 1;
//		gridbag.gridy++;
		optionpanel.add(lblDoors, gridbag);
		
		dead_combo.addActionListener(this);
		dead_combo.setActionCommand("deadends");
		dead_combo.setFont(comboFont);
		gridbag.insets = comboInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(dead_combo, gridbag);

		door_combo.addActionListener(this);
		door_combo.setActionCommand("door_type");
		door_combo.setSelectedIndex(Global.DoorTypes.BASIC.ordinal());
		door_combo.setFont(comboFont);
		gridbag.insets = comboInset;
		gridbag.gridx = 1;
//		gridbag.gridy++;
		optionpanel.add(door_combo, gridbag);

		JLabel lblRoomSize = new JLabel("Room Size");
		lblRoomSize.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 1;
		gridbag.gridy++;
		optionpanel.add(lblRoomSize, gridbag);

		room_combo.addActionListener(this);
		room_combo.setActionCommand("room_size");
		room_combo.setSelectedIndex(Global.RoomSizes.ordinal());
		room_combo.setFont(comboFont);
		gridbag.insets = comboInset;
		gridbag.gridx = 1;
		gridbag.gridy++;
		optionpanel.add(room_combo, gridbag);

		JCheckBox chckbxPadding = new JCheckBox("Padding");
		chckbxPadding.addActionListener(this);
		chckbxPadding.setActionCommand("padding");;
		chckbxPadding.setFont(checkFont);
		gridbag.insets = checkInset;
		gridbag.gridx = 1;
		gridbag.gridy++;
		optionpanel.add(chckbxPadding, gridbag);

		JLabel lblImport = new JLabel("Import");
		lblImport.setFont(defaultFont);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		optionpanel.add(lblImport, gridbag);
		
		txtImport.setEditable(false);
		gridbag.insets = textInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		gridbag.gridwidth = 2;
		optionpanel.add(txtImport, gridbag);
		
		JButton btnChooser = new JButton("...");
		btnChooser.setFont(defaultFont);
		btnChooser.setActionCommand("choose");
		btnChooser.addActionListener(this);
		gridbag.insets = buttonInset;
		gridbag.gridx = 0;
		gridbag.gridy++;
		gridbag.gridwidth = 1;
		optionpanel.add(btnChooser, gridbag);
		
		JButton btnImport = new JButton("Import");
		btnImport.setFont(defaultFont);
		gridbag.insets = buttonInset;
		gridbag.gridx = 1;
//		gridbag.gridy++;
//		gridbag.gridwidth = 1;
		optionpanel.add(btnImport, gridbag);
		
		JCheckBox chckEdit = new JCheckBox("Editable");
		chckEdit.setActionCommand("edit");
		chckEdit.addActionListener(this);
		chckEdit.setFont(checkFont);
		gridbag.insets = checkInset;
		gridbag.gridx = 1;
		gridbag.gridy++;
		optionpanel.add(chckEdit, gridbag);
		
		return optionpanel;
	}
	
	private JPanel createButtonPanel() {
		JPanel bpanel = new JPanel();
		bpanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gridbag = new GridBagConstraints();
		gridbag.anchor = GridBagConstraints.NORTHWEST;
		gridbag.weightx = 1;
		gridbag.weighty = 1;
		gridbag.fill = GridBagConstraints.HORIZONTAL;
		gridbag.insets = buttonInset;
		gridbag.gridx = 0;
		gridbag.gridy = 0;
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setActionCommand("refresh");
		btnRefresh.addActionListener(this);
		btnRefresh.setFont(defaultFont);
		bpanel.add(btnRefresh, gridbag);
		gridbag.gridy++;

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setActionCommand("generate");
		btnGenerate.addActionListener(this);
		btnGenerate.setFont(defaultFont);
		bpanel.add(btnGenerate, gridbag);

		JButton btnClear = new JButton("Clear");
		btnClear.setActionCommand("clear");
		btnClear.addActionListener(this);
		btnClear.setFont(defaultFont);
		gridbag.gridx = 1;
		bpanel.add(btnClear, gridbag);
		
		return bpanel;
	}
	
	public void updateSettings() {
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, getHeight());
	}
	
	public class GenerateRunner implements Runnable {
		
		public void run() {
			System.out.println("Begin Generating");
			map.generateMap();
			System.out.println("End Generating\n");
		}
	}
}
