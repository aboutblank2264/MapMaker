import java.util.ArrayList;

public class GridMap {
	private ArrayList<ArrayList<Grid>> internal;
	public int width;
	public int height;
	
	public GridMap(int height, int width) {
		this.width = width;
		this.height = height;
		internal = new ArrayList<>(width);
		for(int i = 0; i < width; i++){
			ArrayList<Grid> t = new ArrayList<Grid>(height);
			for (int j = 0; j < height; j++){
				t.add(new Grid(i, j));
			}
			internal.add(t);
		}
	}
	
	public Grid get(Grid g) {
		return internal.get(g.x).get(g.y);
	}
	
	public Grid get(int x, int y) {
		return internal.get(y).get(x);
	}
	
//	private void updateWidth() {
//		int diff = width - map.width;
//		for(ArrayList<Grid> m : map) {
//			if(diff > 0) {
//				for(int i = m.size(); i < width; i++) {
//					m.add(0);
//				}
//			} else {
//				for(int i = m.size()-1; i >= width; i--) {
//					m.remove(i);
//				}
//			}
//		}
//	}
//	
//	private void updateHeight() {
//		int diff = height - map.size();
//		if(diff > 0) {
//			for(int i = 0; i < diff; i++) {
//				ArrayList<Integer> n = new ArrayList<Integer>();
//				for(int j = 0; j < height; j++) {
//					n.add(0);
//				}
//				map.add(n);
//			}
//		} else {
//			for(int i = map.size()-1; i >= height; i--) {
//				map.remove(i);
//			}
//		}
//	}
	
	public void clear() {
		for(int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				Grid g = internal.get(i).get(j);
				g.active = false;
				g.type = Grid.Type.CLOSE;
			}
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(ArrayList<Grid> li : internal) {
			str.append(li.toString());
			str.append("\n");
		}
		return str.toString();
	}
}
