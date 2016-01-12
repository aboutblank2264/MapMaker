import java.util.ArrayList;
import java.util.List;

public class Room {
	public int x, y;
	public int w, h;
	public Global.RoomSize size;

	public Room(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		calculateSize();
	}
	
	public Room(int x, int y, int w, int h, Global.RoomSize s) {
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
		for(int i = Global.RoomSize.getValues().size() - 1; i > 0; i--) {
			Grid s = Global.RoomSize.get(i).size;
			if(w > s.x && w < s.y && h > s.x && h < s.y) {
				size = Global.RoomSize.get(i);
				return;
			}
		}
		size = Global.RoomSize.NONE;
	}

	public static Room createRandomRoom(int x, int y, Global.RoomSize r) {
		int h = Global.generateRandomOddInt(r.max, r.min);
		int w = Global.generateRandomOddInt(r.max, r.min);
		return new Room(Global.generateRandomOddInt(x - w, 0),
				Global.generateRandomOddInt(y - h, 0), w, h, r);
	}

	public static List<Room> generateRooms(Global.RoomSize size, MapLayout map) {
		ArrayList<Room> placedRooms = new ArrayList<>(); 
		int success = 0;
		int tPadding = Global.RoomUsePadding ? Global.RoomPadding : 0;
		int pSize = 0;
		int tAttempts = Global.RoomAttempts;
		while(tAttempts > 0) {
			Room room = createRandomRoom(map.getWidth(), map.getHeight(), size);
			Room paddedRoom = new Room(room.x - tPadding, room.y - tPadding,
					room.w + (2 * tPadding), room.h + (2 * tPadding));

			if (checkForEmptySpace(map, paddedRoom)) {
				placeRoom(map, room);
				success++;
				placedRooms.add(room);
				pSize++;
			}

			tAttempts--;
			if(size != Global.RoomSize.getValues().get(0) && 
					pSize % Global.RoomMaxNumPlace == 0) {
				size = size.getSmaller();
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
				if(g == null || (Global.RoomUsePadding && g.active)) {
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
		return (y < map.getHeight() && (y + h) < map.getHeight()) &&
				(x < map.getWidth() && (x + w < map.getWidth()));
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
