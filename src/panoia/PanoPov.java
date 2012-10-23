package panoia;

public class PanoPov {

	public int zoom;
	public float heading, pitch;

	public PanoPov() {
		zoom = 0;
		heading = 0;
		pitch = 0;
	}

	public PanoPov(int zoom, float heading, float pitch) {
		this.zoom = zoom;
		this.heading = heading;
		this.pitch = pitch;
	}

}