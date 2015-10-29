import static org.junit.Assert.*;

import org.junit.Test;

public class MapTests {

	@Test
	public void testPaths() {
		MapLayout map = new MapLayout(10, 10);
		Path p = new Path(map);
		assertFalse("0, 0 is not valid: ", p.isOpenPath(new Grid(0,0), Global.DIRECTION.NONE));
		assertTrue("1, 1 is valid: " , p.isOpenPath(new Grid(1,1), Global.DIRECTION.NONE));
		map.activateGrid(new Grid(2,3));
		assertFalse("2, 3 is not valid: ", p.isOpenPath(new Grid(2,2), Global.DIRECTION.EAST));
		//assertFalse(p.isOpenPath(new Rectangle(2,4), Gobal.DIRECTION.NORTH));
		
//		map.activateGrid(new Grid(1,1));
//		assertFalse(p.isOpenPath(new Grid(2,2), Global.DIRECTION.NORTH));
		
		Grid g1 = new Grid(0, 0);
		g1.active = true;
		g1.type = Grid.Type.CLOSE;
		
		Grid g2 = new Grid(0,0);
		assertFalse(g1.equals(g2));
		g2.active = true;
		g2.type = Grid.Type.CLOSE;
		assertTrue(g1.equals(g2));
		}

}
