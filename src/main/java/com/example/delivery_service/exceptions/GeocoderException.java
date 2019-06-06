package com.example.delivery_service.exceptions;

import java.io.IOException;

public class GeocoderException extends IOException
{

    public enum GeocoderStatus
    {

        ERROR,
        INVALID_REQUEST,
        OK,
        OVER_QUERY_LIMIT,
        REQUEST_DENIED,
        UNKNOWN_ERROR,
        ZERO_RESULTS
    }
    private static final long serialVersionUID = 1L;
    public static final String G_GEO_SUCCESS = "OK";//200;
    public static final String G_GEO_REQUEST_DENIED = "REQUEST_DENIED";
    public static final int G_GEO_BAD_REQUEST = 400;
    public static final int G_GEO_SERVER_ERROR = 500;
    public static final int G_GEO_MISSING_QUERY = 601;
    public static final int G_GEO_UNKNOWN_ADDRESS = 602;
    public static final int G_GEO_UNAVAILABLE_ADDRESS = 603;
    public static final int G_GEO_UNKNOWN_DIRECTIONS = 604;
    public static final int G_GEO_BAD_KEY = 610;
    public static final int G_GEO_TOO_MANY_QUERIES = 620;
    private final String _status;

    public GeocoderException(String status)
    {
        super("Status " + status);

        _status = status;
    }

    public final String getStatus()
    {
        return _status;
    }
}