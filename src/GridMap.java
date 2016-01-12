import java.util.ArrayList;

public class GridMap {
	private ArrayList<ArrayList<Grid>> internal;

	public GridMap(int height, int width) {
		internal = new ArrayList<>(height);
		for(int i = 0; i < height; i++){
			ArrayList<Grid> t = new ArrayList<Grid>(width);
			for (int j = 0; j < width; j++){
				t.add(new Grid(i, j));
			}
			internal.add(t);
		}
	}
	public GridMap(ArrayList<ArrayList<Grid>> map) {
		internal = map;
	}
	
	public Grid get(Grid g) {
		return internal.get(g.x).get(g.y);
	}
	
	public Grid get(int x, int y) {
		return internal.get(y).get(x);
	}
	
	public int getHeight() {
		return internal.size();
	}
	
	public int getWidth() {
		if(internal.size() > 0) {
			return internal.get(0).size();
		} else {
			return 0;
		}
	}

	//TODO insert front or back
	public void updateWidth(int w) {
		int width = getWidth();
		int diff = w - width;
		
		if(w != getWidth()) {
			for(ArrayList<Grid> g : internal) {
				if(diff > 0) {
					for(int i = width; i < w; i++) {
						g.add(new Grid(i,internal.indexOf(g)));
					}
				} else {
					while(g.size() > w) {
						g.remove(g.size() - 1);
					}
				}
			}
		}
	}
	
	public void updateHeight(int h) {
		int height = getHeight();
		int diff = h - height;
		if(diff > 0) {
			for(int i = height; i < h; i++){
				ArrayList<Grid> t = new ArrayList<Grid>(getWidth());
				for (int j = 0; j < getWidth(); j++){
					t.add(new Grid(j, i));
				}
				internal.add(t);
			}
		} else {
			while(internal.size() > h) {
				internal.remove(internal.size() - 1);
			}
		}
	}
	
	public void clear() {
		for(int i = 0; i < getWidth(); i++){
			for (int j = 0; j < getHeight(); j++){
				Grid g = internal.get(j).get(i);
				g.active = false;
				g.type = Grid.Type.CLOSE;
			}
		}
	}
	
	public double percentFilled() {
		double all = 0.0;
		double active = 0.0;
		for(ArrayList<Grid> arr : internal) {
			for(Grid g : arr) {
				all++;
				if(g.active) active++;
			}
		}
		return active/all * 100;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(o == null || o.getClass() != getClass()) {
			return false;
		}
		
		GridMap m = (GridMap)o;
		if(m.getWidth() == getWidth() && m.getHeight() == getHeight()) {
			for(int x = 0; x < getWidth(); x++) {
				for(int y = 0; y < getHeight(); y++) {
					if(!m.get(x, y).equals(get(x, y))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(ArrayList<Grid> li : internal) {
			str.append(li.toString());
			str.append("\n");
		}
		return str.toString();
	}
}
