package panoia;

public class PanoLink {

	public String pano, description;
	public float heading;

	public PanoLink(String pano, float heading, String description) {
		this.pano = pano;
		this.heading = heading;
		this.description = description;
	}

}