package panoia;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.net.URL;
import java.io.InputStream;

public class Panoia {
	
	private Panoia() {}	
	
	public static Document getXML(float latitude, float longitude) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			URL url = new URL("http://maps.google.com/cbk?output=xml&ll="+latitude+","+longitude);

			InputStream stream = url.openStream();
			Document xml_file = docBuilder.parse(stream);
			
			return xml_file;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}
	
	public static Document getXML(String pano_id) {
		try	{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			URL url = new URL("http://maps.google.com/cbk?output=xml&panoid="+pano_id);

			InputStream stream = url.openStream();
			Document xml_file = docBuilder.parse(stream);
			
			return xml_file;
		} catch (Exception e) {
			System.err.println("Panoia: Error in getXML...");
		}
		
		return null;
	}
	
	public static float[] getLatLng(String pano_id) {
		return getLatLng(getXML(pano_id));
	}
	
	public static float[] getLatLng(Document xml_file) {
		try {
			NodeList data = xml_file.getElementsByTagName("data_properties");
			NodeList links = xml_file.getElementsByTagName("link");

			float lat = Float.parseFloat(data.item(0).getAttributes().getNamedItem("lat").getNodeValue());
			float lng = Float.parseFloat(data.item(0).getAttributes().getNamedItem("lng").getNodeValue());
			
			return new float[] {lat, lng};
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}
		
		return null;
	}
	
	public static String[] getPanoIDs(float latitude, float longitude) {
		return getPanoIDs(getXML(latitude, longitude));
	}
	
	public static String[] getPanoIDs(String pano_id) {
		return getPanoIDs(getXML(pano_id));
	}
	
	public static String[] getPanoIDs(Document xml_file) {
		try {
			NodeList data = xml_file.getElementsByTagName("data_properties");
			NodeList links = xml_file.getElementsByTagName("link");

			String[] pano_ids = new String[links.getLength()+1];

			pano_ids[0] = data.item(0).getAttributes().getNamedItem("pano_id").getNodeValue();
		
			for(int i = 0;i < links.getLength();i++) {
				pano_ids[i+1] = links.item(i).getAttributes().getNamedItem("pano_id").getNodeValue();
			}
			
			return pano_ids;
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

		return null;		
	}
	
}