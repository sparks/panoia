import panoia.*;
import java.io.*;
import java.util.*;
import processing.core.*;

public class BikeAccident {
	
	public LatLng latLng;
	public String desc;

	private PApplet parent;
	
	public BikeAccident(PApplet parent, LatLng latLng, String desc) {
		this.parent = parent;
		this.latLng = latLng;
	}
	
	public static ArrayList<BikeAccident> ParseCsv(PApplet parent) {
		ArrayList<BikeAccident> bikeAccidents = new ArrayList<BikeAccident>();
		String[] data = new String[29];
		int errorCount = 0;
		try {
			File file = new File("BikeAccidents.csv");
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
					float lat = Float.parseFloat(data[7]);
					float lng = Float.parseFloat(data[6]);

					LatLng latLng = new LatLng(lat, lng);

					String desc = data[2].trim()+" at "+data[3].trim()+" "+data[4].trim()+"/"+data[5].trim();

					bikeAccidents.add(new BikeAccident(parent, latLng, desc));
				} catch(Exception e) {
					// System.err.println(data[15]+" - "+data[16]);
					// System.err.println("Error parsing CSV for car accidents");
					// System.err.println(e);
					errorCount++;
				}

				col = 0;
			}

			System.out.println(errorCount);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bikeAccidents;
	}
	
}
