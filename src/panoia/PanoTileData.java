package panoia;

public class PanoTileData {

	PanoLocation location;
	
	public float centerHeading;
	// public int tileSize;
	// public int worldSize

	public PanoTileData(PanoLocation location, float centerHeading){
		this.location = location;
		this.centerHeading = centerHeading;
	}

	public String getTileUrl(int zoom, int tileX, int tileY) {
		return "http://maps.google.com/cbk?output=tile&panoid="+location.pano+"&zoom="+zoom+"&x="+tileX+"&y="+tileY;
	}

	public PImage getTile(int zoom, int tileX, int tileY) {

	}

}