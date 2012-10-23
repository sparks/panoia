import panoia.*;
import java.io.*;
import java.util.*;
import processing.core.*;

public class DataVis extends PApplet {
	boolean debug = true;
	
	PFont font;
	
	ArrayList<CarAccident> carAccidents;
	Double randomLat;

	public void setup() {
		size(displayWidth, displayHeight);
		background(0);

		smooth();
		
		font = createFont("Arial", 16, true);
		
		carAccidents = CarAccident.ParseCsv();
		randomLat = carAccidents.get((int)(carAccidents.size() * Math.random())).Latitude;
	}

	public void draw() {
		background(0);
		translate(width/2, height/2);

		textFont(font, 16);
		fill(255);
		textAlign(CENTER);
		text(randomLat.toString(), 0, 0);
	}

	public void mousePressed() {
		
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "DataVis" });
	}
}
