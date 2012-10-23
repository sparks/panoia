import panoia.*;
import java.io.*;
import java.util.*;
import processing.core.*;

public class Interpolate extends PApplet {
	boolean debug = true;

	int current_step;
	float zoom_index;
	int blur_index;

	PVector start = new PVector(45.537734f, -73.622578f);
	PVector finish =  new PVector(45.52015f,-73.583357f);

	PVector[] steps;

	View view;
	PImage[] pics = new PImage[7];
	float yaw = 0;

	public void setup() {
		size(displayWidth, displayHeight);
		// size(screen.height,screen.height);
		background(0);

		smooth();
		
		current_step = -1;
		zoom_index = 1;
		blur_index = 0;
		
		String route_raw = streamToString(createInput("http://maps.google.com/maps/nav?q=from%2045.537734,-73.622578%20to%2045.52015,-73.583357&dirflg=w"));
		
		LinkedList tmp_steps = new LinkedList();

		int index = route_raw.indexOf("coordinates");
		for(int i = 0;index != -1;i++) {
			if(i >= 2) {
				int start = route_raw.indexOf(":[", index)+2;
				int end = route_raw.indexOf(",", index);
				float lat = Float.parseFloat(route_raw.substring(start, end));
				
				start = end+1;
				end = route_raw.indexOf(",", start);
				float lng = Float.parseFloat(route_raw.substring(start, end));
				
				tmp_steps.add(new PVector(lat, lng));
			}
			index = route_raw.indexOf("coordinates", index+1);
		}
		
		steps = new PVector[0];
		steps = (PVector[])tmp_steps.toArray(steps);
		
		breakup(50);
		step();
	}

	public void draw() {
		background(0);
		translate(width/2, height/2);

		int start_tile = floor((6.5f-yaw/360*6.5f)%6.5f);
		float offset = ((6.5f-yaw/360*6.5f)%6.5f-start_tile)*width/6.5f;
		
		for(int i = start_tile;i < 7;i++) {
			if(pics[i] != null) image(pics[i], width/6.5f*(i-start_tile)-width/2-offset, -width/13, width/6.5f, width/6.5f);
		}
		
		for(int i = 0;i < start_tile+1;i++) {
			if(pics[i] != null) image(pics[i], width/6.5f*(i+7-start_tile)-width/2-width/13-offset, -width/13, width/6.5f, width/6.5f);
		}

		fill(255);
		stroke(255);
		strokeWeight(4);
		line(0, -height/2, 0, height/2);
		
		
		// fill(255);
		// stroke(255);
		// 
		// strokeWeight(4);
		// 
		// line(((3.5f-yaw/360*7+7)%7)*width/7-width/2, -height/2, ((3.5f-yaw/360*7+7)%7)*width/7-width/2, height/2);
	}

	public void mousePressed() {
		step();
	}

	public void breakup(int subdivision) {
		if(debug) println("====Breakup====");
		
		float total_dist = 0;
		for(int i = 1;i < steps.length;i++) total_dist += steps[i].dist(steps[i-1]);

		LinkedList tmp_steps = new LinkedList();
		float unit = total_dist/(subdivision-1);
		
		tmp_steps.add(steps[0]);
		
		for(int i = 1;i < steps.length;i++) {
			while(((PVector)tmp_steps.getLast()).dist(steps[i]) > unit) {
					PVector unit_step = PVector.sub(steps[i], (PVector)tmp_steps.getLast());
					unit_step.normalize();
					unit_step.mult(unit);
					tmp_steps.add(PVector.add((PVector)tmp_steps.getLast(), unit_step));
			}

			tmp_steps.add(steps[i]);
		}
		
		steps = (PVector[])tmp_steps.toArray(steps);
		
		if(debug) {
			println(steps.length);
			println("===========");
		}
	}

	public void step() {
		(new Thread(new Runnable() {
			
			public void run() {
				current_step = (current_step+1)%steps.length;

				if(debug) {
					println("->Step "+current_step);
					println("  Lat:"+steps[current_step].x+"\tLng:"+steps[current_step].y);
				}

				view = new View(steps[current_step].y, steps[current_step].x);

				PImage[] tmp_pics = new PImage[7];

				for(int i = 0;i < 7;i++) {
					if(view.getPanoIDs() != null) tmp_pics[i] = loadImage(view.getImgURL(i,1), "jpg");
				}

				zoom_index = 1;
				blur_index = 0;
				pics = tmp_pics;
				yaw = view.getYaw();
				while(yaw < 0) yaw += 360;
				while(yaw > 360) yaw -= 360;
				
				step();
			}
			
		})).start();
	}

	String streamToString(InputStream stream) {
		if(stream != null) {
			try {
				StringBuilder sb = new StringBuilder();
				String line;
			
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
					while((line = reader.readLine()) != null) sb.append(line).append("\n");
				} finally {
					stream.close();
				}
			
				return sb.toString();
			} catch(Exception e) {
				return "";
			}	
		} else {
			return "";
		}
	}

	public static void main (String [] args) {
		PApplet.main(new String[] { "Interpolate" });
	}
}