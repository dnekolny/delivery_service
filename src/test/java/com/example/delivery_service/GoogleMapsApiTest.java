package com.example.delivery_service;

import com.example.delivery_service.model.MapsApiKeyReader;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class GoogleMapsApiTest{

    private GeoApiContext context;

    public GoogleMapsApiTest() {

    }

    @Before
    public void setUpContext(){
        context = new GeoApiContext.Builder()
                .apiKey(MapsApiKeyReader.readKey())
                .build();
    }

    @Test
    public void testContext(){
        assertNotNull(context);
    }

    @Test
    public void testGeocode() throws Exception {
        GeocodingResult[] results = GeocodingApi.newRequest(context).address("Hradešínská 1927/69, 101 00 Vinohrady").await();

        assertNotNull(results);
        assertNotNull(results[0].geometry);
        assertNotNull(results[0].geometry.location);
        assertEquals(50.07446419, results[0].geometry.location.lat, 0.005);
        assertEquals(14.4616468, results[0].geometry.location.lng, 0.005);
        assertEquals(LocationType.ROOFTOP, results[0].geometry.locationType);
        assertNotNull(Arrays.toString(results));
    }

    @Test
    public void testReverseGeocode() throws Exception {
        LatLng latlng = new LatLng(50.07446419, 14.4616468);
        GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(latlng).await();

        assertNotNull(results);
        assertNotNull(Arrays.toString(results));
        assertEquals("Hradešínská 1927/69, 101 00 Vinohrady, Czechia", results[0].formattedAddress);
        assertEquals("69", results[0].addressComponents[0].longName);
        assertEquals("69", results[0].addressComponents[0].shortName);
        assertEquals(AddressComponentType.STREET_NUMBER, results[0].addressComponents[0].types[0]);
        assertEquals(AddressType.STREET_ADDRESS, results[0].types[0]);
    }

    @Test
    public void testSimpleDirection() throws Exception {
        String start = "Ve žlíbku 1800/77, 193 00, Praha 9";
        String end = "Hradešínská 1927/69, 101 00 Vinohrady";

        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(start)
                .destination(end)
                .await();

        assertNotNull(result);
        assertEquals(result.geocodedWaypoints.length, 2);
        assertEquals(result.geocodedWaypoints[0].geocoderStatus, GeocodedWaypointStatus.OK);
        assertTrue(result.routes.length > 0);
        assertEquals(result.routes[0].legs.length, 1);
    }

    @Test
    public void testDirectionWithWaypoints() throws Exception {
        String[] waypoints = getWaypoints();
        String start = "Ve žlíbku 1800/77, 193 00, Praha 9";

        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(start)
                .destination(start)
                .waypoints(waypoints)
                .optimizeWaypoints(true)
                .await();

        assertNotNull(result);
        assertEquals(result.geocodedWaypoints.length, 10);
        assertEquals(result.geocodedWaypoints[0].geocoderStatus, GeocodedWaypointStatus.OK);
        assertTrue(result.routes.length > 0);
        assertArrayEquals(result.routes[0].waypointOrder, new int[]{2,4,5,1,7,3,0,6});
    }

    private String[] getWaypoints(){

        return new String[] {
                "nám. 1. máje 12, 270 62 Rynholec",
                "Dolní Olešnice 34, 543 71 Dolní Olešnice",
                "Rozkoš 19, 671 53 Rozkoš",
                "Nová Ves 38, 277 52 Nová Ves",
                "Obilní čtvrť 230, 686 03 Staré Město",
                "Olšovec 109, 753 01 Olšovec",
                "Rozvadov 80, 348 06 Rozvadov",
                "Mlýny 40, 407 45 Kytlice"
        };
    }
}
