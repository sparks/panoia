import panoia.*;
import processing.core.*;

public class PanoramaTest extends PApplet {

	Pano pano;
	float angleOffset;
	PanoLink[] links;

	float fov = 270;

	public void setup() {
		size(displayWidth, (int)(displayWidth/(6.5f*360/360)));

		background(0);
		
		pano = new Pano(this);
		pano.setPosition(new LatLng(45.537734f, -73.622578f));

		links = pano.getLinks();
	}

	public void draw() {
		background(0);

		pushMatrix();
		translate(width/2, 0);
		pano.drawTiles(-angleOffset, 360, displayWidth);
		stroke(255);
		line(-width/2, 0, -width/2, height);
		line(width/2, 0, width/2, height);
		popMatrix();

		angleOffset = map(mouseX, 0, width, -180, 180);

		stroke(255);
		for(int i = 0;i < links.length;i++) {
			float heading = links[i].heading;
			if(heading > 180) heading -= 360;
			int xpos = (int)((3.25f+(heading+angleOffset)/360*6.5f)*displayWidth/6.5f);
			line(xpos, 0, xpos, height);
		}

		stroke(255, 0, 0);
		line(width/2, 0, width/2, height);
	}

	public void keyPressed() {
		if(key == 'n') {
			PanoLink closest = links[0];
			int thead = (int)(-angleOffset+360)%360;
			for(int i = 0;i < links.length;i++) {
				if(abs(closest.heading-thead) > abs(links[i].heading-thead)) {
					closest = links[i];
				}
			}

			pano.setPano(closest.pano);
			links = pano.getLinks();
		}
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "PanoramaTest" });
	}
}