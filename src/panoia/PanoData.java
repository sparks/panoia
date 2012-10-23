package panoia;

public class PanoData {

	String copyright;

	PanoLink[] links;

	PanoLocation location;
	PanoTileData tiles;

	public PanoData(PanoLocation location, PanoTileData tiles, PanoLink[] links, String copyright) {
		this.location = location;
		this.tiles = tiles;
		this.links = links;

		this.copyright = copyright;
	}

}