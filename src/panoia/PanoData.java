package panoia;

public class PanoData {

	public String copyright;

	public PanoLink[] links;

	public PanoLocation location;
	public PanoTileData tiles;

	public PanoData(PanoLocation location, PanoTileData tiles, PanoLink[] links, String copyright) {
		this.location = location;
		this.tiles = tiles;
		this.links = links;

		this.copyright = copyright;
	}

}