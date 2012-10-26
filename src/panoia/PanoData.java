package panoia;

public class PanoData {

	String apikey;

	public PanoLink[] links;

	public PanoLocation location;
	public float centerHeading;
	public String copyright;

	public PanoData(PanoLocation location, PanoLink[] links, float centerHeading, String copyright, String apikey) {
		this.apikey = apikey;

		this.links = links;

		this.location = location;
		this.centerHeading = centerHeading;
		this.copyright = copyright;
	}

	public String getTileUrl(int zoom, int tileX, int tileY) {
		String urlString = "http://maps.google.com/cbk?output=tile&panoid="+location.pano+"&zoom="+zoom+"&x="+tileX+"&y="+tileY;
		if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;
		return urlString;
	}

	public String getStaticUrl(int width, int height, float heading, float pitch, float fov) {
		String urlString = "http://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+location.latLng.toUrlValue()+"&sensor=false&heading="+heading+"&pitch="+pitch+"&fov="+fov;
		if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;
		return urlString;
	}

}