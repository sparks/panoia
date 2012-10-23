package panoia;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.io.InputStream;

public class GooglePanoProvider implements PanoProvider {

	String apikey;

	public GooglePanoProvider() {
		this(null);
	}

	public GooglePanoProvider(String apikey) {
		return buildPanoData(getXML(pano));
	}

	public PanoData getPanoData(Sting pano) {
		return buildPanoData(getXML(pano));
	}

	public PanoData getPanoData(LatLng lagLng) {
		return null;
	}

	void buildPanoData(Document xml) {
		try {
			//Parse Location Information and Copyright
			Node data = xml.getElementsByTagName("data_properties").item(0);

			float lat = Float.parseFloat(data.getAttributes().getNamedItem("lat").getNodeValue());
			float lng = Float.parseFloat(data.getAttributes().getNamedItem("lng").getNodeValue());
			LatLng latLng = new LatLng(lat, lng);

			String pano = data.getAttributes().getNamedItem("pano_id").getNodeValue();
			
			Node descNode = xml.getElementsByTagName("text").item(0);
			String desc = descNode.getValue();

			PanoLocation location = new PanoLocation(pano, latLng, desc);

			//Parse Tiles
			Node proj = xml.getElementsByTagName("projection_properties").item(0);
			String heading = proj.getAttributes().getNamedItem("pano_yaw_deg").getNodeValue();

			PanoTileData tiles = new PanoTileData(location, heading);

			//Parse links
			NodeList linksNodes = xml.getElementsByTagName("link");
			PanoLinks[] links = new PanoLinks[linksNodes.getLength()];

			for(int i = 0;i < linksNodes.getLength();i++) {
				Node node = linkNodes.item(i);

				String linkPano = node.getAttributes().getNamedItem("pano_id").getNodeValue();
				float linkHeading = node.getAttributes().getNamedItem("yaw_deg").getNodeValue();
				String linkDesc = node.getFirstChild().getNodeValue();

				links[i] = new PanoLink(linkPano, linkHeading, linkDesc);
			}

			//Copyright
			Node copyNode = xml.getElementsByTagName("copyright").item(0);
			String copyright = copyNode.getValue();

			//Put it all together
			PanoData data = new PanoData(location, tiles, links, copyright);
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

	}

	public static Document getXML(LatLng latLng) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			String url_string = "http://maps.google.com/cbk?output=xml&ll="+latLng.toUrlValue();
			if(key != null && !key.equals("")) url_string
			URL url = new URL();

			InputStream stream = url.openStream();
			Document xml = docBuilder.parse(stream);
			
			return xml;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}
	
	public static Document getXML(String pano) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			URL url = new URL("http://maps.google.com/cbk?output=xml&panoid="+pano);

			InputStream stream = url.openStream();
			Document xml = docBuilder.parse(stream);
			
			return xml;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}

}