import processing.core.*;

import panoia.*;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class PanoTest extends PApplet {

	Pano pano;
	PanoPov pov;

	float fov = 360;

	PFont font;

	public void setup() {
		size(displayWidth, (int)(displayWidth/(6.5f*fov/360)));

		background(0);

		pano = new Pano(this);
		pov = pano.getPov();

		pano.setPosition(new LatLng(45.5110809f, -73.5700496f));
		// pano.setPano("3qry8ACTZ8Mw6SQ1UaLNMg");
		// pano.setPosition(new LatLng(45.52059937f, -73.58165741f));

		smooth();
	}

	public void draw() {
		background(0);

		pushMatrix();
		translate(width/2, 0);
		// pano.drawThreeFold(width);
		pano.drawTiles(fov, width);
		popMatrix();

		drawPanoLinks();
		
		//Mouse Ref Lin
		stroke(255, 0, 0);
		line(mouseX, 0, mouseX, height);
	}

	public void mousePressed() {
		pov.setHeading(pov.heading()+map(mouseX, 0, width, -fov/2, fov/2));
		pano.jump();
	}

	public void keyPressed() {
		if(key == 'n') {
			pano.jump();
		}
	}

	public void drawPanoLinks() {
		stroke(255);
		PanoLink[] links = pano.getLinks();
		for(int i = 0;i < links.length;i++) {
			int x = pano.headingToPixel(links[i].heading, fov, width);
			line(x, 0, x, height);
		}
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "PanoTest" });
	}
}