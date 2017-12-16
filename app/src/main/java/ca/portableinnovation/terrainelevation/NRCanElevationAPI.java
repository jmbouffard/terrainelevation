package ca.portableinnovation.terrainelevation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import android.util.Log;

public class NRCanElevationAPI {

	// Elevation Web Service API described at
	// http://www.nrcan.gc.ca/earth-sciences/geography/topographic-information/free-data-geogratis/geogratis-web-services/api/17328

	// Canadian Digital Elevation Model Mosaic (CDEM)
	// See http://geogratis.gc.ca/api/en/nrcan-rncan/ess-sst/C40ACFBA-C722-4BE1-862E-146B80BE738E.html
	private final String mAltitudeURLxml = "http://geogratis.gc.ca/services/elevation/cdem/altitude.xml";
	private final String mAltitudeURLjson = "http://geogratis.gc.ca/services/elevation/cdem/altitude.json";
	private final String mProfileURLxml = "http://geogratis.gc.ca/services/elevation/cdem/profile.xml";
	private final String mProfileURLjson = "http://geogratis.gc.ca/services/elevation/cdem/profile.json";
	// Canadian Digital Surface Model Mosaic (CDSM)
	// See http://geogratis.gc.ca/api/en/nrcan-rncan/ess-sst/34F13DB8-434B-4A37-AE38-03643433FBBB.html
	private final String mCDSMAltitudeURLxml = "http://geogratis.gc.ca/services/elevation/cdsm/altitude.xml";
	private final String mCDSMAltitudeURLjson = "http://geogratis.gc.ca/services/elevation/cdsm/altitude.json";
	private final String mCDSMProfileURLxml = "http://geogratis.gc.ca/services/elevation/cdsm/profile.xml";
	private final String mCDSMProfileURLjson = "http://geogratis.gc.ca/services/elevation/cdsm/profile.json";

	public static final String rootProfileName = "elevations";
	public static final String rootAltitudeName = "elevation";
	public static final String itemDataIdName = "altitude";

	private static final String sDebugTag = "TE.NRCanElevationAPI";
	private static final boolean sLogDebugEnabled = true;

	public enum ElevationDB {
		CDEM, CDSM
	}

	public NRCanElevationAPI() {
	}
	
	/**
	 * Function that reads elevation from the terrain API
	 * @param latitude Latitude of the position to query
	 * @param longitude Longitude of the position to query
	 * @return Elevation of the location to query
	 */
	public double getElevation(ElevationDB elevDB, double latitude, double longitude) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		String result = "";

		try {
			switch (elevDB) {
				case CDEM:
					url = new URL(mAltitudeURLxml+"?lat="+latitude+"&lon=-"+longitude);
					break;
				case CDSM:
					url = new URL(mCDSMAltitudeURLxml+"?lat="+latitude+"&lon=-"+longitude);
					break;
			}
		} catch (MalformedURLException e) {
			Log.e(sDebugTag, "getElevation: MalformedURLException: " + e.getMessage());
			return -1;
		}

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			Log.e(sDebugTag, "getElevation: IOException: " + e.getMessage());
			return -2;
		}

		try {
			InputStream returnedInputStream = new BufferedInputStream(urlConnection.getInputStream());
			// Start parsing of the returned XML document
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(returnedInputStream);
				//Read main element <elevation>
				Element rootElement = doc.getDocumentElement();
				if (isStringEqualTo(rootElement.getNodeName(), rootAltitudeName))
				{
					// Read second level element <altitude>
					Node item = rootElement.getFirstChild();
					while (item != null) {
						if (item.getNodeType() == 1 && isStringEqualTo(item.getNodeName(), itemDataIdName))
						{
							result = item.getTextContent();
							break;
						} else if (sLogDebugEnabled) {
							// This debug info is used to find data items which are not handled by the application
							Log.d(sDebugTag, "getElevation: Unhandled data element :"+
												item.getNodeName());
						}
						item = item.getNextSibling();
					}
				}
			} catch (ParserConfigurationException e) {
				Log.e(sDebugTag, "getElevation: ParserConfigurationException: " + e.getMessage());
			} catch (IllegalStateException e) {
				Log.e(sDebugTag, "getElevation: IllegalStateException: " + e.getMessage());
			} catch (SAXException e) {
				Log.e(sDebugTag, "getElevation: SAXException: " + e.getMessage());
			}
		}
		catch (IOException e) {
			Log.e(sDebugTag, "getElevation: IOException: " + e.getMessage());
		}
		finally {
			urlConnection.disconnect();
		}

		// Handle value returned by the API call
		try {
			return Double.parseDouble(result);
		} catch (NumberFormatException e) {
			return -999.0;
		}
	}

	/**
	 * Function that reads the elevation profile (10 points) between two coordinates from the terrain API
	 * @param startLat Latitude of the starting position to query
	 * @param startLon Longitude of the starting position to query
	 * @param endLat Latitude of the ending position to query
	 * @param endLon Longitude of the ending position to query
	 * @return Profile of the location to query
	 */
	public List<Double> getElevationProfile_10(ElevationDB elevDB, double startLat, double startLon, double endLat, double endLon) {
		return getElevationProfile(elevDB, startLat, startLon, endLat, endLon, 10);
	}

	/**
	 * Function that reads the elevation profile between two coordinates from the terrain API
	 * @param startLat Latitude of the starting position to query
	 * @param startLon Longitude of the starting position to query
	 * @param endLat Latitude of the ending position to query
	 * @param endLon Longitude of the ending position to query
	 * @param steps Number of points to get between both locations
	 * @return Profile of the location to query
	 */
	public List<Double> getElevationProfile(ElevationDB elevDB, double startLat, double startLon, double endLat, double endLon, int steps) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		List<Double> profileLst = new ArrayList<Double>();

		try {
			switch (elevDB) {
				case CDEM:
					url = new URL(mProfileURLxml + "?path=LINESTRING(-" + startLon + "%20" + startLat + ",%20-" + endLon + "%20" + endLat + ")&steps=" + steps);
					break;
				case CDSM:
					url = new URL(mCDSMProfileURLxml + "?path=LINESTRING(-" + startLon + "%20" + startLat + ",%20-" + endLon + "%20" + endLat + ")&steps=" + steps);
					break;
			}
		} catch (MalformedURLException e) {
			Log.e(sDebugTag, "getElevationProfile: MalformedURLException: " + e.getMessage());
			profileLst.add(-999.0);
			return profileLst;
		}

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			Log.e(sDebugTag, "getElevationProfile: IOException: " + e.getMessage());
			profileLst.add(-999.0);
			return profileLst;
		}

		try {
			InputStream returnedInputStream = new BufferedInputStream(urlConnection.getInputStream());
			// Start parsing of the returned XML document
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(returnedInputStream);
				//Read main element <elevations>
				Element rootElement = doc.getDocumentElement();
				if (isStringEqualTo(rootElement.getNodeName(), rootProfileName))
				{
					// Read second level element <altitude>
					Node level2item = rootElement.getFirstChild();
					while (level2item != null) {
						if (level2item.getNodeType() == 1 && isStringEqualTo(level2item.getNodeName(), rootAltitudeName))
						{
							// Read third level element <altitude>
							Node level3item = level2item.getFirstChild();
							while (level3item != null) {
								if (level3item.getNodeType() == 1 && isStringEqualTo(level3item.getNodeName(), itemDataIdName))
								{
									try {
										double res = Double.parseDouble(level3item.getTextContent());
										profileLst.add(res);
									} catch (NumberFormatException e) {
										profileLst.add(-999.0);
									}
									break;
								} else if (sLogDebugEnabled) {
									// This debug info is used to find data items which are not handled by the application
									Log.d(sDebugTag, "getElevation: Unhandled level 3 element :"+
											level3item.getNodeName());
								}
								level3item = level3item.getNextSibling();
							}
						} else if (sLogDebugEnabled) {
							// This debug info is used to find data items which are not handled by the application
							Log.d(sDebugTag, "getElevation: Unhandled level 2 element :"+
									level2item.getNodeName());
						}
						level2item = level2item.getNextSibling();
					}
				}
			} catch (ParserConfigurationException e) {
				Log.e(sDebugTag, "getElevation: ParserConfigurationException: " + e.getMessage());
			} catch (IllegalStateException e) {
				Log.e(sDebugTag, "getElevation: IllegalStateException: " + e.getMessage());
			} catch (SAXException e) {
				Log.e(sDebugTag, "getElevation: SAXException: " + e.getMessage());
			}
		}
		catch (IOException e) {
			Log.e(sDebugTag, "getElevationProfile: IOException: " + e.getMessage());
		}
		finally {
			urlConnection.disconnect();
		}

		// Handle value returned by the API call
		return profileLst;
	}
	
	/* Compares two strings and returns true if equal */
	public static boolean isStringEqualTo(String element1, String element2) {
		if (element1.compareTo(element2) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Reads an InputStream to a String */
	public static String readInputStreamToString(final InputStream is, final int bufferSize)
	{
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "ISO-8859-1");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			}
			finally {
				in.close();
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(sDebugTag, "readInputStreamToString: UnsupportedEncodingException: " + e.getMessage());
		} catch (IOException e) {
			Log.e(sDebugTag, "readInputStreamToString: IOException: " + e.getMessage());
		}
		return out.toString();
	}

}
