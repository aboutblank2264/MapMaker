import java.util.List;

import javax.swing.SwingWorker;

public class MapGeneratorWorker {
	private static MapLayoutPanel panel;
	private static SwingWorker<MapLayout, MapLayout> swingWorker;
	
	public static SwingWorker<MapLayout, MapLayout> getWorker() {
		return swingWorker;
	}
	
	public static void generate(MapLayoutPanel p, final boolean isStepping) {
		panel = p;
		
		swingWorker = new SwingWorker<MapLayout, MapLayout>() {

			MapLayout layout = new MapLayout(Global.MapWidth, Global.MapHeight);
			
			@Override
			protected MapLayout doInBackground() throws Exception {
				
				System.out.println("Making Rooms");
				List<Room> rooms = layout.generateRooms();
				System.out.println("Finished making rooms");
				publish(layout);
				
				if(isStepping) {
					wait();
				}
				
				System.out.println("Making Paths");
				List<Path> paths = layout.generatePaths();
				System.out.println("Finished making paths");
				publish(layout);
				
				if(isStepping) {
					wait();
				}
				
				System.out.println("Making Doors");
//				HashMap<Room, List<Grid>> doors = 
				layout.generateDoors(rooms);
				System.out.println("Finished making doors");
				publish(layout);

				if(isStepping) {
					wait();
				}
				
				System.out.println("Pruning Paths");
				layout.prunePaths(paths);
				System.out.println("Finished pruning paths");
				
				return layout;
			}
			
		     @Override
		     protected void process(List<MapLayout> layouts) {
		    	 panel.setMapLayout(layouts.get(layouts.size() - 1));
		     }
		     
		     @Override
		     protected void done() {
		    	 panel.setMapLayout(layout);
		    	 panel.repaint();
		    	 swingWorker = null;
		     }
			
		};
		
		swingWorker.execute();
	}
	
	public static void generate(MapLayoutPanel panel) {
		generate(panel, false);
	}
}
