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
	
	public static float getPanoYaw(float latitude, float longitude) {
		return getPanoYaw(getXML(latitude, longitude));
	}
	
	public static float getPanoYaw(String pano_id) {
		return getPanoYaw(getXML(pano_id));
	}
	
	public static float getPanoYaw(Document xml_file) {
		try {
			NodeList proj = xml_file.getElementsByTagName("projection_properties");
			
			String yaw = proj.item(0).getAttributes().getNamedItem("pano_yaw_deg").getNodeValue();

			return Float.parseFloat(yaw);
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

		return 0;		
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

			String[] pano_ids = new String[data.getLength()];

			for(int i = 0;i < data.getLength();i++) {
				pano_ids[i] = data.item(i).getAttributes().getNamedItem("pano_id").getNodeValue();
			}
			
			return pano_ids;
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

		return null;		
	}
	
	public static String[] getLinkIDs(float latitude, float longitude) {
		return getLinkIDs(getXML(latitude, longitude));
	}
	
	public static String[] getLinkIDs(String pano_id) {
		return getLinkIDs(getXML(pano_id));
	}
	
	public static String[] getLinkIDs(Document xml_file) {
		try {
			NodeList link = xml_file.getElementsByTagName("link");

			String[] link_ids = new String[link.getLength()];

			for(int i = 0;i < link.getLength();i++) {
				link_ids[i] = link.item(i).getAttributes().getNamedItem("pano_id").getNodeValue();
			}
			
			return link_ids;
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

		return null;		
	}
	
	public static float[] getLinkYaws(float latitude, float longitude) {
		return getLinkYaws(getXML(latitude, longitude));
	}
	
	public static float[] getLinkYaws(String pano_id) {
		return getLinkYaws(getXML(pano_id));
	}
	
	public static float[] getLinkYaws(Document xml_file) {
		try {
			NodeList link = xml_file.getElementsByTagName("link");

			float[] link_yaws = new float[link.getLength()];

			for(int i = 0;i < link.getLength();i++) {
				link_yaws[i] = Float.parseFloat(link.item(i).getAttributes().getNamedItem("yaw_deg").getNodeValue());
			}
			
			return link_yaws;
		} catch(Exception e) {
			//Probably no such street view, so ignore
		}

		return null;		
	}
}