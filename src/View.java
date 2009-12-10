package panoia;

import processing.core.*;
import org.w3c.dom.Document;

public class View {
	
	PApplet parent;
	
	float latitude, longitude;
	
	String[] pano_ids;
		
	boolean tiles_loaded;
	String[][] tile_urls;
	PImage[][] tiles;
	
	public View(PApplet parent, float latitude, float longitude) {
		this.parent = parent;
		
		tiles_loaded = false;
		tiles = new PImage[7][3];
		tile_urls = new String[7][3];
		
		this.latitude = latitude;
		this.longitude = longitude;
		
		pano_ids = Panoia.getPanoIDs(latitude, longitude);
		
		preload();
	}
	
	public View(PApplet parent, String pano_id) {
		this.parent = parent;

		tiles_loaded = false;
		tiles = new PImage[7][3];

		Document xml_file = Panoia.getXML(pano_id);
		
		pano_ids = Panoia.getPanoIDs(xml_file);
		float[] location = Panoia.getLatLng(xml_file);
		
		latitude = location[0];
		longitude = location[1];
		
		preload();
	}
	
	void preload() {
		for(int i = 0;i < 7;i++) {
			for(int j = 0;j < 3;j++) {
				tile_urls[i][j] = "http://maps.google.com/cbk?output=tile&zoom=3&x="+i+"&y="+j+"&panoid="+pano_ids[0];
			}
		}
		
		for(int i = 0;i < 7;i++) {
			for(int j = 0;j < 3;j++) {
				tiles[i][j] = parent.loadImage(tile_urls[i][j],"jpg");
				if(tiles[i][j] == null || tiles[i][j].width == -1) System.err.println("Incapable of loading image pano_id="+pano_ids[0]+" x="+i+" y="+j);
			}
		}
		tiles_loaded = true;
	}
	
	public float getLat() {
		return latitude;
	}
	
	public float getLng() {
		return longitude;
	}
	
	public String getImgURL(int x, int y) {
		return tile_urls[parent.constrain(x, 0, 6)][parent.constrain(y, 0, 2)];
	}
	
	public PImage getPImage(int x, int y) {
		return tiles[parent.constrain(x, 0, 6)][parent.constrain(y, 0, 2)];
	}
		
}