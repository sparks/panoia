package panoia;

import processing.core.*

public class Pano {

	PanoProvider provider;

	PanoData data;
	PanoPov pov;

	public Pano() {
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

	public void draw() {
		// PanoTileData tiles = pano.getData().tiles;
		
		// PImage[] pics = new PImage[7];
		
		// for(int i = 0;i < 7;i++) pics[i] = loadImage(tiles.getTileUrl(3,i,1), "jpg");

		// int start_tile = floor((6.5f-tiles.centerHeading/360*6.5f)%6.5f);
		// float offset = ((6.5f-tiles.centerHeading/360*6.5f)%6.5f-start_tile)*width/6.5f;
		
		// for(int i = start_tile;i < 7;i++) {
		// 	if(pics[i] != null) image(pics[i], width/6.5f*(i-start_tile)-width/2-offset, -width/13, width/6.5f, width/6.5f);
		// }
		
		// for(int i = 0;i < start_tile+1;i++) {
		// 	if(pics[i] != null) image(pics[i], width/6.5f*(i+7-start_tile)-width/2-width/13-offset, -width/13, width/6.5f, width/6.5f);
		// }
	}

}