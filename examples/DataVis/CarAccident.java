import java.io.*;
import java.util.*;

public class CarAccident {
	
	public Double Latitude;
	public Double Longitude;
	
	public CarAccident(double lat, double lon) {
		Latitude = lat;
		Longitude = lon;
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
					//data[col] = "4.44444444";
					//System.out.println(st.nextToken());
					col++;
				}
				
				double lat = parseDouble(data[15]);
				double lon = parseDouble(data[16]);
				
				carAccidents.add(new CarAccident(lat, lon));
				col = 0;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return carAccidents;
	}
	
	private static Double parseDouble(String number) {
		Double dub;
		
		try {
			dub = Double.parseDouble(number);
		} catch (Exception e) {
			dub = 666.666;
		}
		
		return dub;
	}
	
}
