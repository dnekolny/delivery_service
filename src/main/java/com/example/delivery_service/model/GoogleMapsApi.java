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
}