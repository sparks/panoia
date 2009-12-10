/*x=0 to 6 Pan Horizontal (Clockwise)
y=0 to 2 Pan Vertical (Top to Bottom)
zoom=0 to 24 Zoom (Dunno, 3 is what we want)
Paranoid=location ...

http://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q=street+view:+7564+Casgrain+montreal+qc&sll=45.547158,-73.571802&sspn=0.240438,0.883026&ie=UTF8&hq=street+view:+7564+Casgrain&hnear=Montreal,+QC,+Canada&ll=45.534516,-73.660685&spn=0,359.998275&z=19&layer=c&cbll=45.534704,-73.660548&panoid=mM0wN-3bzgNdyzCl3NpHog&cbp=12,183.88,,0,5
*/

import panoia.*;

View v;

void setup() {
	size(512*7,512);
	background(0);
	noLoop();
	v = new View("3qry8ACTZ8Mw6SQ1UaLNMg");
	draw();
}

void draw() {
	String[] urls = v.getImgURLs();
	for(int i = 0;i < urls.length;i++) {
		PImage img = loadImage(urls[i],"jpg");
		image(img, 512*i, 0);
	}
}