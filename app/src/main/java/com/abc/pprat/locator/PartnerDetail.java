package com.abc.pprat.locator;

public class PartnerDetail {

    public String Key;
    public String Latitude;
    public String Longitude;
    public String Name;
    public String Status;


    public PartnerDetail()
    {}

    public PartnerDetail(String key, String latitude, String longitude, String name, String status) {
        Key = key;
        Latitude = latitude;
        Longitude = longitude;
        Name = name;
        Status = status;
    }

    public void setKeyp(String key) {
        Key = key;
    }

    public void setLatitudep(String latitude) {
        Latitude = latitude;
    }

    public void setLongitudep(String longitude) {
        Longitude = longitude;
    }

    public void setNamep(String name) {
        Name = name;
    }

    public void setStatusp(String status) {
        Status = status;
    }

    public String getKeyp() {
        return Key;
    }

    public String getLatitudep() {
        return Latitude;
    }

    public String getLongitudep() {
        return Longitude;
    }

    public String getNamep() {
        return Name;
    }

    public String getStatusp() {
        return Status;
    }
}

