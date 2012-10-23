package panoia;

public class Pano {

	PanoProvider provider;

	PanoData data;
	PanoPov pov;

	public Pano() {
		this(new GooglePanoProvider());
	}

	public Pano(PanoProvider provider) {
		this.provider = provider;
		pov = new PanoPov();
	}

	public PanoLink[] getLinks() {
		return data.links;
	}

	public String getPano() {
		return data.location.pano;
	}

	public LatLng getPosition() {
		return data.location.latLng;
	}

	public PanoPov getPov() {
		return pov;
	}

	public void setPano(String pano) {
		data = provider.getPanoData(pano);
	}

	public void setPosition(LatLng latLng) {
		data = provider.getPanoData(latLng);
	}

	public void setPov(PanoPov pov) {
		this.pov = pov;
		//Adjust field of view??
	}

	//Draw methods ???

}