package panoia;

import processing.core.*;

public class Pano {

	PApplet p;

	PanoProvider provider;

	public PanoData data;
	public PanoPov pov;

	public PImage[][] tileCache;
	public PImage[] threeFoldCache;

	public String apikey;

	public Pano(PApplet p) {
		this(p, null);
	}

	public Pano(PApplet p, String apikey) {
		this.p = p;
		this.apikey = apikey;

		provider = new PanoProvider(apikey);
		pov = new PanoPov();

		tileCache = new PImage[7][3];
		threeFoldCache = new PImage[3];
	}

	public PanoLink[] getLinks() {
		if(data != null) return data.links;
		else return new PanoLink[0];
	}

	public String getPano() {
		if(data != null) return data.location.pano;
		else return "";
	}

	public LatLng getPosition() {
		if(data != null) return data.location.latLng;
		else return new LatLng(0, 0);
	}

	public PanoPov getPov() {
		if(data != null) return pov;
		else return new PanoPov(0, 0, 0);
	}

	public void setPano(String pano) {
		data = provider.getPanoData(pano);

		tileCache = new PImage[tileCache.length][tileCache[0].length];
		threeFoldCache = new PImage[threeFoldCache.length];
		// for(int i = 0;i < tileCache.length;i++) {
		// 	for(int j = 0;j < tileCache[i].length;j++) {
		// 		tileCache[i][j] = null;
		// 	}
		// }
		// for(int i = 0;i < 3;i++) {
		// 	threeFoldCache[i] = null;
		// }
	}

	public void setPosition(LatLng latLng) {
		data = provider.getPanoData(latLng);
		threeFoldCache = new PImage[threeFoldCache.length];

		clearTileCache();
		clearThreeFoldCache();
	}

	public void setPov(PanoPov pov) {
		this.pov = pov;
		clearThreeFoldCache();
	}

	void clearTileCache() {
		tileCache = new PImage[tileCache.length][tileCache[0].length];
	}

	void clearThreeFoldCache() {
		threeFoldCache = new PImage[threeFoldCache.length];
	}

	public PanoData getPanoData() {
		return data;
	}

	public void jump() {
		if(data == null) return;
		PanoLink closest = data.links[0];
		for(int i = 0;i < data.links.length;i++) {
			if(p.abs(closest.heading-pov.heading()) > p.abs(data.links[i].heading-pov.heading())) {
				closest = data.links[i];
			}
		}

		setPano(closest.pano);
	}

	public int headingToPixel(float heading, float fov, int imageWidth) {
		float adjHeading = heading-pov.heading();
		while(adjHeading < -180) adjHeading += 360;
		while(adjHeading > 180) adjHeading -= 360;

		return (int)(imageWidth/2+(adjHeading)/fov*imageWidth);
	}

	public void drawThreeFold(int imageWidth) {
		if(data == null) return;
		try {
			for(int i = 0;i < 3;i++) {
				if(threeFoldCache[i] == null) {
					threeFoldCache[i] = p.loadImage(data.getStaticUrl(640, 480, 90*(i-1)+pov.heading(), 0, 90), "jpg");
				}
				if(threeFoldCache[i] != null) {
					p.image(threeFoldCache[i], imageWidth/3*(i-1)-imageWidth/6, 0, imageWidth/3, imageWidth*480/3/640);
				}
			}
		} catch(Exception e) {

		}
	}

	public void drawTiles(float fov, int imageWidth) {
		if(data == null) return;
		try {
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
					if(tileCache[(i+startIndex)%xTiles][j] != null) {
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
		} catch(Exception e) {

		}
	}

	float mod(float v, float max) {
		if(v == max) return 0;
		while(v < 0) v += max;
		while(v > max) v -= max;
		return v;
	}

}