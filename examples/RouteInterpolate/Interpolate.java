import processing.core.*;

import panoia.*;

import java.io.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.io.InputStream;

public class Interpolate extends PApplet {

	String apikey;

	Pano pano;
	float fov = 360;

	LatLng start = new LatLng(45.537734f,-73.622578f);
	LatLng stop = new LatLng(45.52015f,-73.583357f);

	int current_step;
	LatLng[] steps;

	int cachedex = 0;
	PImage[][][] cachecache;
	int runer = -1;

	public void setup() {
		size(displayWidth, (int)(displayWidth/6.5f));

		background(0);
		smooth();
		
		pano = new Pano(this);
		pano.setPosition(start);

		current_step = 0;
		steps = buildRoute(getXML(start, stop));

		cachecache = new PImage[5][7][3];

		float bearing = (float)pano.getPosition().getInitialBearing(steps[current_step+1]);
		pano.setPov(new PanoPov(0, bearing, 0));

		// breakup(50);
		// step();
	}

	public void draw() {
		background(0);

		pushMatrix();
		translate(width/2, 0);
		// pano.drawThreeFold(width);
		pano.drawTiles(fov, width);
		popMatrix();
	}

	public void mousePressed() {
		// step();
	}

	public void breakup(int subdivision) {		
		// float total_dist = 0;
		// for(int i = 1;i < steps.length;i++) total_dist += steps[i].dist(steps[i-1]);

		// LinkedList tmp_steps = new LinkedList();
		// float unit = total_dist/(subdivision-1);
		
		// tmp_steps.add(steps[0]);
		
		// for(int i = 1;i < steps.length;i++) {
		// 	while(((PVector)tmp_steps.getLast()).dist(steps[i]) > unit) {
		// 			PVector unit_step = PVector.sub(steps[i], (PVector)tmp_steps.getLast());
		// 			unit_step.normalize();
		// 			unit_step.mult(unit);
		// 			tmp_steps.add(PVector.add((PVector)tmp_steps.getLast(), unit_step));
		// 	}

		// 	tmp_steps.add(steps[i]);
		// }
		
		// steps = (PVector[])tmp_steps.toArray(steps);		
	}

	public boolean step() {
		if(current_step+1 == steps.length) {
			current_step = 0;
			pano.setPosition(steps[0]);
			return false;
		} else {
			float dist = (float)pano.getPosition().getDistance(steps[current_step+1]);
			float bearing = (float)pano.getPosition().getInitialBearing(steps[current_step+1]);

			if(dist > 10) {
				cachecache[cachedex] = pano.tileCache;
				cachedex++;
				if(cachedex >= cachecache.length) cachedex = 0;

				pano.setPov(new PanoPov(0, bearing, 0));
				pano.jump();
			} else {
				current_step = (current_step+1)%steps.length;
				pano.setPosition(steps[current_step]);
			}
			return true;
		}
	}

	public void keyPressed() {
		if(key == 'r') {
			runer++;
			if(runer >= cachedex) runer = 0;
			pano.tileCache = cachecache[runer];
		} else if(key == 'g') {
			cachedex = 0;
			
			for(int i = 0;i < cachecache.length;i++) {
				println(i);
				step();
				draw();
			}

			println("done");
		} else if(key == 's') {
			step();
		}
	}

	public Document getXML(LatLng start, LatLng stop) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			String urlString = "http://maps.googleapis.com/maps/api/directions/xml?origin="+start.toUrlValue()+"&destination="+stop.toUrlValue()+"&sensor=false&units=metric&mode=walking";
			if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;

			URL url = new URL(urlString);

			InputStream stream = url.openStream();
			Document xml = docBuilder.parse(stream);
			
			return xml;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}

	LatLng[] buildRoute(Document xml) {
		try {
			//Parse Location Information and Copyright
			NodeList latTags = xml.getElementsByTagName("lat");
			NodeList lngTags = xml.getElementsByTagName("lng");

			LatLng[] steps = new LatLng[latTags.getLength()-2];

			for(int i = 0;i < steps.length;i++) {
				float lat = Float.parseFloat(latTags.item(i).getFirstChild().getNodeValue());
				float lng = Float.parseFloat(lngTags.item(i).getFirstChild().getNodeValue());

				steps[i] = new LatLng(lat, lng);
			}

			return steps;
		} catch(Exception e) {
			//Probably no such street view, so ignore
			System.err.println("Panoia: Error no such streetview or malformed streetview...");
			return null;
		}

	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "Interpolate" });
	}
}