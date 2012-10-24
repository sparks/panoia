package panoia;

import java.lang.Math;

public class LatLng {

	public float lat, lng;

	public LatLng(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public String toString() {
		return "("+lat+", "+lng+")";
	}

	public String toUrlValue() {
		return lat+","+lng;
	}

	public boolean equals(Object o) {
		if(o instanceof LatLng) {
			LatLng l = (LatLng)o;
			if(l.lat == lat && l.lng == lng) return true;
		}

		return false;
	}

	public double getDistance(LatLng point) {
		double lat1 = Math.toRadians(lat);
		double lng1 = Math.toRadians(lng);
		double lat2 = Math.toRadians(point.lat);
		double lng2 = Math.toRadians(point.lng);

		float earthRadius = 6371*1000; //m
		double dLat = lat2-lat1;
		double dLon = lng2-lng1;

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = earthRadius * c;

		return d;
	}

	public double getInitialBearing(LatLng point) {
		double lat1 = Math.toRadians(lat);
		double lng1 = Math.toRadians(lng);
		double lat2 = Math.toRadians(point.lat);
		double lng2 = Math.toRadians(point.lng);

		double dLat = lat2-lat1;
		double dLon = lng2-lng1;

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
		double brng = Math.toDegrees(Math.atan2(y, x));

		return brng;
	}

}