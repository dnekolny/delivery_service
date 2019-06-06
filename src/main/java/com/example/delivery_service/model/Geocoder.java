package com.example.delivery_service.model;

import com.example.delivery_service.exceptions.GeocoderException;
import com.example.delivery_service.model.Entity.LatLng;
import org.apache.wicket.util.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Geocoder. See: http://www.google.com/apis/maps/documentation/services.html# Geocoding_Direct
 *
 * @author Thijs Vonk
 */
public class Geocoder implements Serializable
{

    private static final long serialVersionUID = 1L;
    // Constants
    public static final String OUTPUT_CSV = "csv";
    public static final String OUTPUT_XML = "xml";
    public static final String OUTPUT_KML = "kml";
    public static final String OUTPUT_JSON = "json";
    private final String output = OUTPUT_CSV;

    public Geocoder()
    {
    }

    public LatLng decode(String response) throws GeocoderException
    {
        JSONObject obj = new JSONObject(response);

        String status = obj.getString("status");

        if (!status.equals(GeocoderException.G_GEO_SUCCESS))
        {
            throw new GeocoderException(status);
        }

        JSONArray arr = obj.getJSONArray("results");
        JSONObject location = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        Double latitude = location.getDouble("lat");
        Double longitude = location.getDouble("lng");

        return new LatLng(latitude, longitude);
    }

    /**
     * builds the google geo-coding url
     *
     * @param address
     * @return
     */
    public String encode(final String address)
    {
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + urlEncode(address) + "&key=" + MapsApiKeyReader.readKey();
        //return "http://maps.google.com/maps/geo?q=" + urlEncode(address) + "&output=" + output;
    }

    /**
     * @param address
     * @return
     * @throws IOException
     */
    public LatLng geocode(final String address) throws IOException
    {
        InputStream is = invokeService(encode(address));
        if (is != null)
        {
            try
            {
                String content = IOUtils.toString(is);
                return decode(content);
            }
            finally
            {
                is.close();
            }
        }
        return null;
    }

    /**
     * fetches the url content
     *
     * @param address
     * @return
     * @throws IOException
     */
    protected InputStream invokeService(final String address) throws IOException
    {
        URL url = new URL(address);
        return url.openStream();
    }

    /**
     * url-encode a value
     *
     * @param value
     * @return
     */
    private String urlEncode(final String value)
    {
        try
        {
            return URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
}