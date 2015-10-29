import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class MapLayoutPanel extends JPanel {
	private static final long serialVersionUID = 1L; //make warning go away
	private final static double DEFAULT_GRID_RECT_SIZE = 15;
	private MapLayout layout;
	private int panelWidth, panelHeight;
	private double gridRectSize = DEFAULT_GRID_RECT_SIZE;
	
	private double zoom = 1.0;
	
	private AffineTransform scale = new AffineTransform();
	
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

		/*
		 * TODO: get zoom and adding squares to work.
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke("UP"), "ZoomUp");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke("DOWN"), "ZoomDown");
		
		getActionMap().put("ZoomUp", new ZoomAction(true));
		getActionMap().put("ZoomDown", new ZoomAction(false));
		*/
	}
	
	private void addActive(Point e, boolean turnOff) {
		Grid grid = layout.getGrid(new Grid(
				(int)Math.round(roundToNum(e.getX()- 5	)/gridRectSize),
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
		
//		Graphics2D g2 = (Graphics2D) g;
//		g2.setTransform(scale);
		
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
	
	private boolean padding = true;
	public void usePadding() {
		padding = !padding;
	}
	
	public void generateMap() {
		clearMap();	
		//layout.generateRooms(20, 3, padding);
		//layout.generateMaze();
		layout.generateMap(20, 3, padding);
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (panelWidth * zoom), (int) (panelHeight * zoom));
	}
	
	private void printPair(int x, int y) {
		System.out.println(x + ", " + y);
	}
	
	/*
	private class ZoomAction extends AbstractAction {
		private static final long serialVersionUID = -8996046031526940733L;
		boolean direction;
		public ZoomAction(boolean d) {
			direction = d;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(direction) {
				zoom += (zoom < 1.3)? .1 : 0;
			} else {
				zoom -= (zoom > .7) ? .1 : 0;
			}
			zoom = (double)Math.round(zoom * 10d) / 10d;
			gridRectSize = DEFAULT_GRID_RECT_SIZE * zoom;
			scale.setToScale(zoom, zoom);
			repaint();
		}
	}
	*/
}