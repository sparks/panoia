package panoia;

public interface PanoProvider {

	public PanoData getPanoData(String pano);

	public PanoData getPanoData(LatLng lagLng);

}