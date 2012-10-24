import panoia.*;
import java.io.*;
import java.util.*;
import processing.core.*;

public class CarAccident {
	
	public LatLng latLng;
	
	private PApplet parent;
	
	public CarAccident(PApplet parent, LatLng latLng) {
		this.parent = parent;
		this.latLng = latLng;
	}
	
	public static ArrayList<CarAccident> ParseCsv(PApplet parent) {
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
				try {
					float lat = Float.parseFloat(data[15]);
					float lng = Float.parseFloat(data[16]);

					LatLng latLng = new LatLng(lat, lng);

					carAccidents.add(new CarAccident(parent, latLng));
				} catch(Exception e) {
					// System.err.println(data[15]+" - "+data[16]);
					// System.err.println("Error parsing CSV for car accidents");
					// System.err.println(e);
				}

				col = 0;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return carAccidents;
	}
	
}
