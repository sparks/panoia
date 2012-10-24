package panoia;

import processing.core.*

public class Pano {

	PApplet p;

	PanoProvider provider;

	PanoData data;

	public Pano(PApplet p) {
		this.p = p;
		this.provider = new PanoProvider();
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

	public PanoData getPanoData() {
		return data;
	}

	public PImage getStaticView(PanoPov pov, float fov) {

	}

}