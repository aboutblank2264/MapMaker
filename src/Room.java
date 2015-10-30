import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room {
	public int x, y;
	public int w, h;
	public RoomSize size;
	
	public enum RoomSize {
		TINY(new Grid(3,5)),
		SMALL(new Grid(5,7)),
		MEDIUM(new Grid(7,9)),
		LARGE(new Grid(9,11)),
		NONE(new Grid(0,0));
		
		public final Grid size;
		RoomSize(Grid r) {
			size = r;
		}
		public static RoomSize get(int index) {
			return getValues()[index];
		}
		
		public static RoomSize[] getValues() {
			return Arrays.copyOfRange(values(), 0, values().length - 1);
		}
	}

	public Room(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		calculateSize();
	}
	
	public Room(int x, int y, int w, int h, RoomSize s) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		size = s;
	}

	public void changeRoom(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private void calculateSize() {
		for(int i = RoomSize.getValues().length - 1; i > 0; i--) {
			Grid s = RoomSize.get(i).size;
			if(w > s.x && w < s.y && h > s.x && h < s.y) {
				size = RoomSize.get(i);
				return;
			}
		}
		size = RoomSize.NONE;
	}

	public static Room createRandomRoom(int x, int y, RoomSize r) {
		return new Room(Global.generateRandomOddInt(x, 0),
				Global.generateRandomOddInt(y, 0),
				Global.generateRandomOddInt(r.size.y, r.size.x),
				Global.generateRandomOddInt(r.size.y, r.size.x), r);
	}

	public static List<Room> generateRooms(int size, boolean usePadding, MapLayout map) {
		ArrayList<Room> placedRooms = new ArrayList<>(); 
		int success = 0;
		int tPadding = usePadding ? Global.RoomPadding : 0;
		int pSize = 0;
		int tAttempts = Global.RoomAttempts;
		while(tAttempts > 0) {
			Room room = createRandomRoom(map.width, map.height, RoomSize.get(size));
			Room paddedRoom = new Room(room.x - tPadding, room.y - tPadding,
					room.w + (2 * tPadding), room.h + (2 * tPadding));

			if (checkForEmptySpace(map, paddedRoom)) {
				placeRoom(map, room);
				success++;
				placedRooms.add(room);
				pSize++;
			}

			tAttempts--;
			if(size > 0 && pSize % Global.RoomMaxNumPlace == 0) {
				size--;
			}
		}
		System.out.println("Successful Rooms: " + success);
		return placedRooms;
	}

	private static void placeRoom(MapLayout map, Room room) {
		for(int i = room.y; i < room.y + room.h; i++) {
			for(int j = room.x; j < room.x + room.w; j++) {
				map.activateGrid(new Grid(i,j));
			}
		}
	}

	private static boolean checkForEmptySpace(MapLayout map, Room room) {
		for(int y = room.y; y < room.y + room.h; y++) {
			for(int x = room.x; x < room.x + room.w; x++) {
				Grid g = map.getGrid(x,y);
				if(g == null || g.active) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if the this Room can be placed on the map with the x,y coordinates
	 * @param map
	 * @return
	 */
	public boolean checkRectangleInBounds(MapLayout map) {
		return (y < map.height && (y + h) < map.height) &&
				(x < map.width && (x + w < map.width));
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(o == null || o.getClass() != getClass()) {
			return false;
		}
		Room r = (Room) o;
		return x == r.x && y == r.y && w == r.w && h == r.h;
	}

	@Override
	public String toString() {
		return "X: " + x + " Y: " + y + " W: " + w + " H: " + h;
	}
}
