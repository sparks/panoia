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

	// LatLng start = new LatLng(45.537734f,-73.622578f);
	// LatLng stop = new LatLng(45.52015f,-73.583357f);

	// LatLng start = new LatLng(45.509415f,-73.567801f);
	LatLng start = new LatLng(45.506903f,-73.570139f);
	LatLng stop = new LatLng(45.506257f,-73.575718f);

	int current_step;
	LatLng[] steps;

	int cachedex = 0;
	int cachelim = 0;
	PImage[][][] cachecache;
	PanoData[] datacache;
	PanoPov[] povcache;

	int runer = -1;

	boolean anim;
	int anim_runer = -1;

	int fm = 10;

	public void setup() {
		size(displayWidth, (int)(displayWidth/6.5f));

		background(0);
		smooth();

		current_step = 0;
		steps = buildRoute(getXML(start, stop));

		int cache_size = 60;

		cachecache = new PImage[cache_size][7][3];
		datacache = new PanoData[cache_size];
		povcache = new PanoPov[cache_size];
		
		pano = new Pano(this, "AIzaSyDklHrdigHKgVYzrDAvSXaaR6Epx1_cygQ");
		pano.setPosition(start);

		float bearing = (float)pano.getPosition().getInitialBearing(steps[(current_step+1)%steps.length]);
		pano.setPov(new PanoPov(0, bearing, 0));

		// step();

		frameRate(fm);
	}

	public void draw() {
		background(0);

		pushMatrix();
		translate(width/2, 0);
		// pano.drawThreeFold(width);
		pano.drawTiles(fov, width);
		popMatrix();

		if(anim) {
			anim_runer++;
			if(anim_runer >= cachecache.length || anim_runer >= cachelim) {
				anim = false;
			} else {
				pano.tileCache = cachecache[anim_runer];
				pano.data = datacache[anim_runer];
				pano.pov = povcache[anim_runer];
			}
		}
	}

	public void mousePressed() {
		// step();
	}

	public boolean step() {
		if(current_step+1 == steps.length) {
			println("done");
			// current_step = 0;
			// pano.setPosition(steps[0]);
			return false;
		} else {
			float dist = (float)pano.getPosition().getDistance(steps[(current_step+1)%steps.length]);

			if(dist < 20) {
				current_step = (current_step+1)%steps.length;
				println("Now Step"+ current_step);

				pano.setPosition(steps[current_step]);
			} else {
				pano.jump();
			}

			float bearing = (float)pano.getPosition().getInitialBearing(steps[(current_step+1)%steps.length]);
			pano.setPov(new PanoPov(0, bearing, 0));

			cachecache[cachedex] = pano.tileCache;
			datacache[cachedex] = pano.data;
			povcache[cachedex] = pano.pov;

			cachedex++;
			cachelim++;
			if(cachelim >= cachecache.length) cachelim = cachecache.length;
			if(cachedex >= cachecache.length) cachedex = 0;

			return true;
		}
	}

	public void keyPressed() {
		if(key == 'r') {
			runer++;
			if(runer >= cachecache.length || runer >= cachelim) runer = 0;

			pano.tileCache = cachecache[runer];
			pano.data = datacache[runer];
			pano.pov = povcache[runer];
		} else if(key == 'g') {
			cachedex = 0;

			(new Thread(new Runnable() {
				public void run() {
					for(int i = 0;i < cachecache.length;i++) {
						println(i);
						step();
						draw();
						try {
							Thread.sleep(100);
						} catch(Exception e) {

						}
					}
					println("Cache Done");
				}
			})).start();

		} else if(key == 's') {
			step();
		} else if( key == 'a') {
			anim_runer = 0;
			anim = true;
		} else if(key == '-') {
			fm--;
			if(fm < 1) fm = 1;
			frameRate(fm);
			println(fm);
		} else if(key == '=') {
			fm++;
			frameRate(fm);
			println(fm);
		}
	}

	public Document getXML(LatLng start, LatLng stop) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			String urlString = "http://maps.googleapis.com/maps/api/directions/xml?origin="+start.toUrlValue()+"&destination="+stop.toUrlValue()+"&sensor=false&units=metric&mode=driving";
			if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;

			println(urlString);

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

			LatLng[] steps = new LatLng[(latTags.getLength()-4)]; //Super hack

			for(int i = 0;i < steps.length;i++) {
				float lat = Float.parseFloat(latTags.item(i).getFirstChild().getNodeValue());
				float lng = Float.parseFloat(lngTags.item(i).getFirstChild().getNodeValue());

				steps[i] = new LatLng(lat, lng);
			}

			println(steps);
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