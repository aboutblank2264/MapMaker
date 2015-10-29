import java.util.ArrayList;
import java.util.List;

public class Path {
	private boolean TEST_TIME = false;
	private long startTime;
	private long endTime;
	
	private MapLayout map;
	public TreeNode<Grid> path;
	private List<TreeNode<Grid>> deadEnds = new ArrayList<>();
	public int length = 0;
	
	public Path(MapLayout m) {
		map = m;
	}
	
	public void createPath(int x, int y) {
		if(TEST_TIME) {
			startTime = System.nanoTime();
		}
		
		Grid cur = map.getGrid(y, x);
		if(cur == null || cur.active || !isOpenPath(cur, Global.DIRECTION.NONE) ||
				path != null) return;

		path = new TreeNode<Grid>(cur);
		map.activateGrid(cur);
		length = 1;
		deadEnds.add(path);

		TreeNode<Grid> tpath = path;
		List<Global.DIRECTION> d = findOpenDirections(tpath.data);
		Global.DIRECTION direction = Global.DIRECTION.EAST;
		
		if(!d.isEmpty()) direction = Global.getRandomFromList(d);
		
		List<TreeNode<Grid>> openPaths = new ArrayList<>();
		int step_counter = 0;
		while(true) {
			step_counter++;
			Grid r1 = map.getGrid(tpath.data.add(direction.rect));
			Grid r2 = map.getGrid(r1.add(direction.rect));
			if(isOpenPath(r1, direction) && isOpenPath(r2, direction)
					&&
				//don't need to check r1?
					step_counter % Global.PathMinLength > 0) {
				tpath = tpath.addChild(r1).addChild(r2);
				map.activateGrid(r1);
				map.activateGrid(r2);
				length += 2;
			} else {
				// TODO: same direction more likely
				ArrayList<Global.DIRECTION> o = findOpenDirections(tpath.data);
				if(!o.isEmpty()) {
					direction = Global.popRandomFromList(o);
					if(!o.isEmpty()) {
						openPaths.add(tpath);
					}
				} else {
					if(!deadEnds.contains(tpath)) {
//						System.out.println("Adding deadend: " + tpath.data);
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
		if(TEST_TIME) {
			endTime = (System.nanoTime() - startTime)/1000000;
			if(endTime > 0) System.out.println("Create Path: " + endTime);
		}
//		System.out.println(deadEnds.size());
	}

	//checks is the given Rectangle is clear for path
	public boolean isOpenPath(Grid cur, Global.DIRECTION direction) {
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

	public ArrayList<Global.DIRECTION> findOpenDirections(Grid cur) {
		ArrayList<Global.DIRECTION> open = new ArrayList<>();
		if(cur != null) {
			for(Global.DIRECTION dir : Global.DIRECTION.getValues()) {
				if(isOpenPath(cur.add(dir.rect), dir)) {
					open.add(dir);
				}
			}
		}
//		System.out.println(cur + ", " + open);
		return open;
	}

	/**
	 * when to prune:
	 * has no children
	 * does not have a door
	 * within prune-[variable] strictness
	 */
	public void prunePath() {
		for(TreeNode<Grid> node : deadEnds) {
			if(node.parent != null && !node.children.isEmpty()) {
//				System.out.println(node.data);
				continue;
			}
			
			TreeNode<Grid> cnode = node;

			head:
			while(cnode.isRoot()) {
				if(!cnode.data.isNextToDoor(map) && cnode.children.size() <= 1) {
					map.deactivateGrid(cnode.data);
//					map.setGrid(cnode.data, Grid.Type.TESTER);
					TreeNode<Grid> t = cnode.children.get(0);
					cnode.remove();
					cnode = t;
				} else {
					break head;
				}
			}
			
			tail:
				while(cnode.isLeaf()) {
					if(!cnode.data.isNextToDoor(map)) {
						map.deactivateGrid(cnode.data);
//					map.setGrid(cnode.data, Grid.Type.TESTER);
						if(!(cnode.isRoot())) {
							TreeNode<Grid> t = cnode.parent;
							cnode.remove();
							cnode = t;
						}
					} else {
						break tail;
					}
				}
		}
	}
	
	public TreeNode<Grid> getNode(Grid g) {
		return path.findTreeNode(g);
	}
}