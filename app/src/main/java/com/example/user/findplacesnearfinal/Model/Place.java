package com.example.user.findplacesnearfinal.Model;

import java.util.List;

public class Place{

    private Geometry geometry;
    private Location location;
    private String icon;
    private String name;
    private OpenHours opening_hours;
    private List<Photo> photos;
    private double rating;
    private String[] types;

    //for nearSearch - API
//    private String vicinity;

    //for textSearch - API
    private String formatted_address;

    //, String vicinity

    public Place(Geometry geometry, String name, String icon, List<Photo> photos, OpenHours opening_hours, double rating, String[] types, String formatted_address) {
        this.geometry = geometry;
        this.name = name;
        this.icon = icon;
        this.photos = photos;
        this.opening_hours = opening_hours;
        this.rating = rating;
        this.types = types;
      //  this.vicinity = vicinity;
        this.formatted_address = formatted_address;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public OpenHours getOpening_hours() {
        return opening_hours;
    }

    public double getRating() {
        return rating;
    }

    public String[] getTypes() {
        return types;
    }

 //   public String getVicinity() {
  //      return vicinity;
  //  }
}
