import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MapLayout {
	private GridMap map;
//	public ArrayList<Grid> active;
	public int height, width;
	
	private List<Path> paths;
	
	public MapLayout(int x, int y) {
		width = x;
		height = y;
		map = new GridMap(width, height);
//		active = new ArrayList<Grid>();
		paths = new ArrayList<Path>();
	}
	
	public void activateGrid(Grid r) {
//		updatePosition(r, false);
		setGrid(r, Grid.Type.OPEN);
	}
	
	public void deactivateGrid(Grid r) {
//		updatePosition(r, true);
		setGrid(r, Grid.Type.CLOSE);
	}
	
	public void setGrid(Grid r, Grid.Type t) {
		try {
			map.get(r).type = t;
			if(t != Grid.Type.CLOSE) map.get(r).active = true;
			else map.get(r).active = false;
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Grid not found: " + r);
		}
	}
	
	/**
	 * Updates the position of the rectangle with the type of grid it will be
	 * @param r
	 * @param turnOff
	 */
//	public void updatePosition(Grid r, boolean turnOff) {
//		try {
//			if(r.active && turnOff) {
//				map.get(r).setActive(false);
//				active.remove(r);
//			} else {
//				map.get(r).setActive(true);
//				active.add(r);
//			}
//		} catch(IndexOutOfBoundsException e) { }
//	}

//	private int hashPosn(Grid r) {
//		return r.y * height + r.x;
//	}
	
	public void clear() {
//		for(Grid r : active) {
//			map.get(r).active = false;
//			map.get(r).type = Grid.Type.CLOSE;
//		}
//		active.clear();
		map.clear();
		paths.clear();
	}
	
	/**
	 * Change and update Dimensions of the Map
	 */
	
	public void setHeight(int h) {
		height = h;
		updateMap();
	}
	
	public void setWidth(int w) {
		width = w;
		updateMap();
	}
	
	private void updateMap() {
//		if(map.width != width) {
//			updateWidth();
//		}
//		if(map.height != height) {
//			updateHeight();
//		}
	}
	
	public Grid getGrid(int x, int y) {
		try {
			return map.get(x, y);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Grid getGrid(Grid g) {
		try {
			return map.get(g);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void generateMap(int attempts, int size, boolean usePadding) {
		List<Room> rooms = Room.generateRooms(size, usePadding, this);
		paths = generateMaze();
		System.out.println();
		selectDoors(findDoors(rooms));
		prunePaths();
	}

	/**
	 * Generate the Maze
	 */
	public ArrayList<Path> generateMaze() {
		ArrayList<Path> paths = new ArrayList<>();
		for(int y = 1; y < height; y += 2) {
			for(int x = 1; x < width; x += 2) {
				if(getGrid(y, x) != null && !getGrid(y, x).active) {
					Path p = new Path(this);
					p.createPath(x, y);
					paths.add(p);
					System.out.println("Created Path: " + p.length);
				}
			}
		}
		return paths;
	}

	private HashMap<Room,List<Grid>> selectDoors(HashMap<Room, List<Grid>> doors) {
		HashMap<Room, List<Grid>> sel = new HashMap<>();
		
		for(Entry<Room, List<Grid>> pair : doors.entrySet()) {
			ArrayList<Grid> picked = new ArrayList<>();
			if(!pair.getValue().isEmpty()) {
				picked.add(Global.popRandomFromList(pair.getValue()));
				makeDoor(picked.get(0));
				
				while(!pair.getValue().isEmpty()) {
					Grid g = Global.popRandomFromList(pair.getValue());
					if(picked.get(0).distance(g) >= Global.RoomDoorDistance &&
							picked.size() < Global.RoomMaxDoors) {
						picked.add(g);
						makeDoor(g);
					}
				}
			}
//			sel.put(pair.getKey(), picked);
//			for(Grid g : pair.getValue()) {
//				makeDoor(g);
//			}
		}
		return sel;
	}
	
	private HashMap<Room, List<Grid>> findDoors(List<Room> rooms) {
		HashMap<Room, List<Grid>> doors = new HashMap<>();
		for(Room r : rooms) {
			ArrayList<Grid> rd = new ArrayList<>();
			for(int x = r.x; x < r.x + r.w; x++) {
				Grid g = map.get(x, r.y - 1);
				Grid g2 = map.get(x, r.y - 2);
				if(!g.active && g2.active) rd.add(g);

				Grid g3 = map.get(x, r.y + r.h);
				Grid g4 = map.get(x, r.y + r.h + 1);
				if(!g3.active && g4.active) rd.add(g3);
			}

			for(int y = r.y; y < r.y + r.h; y++) {
				Grid g = map.get(r.x - 1, y);
				Grid g2 = map.get(r.x - 2, y);
				if(!g.active && g2.active) rd.add(g);

				Grid g3 = map.get(r.x + r.w, y);
				Grid g4 = map.get(r.x + r.w + 1, y);
				if(!g3.active && g4.active) rd.add(g3);
			}
			doors.put(r, rd);
		}
		return doors;
	}
	
	public void makeDoor(Grid g) {
		activateGrid(g);
		g.type = Grid.Type.DOOR;
	}
	
	public void prunePaths() {
		for(Path p : paths) {
			p.prunePath();
		}
	}
	
	public void printGrid(Grid g) {
		for(Path p : paths) {
			TreeNode<Grid> r = p.getNode(g);
			if(r != null) {
				printNode(r);
			}
		}
		System.out.println("Grid: " + g);
		System.out.println("Active: " + g.active);
		System.out.println("Type: " + g.type);
	}
	
	/******************************************************/
	public void printNode(TreeNode<Grid> t) {
		System.out.println("Grid: " + t.data);
		System.out.println("Parent: " + t.parent);
		System.out.println("Children: " + t.children);
		System.out.println("isLeaf: " + t.isLeaf());
		System.out.println("NextToDoor: " + t.data.isNextToDoor(this));
	}
	
	public String toString() {
		return map.toString();
	}
}