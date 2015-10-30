import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class MapLayoutPanel extends JPanel {
	private static final long serialVersionUID = 1L; //make warning go away
	private MapLayout layout;
	private int panelWidth, panelHeight;
	private double gridRectSize = Global.DEFAULT_GRID_RECT_SIZE;
	
	public MapLayoutPanel(int mapX, int mapY) {
		panelWidth = (int)(mapX * gridRectSize);
		panelHeight = (int)(mapY * gridRectSize);
		layout = new MapLayout(mapX, mapY);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addActive(e.getPoint(), true);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {		
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				addActive(e.getPoint(), false);
				repaint();
			}
		});
	}
	
	private void addActive(Point e, boolean turnOff) {
		Grid grid = layout.getGrid(new Grid(
				(int)Math.round(roundToNum(e.getX()- 5)/gridRectSize),
				(int)Math.round(roundToNum(e.getY() - 5)/gridRectSize)));
		if(!editable) {
			layout.printGrid(grid);
			System.out.println();
			return;
		}
		
		layout.setGrid(grid, Grid.Type.OPEN);
		repaint();
	}
	
	private int roundToNum(double val) {
		return (int) ((Math.round(val/gridRectSize)) * gridRectSize);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)(gridRectSize * layout.width),
				(int)(gridRectSize * layout.height));

		// draw active grids
		for(int x = 0; x < layout.width; x++) {
			for(int y = 0; y < layout.height; y++) {
				Grid r = layout.getGrid(x, y); 
				if(r.active) {
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect((int)(r.x * gridRectSize),(int) (r.y * gridRectSize),
							(int)gridRectSize -1, (int)gridRectSize -1);

					if(r.type == Grid.Type.DOOR) g.setColor(Color.YELLOW);
					else if(r.type == Grid.Type.DEADEND) g.setColor(Color.BLACK);
					else if (r.type == Grid.Type.TESTER) g.setColor(Color.GREEN);
					else if (r.type == Grid.Type.ERROR) g.setColor(Color.RED);
					else g.setColor(Color.WHITE);
					g.fillRect((int)(r.x * gridRectSize + 1),(int) (r.y * gridRectSize + 1),
							(int)gridRectSize - 1, (int)gridRectSize - 1);
				}
			}
		}
	}
	
	public void clearMap() {
		layout.clear();
		repaint();
	}

	private boolean editable = false;
	public void canEdit() {
		editable = !editable;
	}
	
	public void usePadding() {
		Global.RoomUsePadding = !Global.RoomUsePadding;
	}
	
	public void generateMap() {
		clearMap();
		layout.generateMap(20, 3, Global.RoomUsePadding);
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(panelWidth, panelHeight);
	}
}