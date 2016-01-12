import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class MapImportExport {

	public static GridMap importMap(String filename) {
		return importMap(new File(filename));
	}

	public static GridMap importMap(File file) {
		if(file.exists()) {
			ArrayList<ArrayList<Grid>> map = new ArrayList<>();
			int currentRow = 0;

			try (BufferedReader reader = 
					Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {

				String line = null;
				while((line = reader.readLine()) != null) {
//					System.out.println(line.length());
					ArrayList<Grid> col = new ArrayList<>();
					for(int r = 0; r < line.length(); r++) {
						char c = line.charAt(r);
						Grid currGrid = new Grid(currentRow, c);
						if((int)c != 9) {
							currGrid.active = true;
							currGrid.type = Grid.Type.OPEN;
							if(c == 'D') {
								while((int)line.charAt(r) != 9) {
									r++;
								}
							}
						}
						col.add(currGrid);
					}
					System.out.println(col.size());
					map.add(col);
					currentRow++;
				}
				return new GridMap(map);
			} catch (IOException e) {

			}
		}
		return null;
	}
}
