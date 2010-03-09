package panoia;

import processing.core.*;
import org.w3c.dom.Document;

public class View {
		
	float latitude, longitude;
	
	String[] pano_ids;
		
	String[][] tile_urls;
	
	public View(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;

		tile_urls = new String[7][3];
				
		pano_ids = Panoia.getPanoIDs(latitude, longitude);
	}
	
	public View(String pano_id) {
		tile_urls = new String[7][3];
		
		//Grabbing XML file to minimize remote file loads
		
		Document xml_file = Panoia.getXML(pano_id);
		
		pano_ids = Panoia.getPanoIDs(xml_file);
		float[] location = Panoia.getLatLng(xml_file);

		latitude = location[0];
		longitude = location[1];
	}
	
	public float getLat() {
		return latitude;
	}
	
	public float getLng() {
		return longitude;
	}
	
	public String getImgURL(int x, int y) {
		if(pano_ids != null) return "http://maps.google.com/cbk?output=tile&zoom=3&x="+constrain(x, 0, 6)+"&y="+constrain(y, 0, 2)+"&panoid="+pano_ids[0];
		else return null;
	}
	
	public String[] getPanoIDs() {
		return pano_ids;
	}
	
	int constrain(int val, int min, int max) {
		if(val > max) return max;
		if (val < min) return min;
		else return val;
	}
	
}