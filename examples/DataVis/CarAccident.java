import java.io.*;
import java.util.*;

public class CarAccident {
	
	public double Latitude;
	public double Longitude;
	
	public CarAccident(String[] elements) {
		Latitude = Double.parseDouble(elements[15]);
		Longitude = Double.parseDouble(elements[16]);
	}
	
	public static ArrayList<CarAccident> ParseCsv() {
		
		ArrayList<CarAccident> carAccidents = new ArrayList<CarAccident>();
		String[] data = new String[29];
		
		try {
			File file = new File("CarCrashes2007.csv");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = reader.readLine(); // Skip the first one with title info
			int col = 0;
			
			while ((line = reader.readLine()) != null) {
				
				StringTokenizer st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					data[col] = st.nextToken();
					col++;
				}
				carAccidents.add(new CarAccident(data));
				col = 0;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return carAccidents;
	}
	
}
