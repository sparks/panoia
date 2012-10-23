package panoia;

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

}