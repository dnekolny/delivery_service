package com.example.delivery_service.model;


import com.example.delivery_service.model.Entity.Address;
import com.example.delivery_service.model.Entity.LatLng;
import com.example.delivery_service.model.Entity.Order;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapsApi
{
    GeoApiContext context;

    public GoogleMapsApi()
    {
        context = new GeoApiContext.Builder()
                .apiKey(MapsApiKeyReader.readKey())
                .build();
    }

    public List<Order> sortOrdersByShortestRoute(String start, List<Order> orders) throws ApiException, InterruptedException, IOException {
        List<Order> sortedOrders = new ArrayList<>();

        if(orders.size() > 0) {
            String[] waypoints = new String[orders.size()];
            for (int i = 0; i < orders.size(); i++) {
                waypoints[i] = orders.get(i).getDriveFormatAddress();
            }

            DirectionsResult result = DirectionsApi.newRequest(context)
                    .origin(start)
                    .destination(start)
                    .waypoints(waypoints)
                    .optimizeWaypoints(true)
                    .await();

            int[] wporder = result.routes[0].waypointOrder;

            for (int i : wporder) {
                sortedOrders.add(orders.get(i));
            }
        }
        return sortedOrders;
    }

    public LatLng geocode(final String address) throws ApiException, InterruptedException, IOException{
        LatLng ll = null;
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        if(results.length > 0) {
            ll = new LatLng(results[0].geometry.location);
        }
        return ll;
    }

    /*public LatLng geocode(final String address) throws IOException
    {
        InputStream is = invokeService(buildGeocodingUrl(address));
        if (is != null)
        {
            try
            {
                String response = IOUtils.toString(is);
                return decodeGeocoding(response);
            }
            finally
            {
                is.close();
            }
        }
        return null;
    }

    public List<Order> sortOrdersByShortestRoute(Address start, List<Order> orders) throws IOException{
        List<Order> sortedOrders = new ArrayList<>();
        List<String> waypoints = new ArrayList<>();
        orders.forEach(o -> waypoints.add(o.getDriveFormatAddress()));

        InputStream is = invokeService(buildDirectionUrl(start.getFormatAddress(), waypoints));
        if (is != null)
        {
            try
            {
                String response = IOUtils.toString(is);
                sortedOrders = decodeDirection(response);
            }
            finally
            {
                is.close();
            }
        }

        return sortedOrders;
    }


    private LatLng decodeGeocoding(String response) throws GeocoderException
    {
        JSONObject obj = new JSONObject(response);

        String status = obj.getString("status");

        if (!status.equals(GeocoderException.G_GEO_SUCCESS))
        {
            throw new GeocoderException(status);
        }

        JSONArray arr = obj.getJSONArray("results");
        JSONObject location = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        double latitude = location.getDouble("lat");
        double longitude = location.getDouble("lng");

        return new LatLng(latitude, longitude);
    }


    private List<Order> decodeDirection(String response) throws GeocoderException
    {
        JSONObject obj = new JSONObject(response);

        /*String status = obj.getString("status");

        if (!status.equals(GeocoderException.G_GEO_SUCCESS))
        {
            throw new GeocoderException(status);
        }

        JSONArray arr = obj.getJSONArray("results");
        JSONObject location = arr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        double latitude = location.getDouble("lat");
        double longitude = location.getDouble("lng");

        return new LatLng(latitude, longitude);*/

        /*return new ArrayList<>();
    }


    private InputStream invokeService(final String address) throws IOException
    {
        URL url = new URL(address);
        return url.openStream();
    }


    private String buildGeocodingUrl(final String address)
    {
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + urlEncode(address) + "&key=" + MapsApiKeyReader.readKey();
        //return "http://maps.google.com/maps/geo?q=" + urlEncode(address) + "&output=" + output;
    }

    private String buildDirectionUrl(String start, List<String> waypoints)
    {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=" + MapsApiKeyReader.readKey();
        //return "https://maps.googleapis.com/maps/api/geocode/json?address=" + urlEncode(address) + "&key=" + MapsApiKeyReader.readKey();
        //return "http://maps.google.com/maps/geo?q=" + urlEncode(address) + "&output=" + output;
    }


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
    }*/
}