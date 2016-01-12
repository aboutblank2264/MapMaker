import java.util.ArrayList;
import java.util.List;

public class Grid implements Comparable<Grid> {
	public int x, y;
	public boolean active = false;
	
	public Type type = Type.OPEN;
	
	public Grid(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Grid(Grid r) {
		x = r.x;
		y = r.y;
		active = r.active;
		type = r.type;
	}
	
	public Grid add(Grid r) {
		return new Grid(x + r.x, y + r.y);
	}
	
	public boolean isNeighbor(Grid g) {
		return distance(g) <= 1;
	}
	
	public List<Grid> getNeighbors(MapLayout map) {
		ArrayList<Grid> neighbors = new ArrayList<>();
		for(Global.DIRECTION d : Global.DIRECTION.getValues()) {
			Grid g = map.getGrid(this.add(d.rect));
			if(g != null) neighbors.add(g);
		}
		return neighbors;
	}
	
	public boolean isNextToDoor(MapLayout map) {
		for(Grid g : getNeighbors(map)) {
			if(g.type == Type.DOOR) {
				return true;
			}
		}
		return false;
	}
	
	public double distance(Grid g) {
		double dx = Math.pow(x - g.x, 2);
		double dy = Math.pow(y - g.y, 2);
//		System.out.println(this + " " + g + " Distance: " + Math.sqrt(dx + dy));
		return Math.sqrt(dx + dy);
	}
	
	public void clear() {
		active = false;
		type = Type.OPEN;
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(o == null || o.getClass() != getClass()) {
			return false;
		}
		
		Grid r = (Grid)o;
		return r.x == x && r.y == y && r.active == active && r.type == type;
	}

	public enum Type {
		OPEN, CLOSE, DOOR, DEADEND, TESTER, ERROR
		
	}

	@Override
	public int compareTo(Grid o) {
		if (this.equals(o)) return 0;
		return -1;
	}
}
