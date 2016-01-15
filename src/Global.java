import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class Global {

	public final static int MINIMUM_MAP_SIZE = 31;
	public final static int MAXIMUM_MAP_SIZE = 151;
	public final static int DEFAULT_MAP_W = 45;
	public final static int DEFAULT_MAP_H = 41;
	public final static int DEFAULT_WINDOW_W = 885;
	public final static int DEFAULT_WINDOW_H = 620;
	public final static int DEFAULT_OPTION_W = 175;
	public final static double DEFAULT_GRID_RECT_SIZE = 15;
	
	public final static int MINIMUM_PATH_CREATE_LENGTH = 15;
	
	public static int MapWidth = DEFAULT_MAP_W;
	public static int MapHeight = DEFAULT_MAP_H;
	
	public static boolean RoomUsePadding = true;
	
	public static int PathMinLength = PathLength.SHORT.length;
	
	public static RoomSize RoomSizes = RoomSize.LARGE;
	public static int RoomTries = 10;
	public static int RoomPadding = 3;
	public static int RoomAttempts = 2000;
	public static int RoomMaxNumPlace = 2;
	
	public static int RoomMaxDoors = 2;
	public static int RoomDoorDistance = 10;
	
	public static int RoomDoorPercent = 5;
	public static int PathDeadEndPercent = DeadEndPercent.NONE.percent;
	public static int PathSameDirectionPercent = PathStraightness.STRAIGHT.percent;
	
	private static Random rand = new Random();
	
	public static int generateRandomInt(int max, int min) {
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public static int generateRandomOddInt(int max, int min) {
		if (max % 2 == 0) --max;
		if (min % 2 == 0) ++min;
		return min + 2*(int)(Math.random()*((max-min)/2+1));
	}
	
	public static boolean generateChance(int percent) {
		int r = generateRandomInt(100, 0);
		return percent > r;
	}
	
	public static <T> T getRandomFromList(List<T> list) {
		int t = generateRandomInt(list.size() - 1, 0);
		return list.get(t);
	}
	
	public static <T> T popRandomFromList(List<T> list) {
		int i = generateRandomInt(list.size() - 1, 0);
		T t = list.get(i);
		list.remove(i);
		return t;
	}
	
	public enum DIRECTION {
		NORTH(new Grid(0, -1)),
		EAST(new Grid(1, 0)),
		SOUTH(new Grid(0, 1)),
		WEST(new Grid(-1, 0)),
		NONE(new Grid(0,0));
		
		public final Grid rect;
		DIRECTION(Grid d) {
			rect = d;
		}
		
		public static DIRECTION randomDirection() {
			return values()[new Random().nextInt((3) + 1)];
		}
		
		public static DIRECTION reverse(DIRECTION d) {
			if(d == NORTH) return SOUTH;
			if(d == SOUTH) return NORTH;
			if(d == EAST) return WEST;
			if(d == WEST) return EAST;
			return NONE;
		}
		
		public static DIRECTION[] getValues() {
			return Arrays.copyOfRange(values(), 0, 4);
		}
	}
	
	public enum RoomSize {
		SMALL(new Grid(5,7)),
		MEDIUM(new Grid(7,9)),
		LARGE(new Grid(9,12)),
		NONE(new Grid(0,0));
		
		public final Grid size;
		public final int min, max;
		RoomSize(Grid r) {
			size = r;
			min = r.x;
			max = r.y;
		}
		
		public static RoomSize get(int index) {
			return getValues().get(index);
		}
		
		public RoomSize getSmaller() {
			int r = getValues().indexOf(this);
			if(r > 0) return RoomSize.get(r - 1);
			else return RoomSize.get(0);
		}
		
		public static List<RoomSize> getValues() {
			return Arrays.asList(Arrays.copyOfRange(values(), 0, values().length - 1));
		}
	}
	
	public enum PathLength {
		SHORT(2),
		MEDIUM(4),
		LONG(6);
		
		public final int length;
		PathLength(int l) {
			length = l;
		}
	}
	
	public enum PathStraightness {
		STRAIGHT(80),
		WINDY(50),
		MAZE(0);
		
		public final int percent;
		private PathStraightness(int p) {
			percent = p;
		}
	}
	
	public enum DeadEndPercent {
		NONE(0),
		MINIMAL(20),
		SOME(50),
		MANY(80);
		
		public final int percent;
		DeadEndPercent(int p) {
			percent = p;
		}
	}
	
	public enum DoorTypes {
		NONE,
		BASIC,
		SECURE,
		TRAPPED;
	}
}
