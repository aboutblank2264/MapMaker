import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MapLayout {
	private GridMap map;
	
	public MapLayout(int x, int y) {
		map = new GridMap(x, y);
	}
	
	public MapLayout(GridMap m) {
		map = m;
	}
	
	public void activateGrid(Grid ... g) {
		for(Grid r : g) {
			setGrid(r, Grid.Type.OPEN);
		}
	}
	
	public void deactivateGrid(Grid r) {
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
	
	public void clear() {
		map.clear();
	}
	
	/**
	 * Change and update Dimensions of the Map
	 */
	
	public void setHeight(int h) {
		map.updateHeight(h);
	}
	
	public void setWidth(int w) {
		map.updateWidth(w);
	}
	
	public int getWidth() {
		return map.getWidth();
	}
	
	public int getHeight() {
		return map.getHeight();
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

	public void generateMap() {
		List<Room> rooms = Room.generateRooms(Global.RoomSizes, this);
		List<Path> paths = generateMaze();
		findDoors(rooms);
		selectDoors(findDoors(rooms));
		prunePaths(paths);
		
		System.out.println("Filled: " + map.percentFilled());
	}

	/**
	 * Generate the Maze
	 */
	public ArrayList<Path> generateMaze() {
		int[] seed = findSeed();
		ArrayList<Path> paths = new ArrayList<>();
		for(int y = seed[1]; y < map.getHeight(); y += 2) {
			for(int x = seed[0]; x < map.getWidth(); x += 2) {
				if(getGrid(y, x) != null && !getGrid(y, x).active) {
					Path p = new Path();
					p.createPath(x, y, this);
					if(p.length > Global.PathMinLength) {
						paths.add(p);
						System.out.println("Created Path: " + p.length);
					} else {
						p.clearPath(this);
					}
				}
			}
		}
		return paths;
	}
	
	private int[] findSeed() {
		int x, y = 1;
		do {
			x = Global.generateRandomOddInt(map.getWidth() / 4, 1);
			y = Global.generateRandomOddInt(map.getHeight() / 4, 1);
		}
		while(map.get(x, y).active);
		
		return new int[]{x, y};
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
		}
		return sel;
	}
	
	private HashMap<Room, List<Grid>> findDoors(List<Room> rooms) {
		HashMap<Room, List<Grid>> doors = new HashMap<>();
		for(Room r : rooms) {
			ArrayList<Grid> rd = new ArrayList<>();
			for(int x = r.x; x < r.x + r.w; x++) {
				try {
				Grid g = map.get(x, r.y - 1);
				Grid g2 = map.get(x, r.y - 2);
				if(!g.active && g2.active){
					rd.add(g);
//					setGrid(g, Grid.Type.TESTER);
				}

				Grid g3 = map.get(x, r.y + r.h);
				Grid g4 = map.get(x, r.y + r.h + 1);
				if(!g3.active && g4.active) rd.add(g3);
				} catch(IndexOutOfBoundsException e) {}
			}

			for(int y = r.y; y < r.y + r.h; y++) {
				try {
				Grid g = map.get(r.x - 1, y);
				Grid g2 = map.get(r.x - 2, y);
				if(!g.active && g2.active) {
					rd.add(g);
//					setGrid(g, Grid.Type.TESTER);
				}

				Grid g3 = map.get(r.x + r.w, y);
				Grid g4 = map.get(r.x + r.w + 1, y);
				if(!g3.active && g4.active) rd.add(g3);
				} catch(IndexOutOfBoundsException e) {}
			}
			doors.put(r, rd);
		}
		return doors;
	}
	
	public void makeDoor(Grid g) {
		activateGrid(g);
		g.type = Grid.Type.DOOR;
	}
	
	public void prunePaths(List<Path> paths) {
		for(Path p : paths) {
			p.prunePath(this);
		}
	}
	
	public void printGrid(Grid g) {
		if(g != null) {
			System.out.println("Grid: " + g);
			System.out.println("Active: " + g.active);
			System.out.println("Type: " + g.type);
		} else {
			System.out.println("Out of Bounds");
		}
	}
	
//	public void printGrid(Grid g) {
//		for(Path p : paths) {
//			TreeNode<Grid> r = p.getNode(g);
//			if(r != null) {
//				printNode(r);
//			}
//		}
//		System.out.println("Grid: " + g);
//		System.out.println("Active: " + g.active);
//		System.out.println("Type: " + g.type);
//	}
	
	/******************************************************/
//	public void printNode(TreeNode<Grid> t) {
//		System.out.println("Grid: " + t.data);
//		System.out.println("Parent: " + t.parent);
//		System.out.println("Children: " + t.children);
//		System.out.println("isLeaf: " + t.isLeaf());
//		System.out.println("NextToDoor: " + t.data.isNextToDoor(this));
//	}
	
	public String toString() {
		return map.toString();
	}
}