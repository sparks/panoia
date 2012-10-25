package panoia;

import processing.core.*;

public class Pano {

	PApplet p;

	PanoProvider provider;

	PanoData data;
	PanoPov pov;

	PImage[][] tileCache;
	PImage[] threeFoldCache;

	public Pano(PApplet p) {
		this.p = p;
		this.provider = new PanoProvider();
		pov = new PanoPov();

		tileCache = new PImage[7][3];
		threeFoldCache = new PImage[3];
	}

	public PanoLink[] getLinks() {
		return data.links;
	}

	public void jump() {
		PanoLink closest = data.links[0];
		for(int i = 0;i < data.links.length;i++) {
			if(p.abs(closest.heading-pov.heading()) > p.abs(data.links[i].heading-pov.heading())) {
				closest = data.links[i];
			}
		}

		setPano(closest.pano);
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
		for(int i = 0;i < tileCache.length;i++) {
			for(int j = 0;j < tileCache[i].length;j++) {
				tileCache[i][j] = null;
			}
		}
		for(int i = 0;i < 3;i++) {
			threeFoldCache[i] = null;
		}
	}

	public void setPosition(LatLng latLng) {
		data = provider.getPanoData(latLng);
		for(int i = 0;i < tileCache.length;i++) {
			for(int j = 0;j < tileCache[i].length;j++) {
				tileCache[i][j] = null;
			}
		}
		for(int i = 0;i < 3;i++) {
			threeFoldCache[i] = null;
		}
	}

	public void setPov(PanoPov pov) {
		this.pov = pov;
	}

	public PanoData getPanoData() {
		return data;
	}

	public int headingToPixel(float heading, float fov, int imageWidth) {
		float adjHeading = heading-pov.heading();
		while(adjHeading < -180) adjHeading += 360;
		while(adjHeading > 180) adjHeading -= 360;

		return (int)(imageWidth/2+(adjHeading)/fov*imageWidth);
	}

	public void drawThreeFold(int imageWidth) {
		for(int i = 0;i < 3;i++) {
			if(threeFoldCache[i] == null) {
				threeFoldCache[i] = p.loadImage(data.getStaticUrl(640, 480, 90*(i-1)+pov.heading(), 0, 90), "jpg");
			}
			p.image(threeFoldCache[i], imageWidth/3*(i-1)-imageWidth/6, 0, imageWidth/3, imageWidth*480/3/640);
		}
	}

	public void drawTiles(float fov, int imageWidth) {
		int xTiles = 7;
		int yTiles = 3;
		float overlap = 0.5f;
		float wrapTile = 6.5f;
		float carTile = 3.25f;
		int tileSize = (int)(imageWidth/(wrapTile*fov/360));

		float targetHeading = mod(data.centerHeading-pov.heading(), 360);

		float centerTile = mod(carTile-targetHeading/360*wrapTile, wrapTile);
		float povTileLen = fov/360*wrapTile;
		float startTile = mod(centerTile-povTileLen/2, wrapTile);

		float offsetX = (mod(startTile,1)+povTileLen/2)*tileSize;

		int startIndex = p.floor(startTile);	

		for(int i = 0;i <= p.ceil(povTileLen)+1;i++) {
			// for(int j = 0;j < yTiles;j++) {
			for(int j = 1;j < 2;j++) {
				if(tileCache[(i+startIndex)%xTiles][j] == null) {
					tileCache[(i+startIndex)%xTiles][j] = p.loadImage(data.getTileUrl(3,(i+startIndex)%xTiles,j), "jpg");
				}
				if(i+startIndex < xTiles) {
					p.image(tileCache[(i+startIndex)%xTiles][j], tileSize*i-offsetX, 0, tileSize, tileSize);
					// p.image(tileCache[(i+startIndex)%xTiles][j], tileSize*i-offsetX, tileSize*j, tileSize, tileSize);
				} else {
					p.image(tileCache[(i+startIndex)%xTiles][j], tileSize*(i-overlap)-offsetX, 0, tileSize, tileSize);
					// p.image(tileCache[(i+startIndex)%xTiles][j], tileSize*(i-overlap)-offsetX, tileSize*j, tileSize, tileSize);
				}
			}
		}
	}

	public void drawRoads() {
		PanoLink[] links = data.links;
		for(int i = 0;i < links.length;i++) {
			int x = headingToPixel(links[i].heading, data.centerHeading, p.width);
			p.fill(255, 50);
			p.stroke(255, 50);
			float squish = 4.5f;
			p.quad(
				x+p.width/14/squish, p.height/2+30, 
				x-p.width/14/squish, p.height/2+30, 
				x-p.width/14, p.height, 
				x+p.width/14, p.height
			);

		}
	}

	float mod(float v, float max) {
		if(v == max) return 0;
		while(v < 0) v += max;
		while(v > max) v -= max;
		return v;
	}

}