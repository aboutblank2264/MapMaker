import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Path {
	public TreeNode<Grid> path;
	private List<TreeNode<Grid>> deadEnds;
	public int length = 0;
	
	public void createPath(int x, int y, MapLayout map) {

		Grid cur = map.getGrid(y, x);
		if(cur == null || cur.active || !isOpenPath(cur, Global.DIRECTION.NONE, map) ||
				path != null) return;

		path = new TreeNode<Grid>(cur);
		map.activateGrid(cur);
//		cur.type = Grid.Type.TESTER;
		length = 1;
		if(deadEnds == null) deadEnds = new ArrayList<>();
		deadEnds.add(path);

		TreeNode<Grid> tpath = path;
		List<Global.DIRECTION> d = findOpenDirections(tpath.data, map);
		Global.DIRECTION direction = Global.DIRECTION.EAST;

		if(!d.isEmpty()) direction = Global.getRandomFromList(d);

		List<TreeNode<Grid>> openPaths = new ArrayList<>();
		int step_counter = 1;
		while(true) {
			step_counter++;
			Grid r1 = map.getGrid(tpath.data.add(direction.rect));
			Grid r2 = map.getGrid(r1.add(direction.rect));
			if(isOpenPath(r1, direction, map) && isOpenPath(r2, direction, map)
					&& step_counter % Global.PathMinLength > 0) {
				tpath = tpath.addChild(r1).addChild(r2);
				map.activateGrid(r1, r2);
				length += 2;
			} else {
				ArrayList<Global.DIRECTION> o = findOpenDirections(tpath.data, map);
				if(!o.isEmpty()) {
					if(!(o.contains(direction) && 
							Global.generateChance(Global.PathSameDirectionPercent))) {
						direction = Global.popRandomFromList(o);
					}
					if(!o.isEmpty()) {
						openPaths.add(tpath);
					}
				} else {
					if(!deadEnds.contains(tpath)) {
						deadEnds.add(tpath);
					}
					if(!openPaths.isEmpty() ) {
						tpath = Global.popRandomFromList(openPaths);
					} else {
						break;
					}
				}
			}
		}
	}

	//checks is the given Rectangle is clear for path
	public boolean isOpenPath(Grid cur, Global.DIRECTION direction, MapLayout map) {
		if(cur == null) return false;
		for(Global.DIRECTION dir : Global.DIRECTION.getValues()) {
			if(dir != Global.DIRECTION.reverse(direction)) {
				Grid g = map.getGrid(cur.add(dir.rect));
				if(g == null || g.active) {
					return false;
				}
			}
		}
		return true;
	}

	public ArrayList<Global.DIRECTION> findOpenDirections(Grid cur, MapLayout map) {
		ArrayList<Global.DIRECTION> open = new ArrayList<>();
		if(cur != null) {
			for(Global.DIRECTION dir : Global.DIRECTION.getValues()) {
				if(isOpenPath(cur.add(dir.rect), dir, map)) {
					open.add(dir);
				}
			}
		}
		return open;
	}

	public void prunePath(MapLayout map) {
		for(TreeNode<Grid> node : deadEnds) {
			if(node.parent != null && !node.children.isEmpty() ||
					Global.generateChance(Global.PathDeadEndPercent)) {
				continue;
			}
			
			TreeNode<Grid> cnode = node;
			tail:
			while(cnode.isLeaf()) {
				if(!cnode.data.isNextToDoor(map)) {
					map.deactivateGrid(cnode.data);
					if(!(cnode.isRoot())) {
						TreeNode<Grid> t = cnode.parent;
						cnode.remove();
						cnode = t;
					}
				} else {
					break tail;
				}
			}

			head:
			while(cnode.isRoot()) {
				if(!cnode.data.isNextToDoor(map) && cnode.children.size() <= 1) {
					map.deactivateGrid(cnode.data);
					if(cnode.children.size() != 0) {
						TreeNode<Grid> t = cnode.children.get(0);
						cnode.remove();
						cnode = t;
					} else {
						cnode.remove();
					}
				} else {
					break head;
				}
			}
		}
		deadEnds = null;
	}
	
	public void clearPath(MapLayout map) {
		for(Iterator<TreeNode<Grid>> iter = path.iterator(); iter.hasNext(); ) {
			TreeNode<Grid> g = iter.next();
			map.deactivateGrid(g.data);
		}
		path = null;
		length = 0;
	}

	public TreeNode<Grid> getNode(Grid g) {
		return path.findTreeNode(g);
	}
}