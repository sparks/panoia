package panoia;

import org.w3c.dom.Document;

public class View {
	
	float latitude, longitude;
	
	String[] pano_ids;
	
	public View(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		
		pano_ids = Panoia.getPanoIDs(latitude, longitude);
	}
	
	public View(String pano_id) {
		Document xml_file = Panoia.getXML(pano_id);
		
		pano_ids = Panoia.getPanoIDs(xml_file);
		float[] loc = Panoia.getLatLng(xml_file);
		
		latitude = loc[0];
		longitude = loc[1];
	}
	
	public float getLat() {
		return latitude;
	}
	
	public float getLng() {
		return longitude;
	}
	
	public String[] getImgURLs() {
		String[] urls = new String[7];
		for(int i = 0;i < 7;i++) {
			urls[i] = "http://maps.google.com/cbk?output=tile&zoom=3&x="+i+"&y=1&panoid="+pano_ids[0];
		}
		return urls;
	}
	
}