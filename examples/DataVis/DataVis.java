import panoia.*;
import java.io.*;
import java.util.*;
import processing.core.*;

public class DataVis extends PApplet {
	boolean debug = true;
	
	PFont font;
	
	ArrayList<CarAccident> carAccidents;

	public void setup() {
		size(displayWidth, displayHeight);
		background(0);

		smooth();
		
		font = createFont("Arial", 16, true);
		
		carAccidents = CarAccident.ParseCsv();
	}

	public void draw() {
		background(0);
		translate(width/2, height/2);

		textFont(font, 16);
		fill(255);
		textAlign(CENTER);
		text(carAccidents.get(2).Latitude.toString(), 0, 0);
	}

	public void mousePressed() {
		
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "DataVis" });
	}
}
