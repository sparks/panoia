package panoia;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.io.InputStream;

public class PanoProvider {

	String apikey;

	public PanoProvider() {
		this(null);
	}

	public PanoProvider(String apikey) {
		this.apikey = apikey;
	}

	public PanoData getPanoData(String pano) {
		return buildPanoData(getXML(pano));
	}

	public PanoData getPanoData(LatLng latLng) {
		return buildPanoData(getXML(latLng));
	}

	PanoData buildPanoData(Document xml) {
		try {
			//Parse Location Information and Copyright
			Node data = xml.getElementsByTagName("data_properties").item(0);

			float lat = Float.parseFloat(data.getAttributes().getNamedItem("lat").getNodeValue());
			float lng = Float.parseFloat(data.getAttributes().getNamedItem("lng").getNodeValue());
			LatLng latLng = new LatLng(lat, lng);

			String pano = data.getAttributes().getNamedItem("pano_id").getNodeValue();
			
			Node descNode = xml.getElementsByTagName("text").item(0);
			String desc = descNode.getNodeValue();

			PanoLocation location = new PanoLocation(pano, latLng, desc);

			//Parse Tiles
			Node proj = xml.getElementsByTagName("projection_properties").item(0);
			float heading = Float.parseFloat(proj.getAttributes().getNamedItem("pano_yaw_deg").getNodeValue());

			PanoTileData tiles = new PanoTileData(location, heading);

			//Parse links
			NodeList linkNodes = xml.getElementsByTagName("link");
			PanoLink[] links = new PanoLink[linkNodes.getLength()];

			for(int i = 0;i < linkNodes.getLength();i++) {
				Node node = linkNodes.item(i);

				String linkPano = node.getAttributes().getNamedItem("pano_id").getNodeValue();
				float linkHeading = Float.parseFloat(node.getAttributes().getNamedItem("yaw_deg").getNodeValue());
				String linkDesc = node.getFirstChild().getNodeValue();

				links[i] = new PanoLink(linkPano, linkHeading, linkDesc);
			}

			//Copyright
			Node copyNode = xml.getElementsByTagName("copyright").item(0);
			String copyright = copyNode.getNodeValue();

			//Put it all together
			return new PanoData(location, tiles, links, copyright);
		} catch(Exception e) {
			//Probably no such street view, so ignore
			return null;
		}

	}

	public Document getXML(LatLng latLng) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			String urlString = "http://maps.google.com/cbk?output=xml&ll="+latLng.toUrlValue();
			if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;
			URL url = new URL(urlString);

			InputStream stream = url.openStream();
			Document xml = docBuilder.parse(stream);
			
			return xml;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}
	
	public Document getXML(String pano) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();


			String urlString = "http://maps.google.com/cbk?output=xml&panoid="+pano;
			if(apikey != null && !apikey.equals("")) urlString += "&key="+apikey;
			URL url = new URL(urlString);

			InputStream stream = url.openStream();
			Document xml = docBuilder.parse(stream);
			
			return xml;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}

}