import panoia.*;
import processing.core.*;

public class PanoramaTest extends PApplet {

	Pano pano;
	float angleOffset;
	PanoTileData tiles;
	PImage[][] pics;
	public void setup() {
		size(displayWidth, (int)(displayWidth/6.5*3));

		background(0);
		
		pano = new Pano();
		pano.setPosition(new LatLng(45.537734f, -73.622578f));


		tiles = pano.getPanoData().tiles;
		
		pics = new PImage[7][3];
		
		for(int i = 0;i < 7;i++) {
			for(int j = 0;j < 3;j++) {
				pics[i][j] = loadImage(tiles.getTileUrl(3,i,j), "jpg");
			}
		}
	}

	public void draw() {
		background(0);
		pushMatrix();
		translate(width/2, 0);

		float centerHeading = angleOffset;
		float xPov = 180;
		int tileSize = (int)(width/6.5f);

		int xTiles = 7;
		int yTiles = 3;
		float wrapTile = 6.5f;
		float carTile = 3.25f;

		float targetHeading = mod(tiles.centerHeading-centerHeading, 360);

		float centerTile = mod(carTile-targetHeading/360*wrapTile, wrapTile);
		float povTileLen = xPov/360*wrapTile;
		float startTile = mod(centerTile-povTileLen/2, wrapTile);

		float offsetX = (mod(startTile,1)+povTileLen/2)*tileSize;

		int startIndex = floor(startTile);	

		for(int i = 0;i <= ceil(povTileLen);i++) {
			for(int j = 1;j < 2;j++) {
				if(pics[(i+startIndex)%xTiles][j] != null) {
					if(i+startIndex < 7) {
						image(pics[(i+startIndex)%7][j], tileSize*i-offsetX, tileSize*j, tileSize, tileSize);
					} else {
						image(pics[(i+startIndex)%7][j], tileSize*(i-0.5f)-offsetX, tileSize*j, tileSize, tileSize);
					}
				}
			}
		}

		angleOffset = map(mouseX, 0, width, -180, 180);
		popMatrix();
		stroke(255);
		strokeWeight(5);
		line(width/4, 0, width/4, height);
		line(3*width/4, 0, 3*width/4, height);

	}

	float mod(float v, float max) {
		if(v == max) return 0;
		while(v < 0) v += max;
		while(v > max) v -= max;
		return v;
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "PanoramaTest" });
	}

	public void mousePressed() {
		System.out.println(mouseX*6.5f/width);
	}

	public void keyPressed() {
		if(key == 'n') {
			pano.setPano(pano.getLinks()[0].pano);

			tiles = pano.getPanoData().tiles;

			for(int i = 0;i < 7;i++) {
				for(int j = 0;j < 3;j++) {
					pics[i][j] = loadImage(tiles.getTileUrl(3,i,j), "jpg");
				}
			}
		}
	}

}