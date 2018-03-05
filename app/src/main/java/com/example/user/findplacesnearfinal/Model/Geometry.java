package com.example.user.findplacesnearfinal.Model;

/**
 * Created by user on 20/02/2018.
 */

public class Geometry {

    private LocationLatLng location;

    public Geometry(LocationLatLng location) {
        this.location = location;
    }

    public LocationLatLng getLocation() {
        return location;
    }
}
