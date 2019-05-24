package com.example.delivery_service.model.Enums;

public enum PickupType{
    PICKUP,
    DROP;

    public String toString() {
        switch(this){
            case PICKUP:
                return "Package pickup by driver";
            case DROP:
                return "Package drop off at branch";
        }

        return "";
    }

    public double getPrice() {
        if(this == PickupType.PICKUP){
            return 89;
        }

        return 0;
    }
}
