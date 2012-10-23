package panoia;

public class PanoLocation {

	public String pano, description;
	public LatLng latLng;

	public PanoLocation(String pano, LatLng latLng, String description) {
		this.pano = pano;
		this.latLng = latLng;
		this.description = description;
	}

}