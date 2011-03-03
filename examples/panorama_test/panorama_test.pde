/*x=0 to 6 Pan Horizontal (Clockwise)
y=0 to 2 Pan Vertical (Top to Bottom)
zoom=0 to 24 Zoom (Dunno, 3 is what we want)
Paranoid=location ...

http://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q=street+view:+7564+Casgrain+montreal+qc&sll=45.547158,-73.571802&sspn=0.240438,0.883026&ie=UTF8&hq=street+view:+7564+Casgrain&hnear=Montreal,+QC,+Canada&ll=45.534516,-73.660685&spn=0,359.998275&z=19&layer=c&cbll=45.534704,-73.660548&panoid=mM0wN-3bzgNdyzCl3NpHog&cbp=12,183.88,,0,5
*/

import panoia.*;

View view;
float[] yaws;

void setup() {
	size(screen.width,screen.width/7*3);
	background(0);
	noLoop();
	
	view = new View(45.537734, -73.622578);
}

void draw() {
	background(0);
	translate(width/2, height/2);
	
	float yaw = view.getYaw();
	
	PImage[] pics = new PImage[7];
	
	if(view.getPanoIDs() != null) {
		for(int i = 0;i < 7;i++) pics[i] = loadImage(view.getImgURL(i,1), "jpg");
	}

	int start_tile = floor((6.5-yaw/360*6.5)%6.5);
	float offset = ((6.5-yaw/360*6.5)%6.5-start_tile)*width/6.5;
	
	for(int i = start_tile;i < 7;i++) {
		if(pics[i] != null) image(pics[i], width/6.5*(i-start_tile)-width/2-offset, -width/13, width/6.5, width/6.5);
	}
	
	for(int i = 0;i < start_tile+1;i++) {
		if(pics[i] != null) image(pics[i], width/6.5*(i+7-start_tile)-width/2-width/13-offset, -width/13, width/6.5, width/6.5);
	}

	fill(255);
	stroke(255);
	strokeWeight(4);
	
	yaws();
	
	for(int i = 0;i < yaws.length;i++) {
		line(yaws[i]-width/2, -height/2, yaws[i]-width/2, height/2);
	}
}

void mousePressed() {
	for(int i = 0;i < yaws.length;i++) {
		if(abs(mouseX-yaws[i]) < 200) {
			view = new View(view.getLinkIDs()[i]);
			redraw();
			return;
		}
	}
}

void yaws() {
	yaws = null;
	while(yaws == null) {
		yaws = view.getLinkYaws();
		Thread.yield();
	}
	for(int i = 0;i < yaws.length;i++) {
		yaws[i] = map(yaws[i], 0, 360, 0, width);
	}
}