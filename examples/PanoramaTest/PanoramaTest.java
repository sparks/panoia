import panoia.*;
import processing.core.*;

public class PanoramaTest extends PApplet {

	Pano pano;
	float[] yaws;

	public void setup() {
		size(displayWidth,displayWidth/7*3);

		background(0);
		noLoop();
		
		pano = new Pano();
		pano.setPosition(new LatLng(45.537734f, -73.622578f));
	}

	public void draw() {
		background(0);
		translate(width/2, height/2);
		scale(2.5f);
		
		PanoTileData tiles = pano.getPanoData().tiles;
		
		PImage[] pics = new PImage[7];
		
		for(int i = 0;i < 7;i++) pics[i] = loadImage(tiles.getTileUrl(3,i,1), "jpg");

		int start_tile = floor((6.5f-tiles.centerHeading/360*6.5f)%6.5f);
		float offset = ((6.5f-tiles.centerHeading/360*6.5f)%6.5f-start_tile)*width/6.5f;
		
		for(int i = start_tile;i < 7;i++) {
			if(pics[i] != null) image(pics[i], width/6.5f*(i-start_tile)-width/2-offset, -width/13, width/6.5f, width/6.5f);
		}
		
		for(int i = 0;i < start_tile+1;i++) {
			if(pics[i] != null) image(pics[i], width/6.5f*(i+7-start_tile)-width/2-width/13-offset, -width/13, width/6.5f, width/6.5f);
		}
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "PanoramaTest" });
	}

}