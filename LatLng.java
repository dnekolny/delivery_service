package com.example.delivery_service.model.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.StringTokenizer;

@Entity
public class LatLng
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private double lat;
    private double lng;

    public LatLng(){}

    public LatLng(double lat, double lng)
    {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng(com.google.maps.model.LatLng gLL){
        this.lat = gLL.lat;
        this.lng = gLL.lng;
    }

    /**
     * 37.34068368469045, -122.48519897460936
     */
    public static LatLng parse(String value)
    {
        try
        {
            StringTokenizer tokenizer = new StringTokenizer(value, ", ");

            float lat = Float.valueOf(tokenizer.nextToken());
            float lng = Float.valueOf(tokenizer.nextToken());
            return new LatLng(lat, lng);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}