import static org.junit.Assert.*;

import org.junit.Test;

public class MapTests {

	@Test
	public void testMap() {
		GridMap m = new GridMap(2, 2);
		m.updateHeight(4);
		assertTrue(m.equals(new GridMap(2,4)));
		assertTrue(m.getHeight() == 4);
		m.updateHeight(2);
		assertTrue(m.equals(new GridMap(2,2)));

		m.updateWidth(4);
		assertTrue(m.getWidth() == 4);
		assertTrue(m.equals(new GridMap(4,2)));
		
		m.updateWidth(2);
		assertTrue(m.getWidth() == 2);
		assertTrue(m.equals(new GridMap(2,2)));
	}
	
	@Test
	public void testGrid() {
		Grid g1 = new Grid(0, 0);
		g1.active = true;
		g1.type = Grid.Type.CLOSE;
		
		Grid g2 = new Grid(0,0);
		assertFalse(g1.equals(g2));
		g2.active = true;
		g2.type = Grid.Type.CLOSE;
		assertTrue(g1.equals(g2));
	}
	
	@Test
	public void testPaths() {
		MapLayout map = new MapLayout(10, 10);
		Path p = new Path();
		assertFalse("0, 0 is not valid: ", p.isOpenPath(new Grid(0,0), Global.DIRECTION.NONE, map));
		assertTrue("1, 1 is valid: " , p.isOpenPath(new Grid(1,1), Global.DIRECTION.NONE, map));
		map.activateGrid(new Grid(2,3));
		assertFalse("2, 3 is not valid: ", p.isOpenPath(new Grid(2,2), Global.DIRECTION.EAST, map));
		}

}