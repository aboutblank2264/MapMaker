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
	private double gridRectSize = Global.DEFAULT_GRID_RECT_SIZE;

	private boolean editable = false;
	private boolean isGenerating = false;
	
	public MapLayoutPanel(int mapX, int mapY) {
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
	
	
	public void setMapLayout(GridMap m) {
		layout = new MapLayout(m);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)(gridRectSize * layout.getHeight()),
				(int)(gridRectSize * layout.getWidth()));
		drawGrid(g);
	}
	
	private void drawGrid(Graphics g) {
		for(int y = 0; y < layout.getHeight(); y++) {
			for(int x = 0; x < layout.getWidth(); x++) {
				Grid r = layout.getGrid(x, y); 
				if(r != null && r.active) {
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect((int)(r.x * gridRectSize),(int) (r.y * gridRectSize),
							(int)gridRectSize -1, (int)gridRectSize -1);
					switch(r.type) {
					case DOOR:
						g.setColor(Color.YELLOW);
						break;
					case DEADEND:
						g.setColor(Color.BLUE);
						break;
					case TESTER:
						g.setColor(Color.GREEN);
						break;
					case ERROR:
						g.setColor(Color.RED);
						break;
					default:
						g.setColor(Color.WHITE);
					};
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

	public void canEdit() {
		editable = !editable;
	}
	
	// Don't use generateMap. use MapGeneratorWorker
	public void generateMap() {
		if(!isGenerating) {
			isGenerating = true;
			
			layout = new MapLayout(Global.MapWidth, Global.MapHeight);
			layout.generateMap();
			isGenerating = false;
		}
		repaint();
	}

	public MapLayout getMapLayout() {
		return layout;
	}
	
	public void setMapLayout(MapLayout layout) {
		this.layout = layout;
		getParent().revalidate();
	}
	
	@Override
	public Dimension getPreferredSize() {

		int panelWidth = (int)(Global.MapWidth * gridRectSize);
		int panelHeight = (int)(Global.MapHeight * gridRectSize);
		
		return new Dimension(panelWidth, panelHeight);
	}
}