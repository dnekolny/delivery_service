package com.example.delivery_service.model.Enums;

public enum OrderState {
    WAITING_FOR_PACKAGE, //0
    PROCESSING, //1
    ON_ROAD, //2
    DELIVERED; //3

    public String toString() {
        switch(this){
            case WAITING_FOR_PACKAGE:
                return "Waiting for package handover";
            case PROCESSING:
                return "Package is being processed";
            case ON_ROAD:
                return "Package is on its way";
            case DELIVERED:
                return "Package is delivered";

        }

        return "";
    }

    public String getResourceName() {
        switch(this){
            case WAITING_FOR_PACKAGE:
                return "enum.waiting.for.package";
            case PROCESSING:
                return "enum.processing";
            case ON_ROAD:
                return "enum.on.road";
            case DELIVERED:
                return "enum.delivered";
        }
        return "";
    }
}
