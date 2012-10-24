package panoia;

public class PanoPov {

	public int zoom;
	float heading, pitch;

	public PanoPov() {
		zoom = 0;
		heading = 0;
		pitch = 0;
	}

	public PanoPov(int zoom, float heading, float pitch) {
		this.zoom = zoom;
		setHeading(heading);
		setPitch(pitch);
	}

	public void setHeading(float heading) {
		while(heading < 0) heading += 360;
		while(heading > 360) heading -= 360;
		this.heading = heading;
	}

	public void setPitch(float pitch) {
		while(pitch < 0) pitch += 360;
		while(pitch > 360) pitch -= 360;
		this.pitch = pitch;
	}

	public float heading() {
		return heading;
	}

	public float pitch() {
		return pitch;
	}

}