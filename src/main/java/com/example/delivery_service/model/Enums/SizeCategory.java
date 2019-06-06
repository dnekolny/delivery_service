package com.example.delivery_service.model.Enums;

public enum SizeCategory{
    SMALL,
    MEDIUM,
    BIG;

    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase() + " package";
    }

    public String getMeasures() {
        switch(this){
            case SMALL:
                return "30x30x30cm";
            case MEDIUM:
                return "60x40x30cm";
            case BIG:
                return "100x50x50cm";
        }

        return "";
    }

    public double getPrice() {
        switch(this){
            case SMALL:
                return 89;
            case MEDIUM:
                return 109;
            case BIG:
                return 149;
        }

        return 0;
    }

    public String getResourceName() {
        switch(this){
            case SMALL:
                return "enum.small";
            case MEDIUM:
                return "enum.medium";
            case BIG:
                return "enum.big";
        }
        return "";
    }
}