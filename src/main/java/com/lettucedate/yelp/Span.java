package com.lettucedate.yelp;

/**
 * Created by Dave on 9/23/2015.
 */
public class Span {
    private String latitude_delta;

    private String longitude_delta;

    public String getLatitude_delta ()
    {
        return latitude_delta;
    }

    public void setLatitude_delta (String latitude_delta)
    {
        this.latitude_delta = latitude_delta;
    }

    public String getLongitude_delta ()
    {
        return longitude_delta;
    }

    public void setLongitude_delta (String longitude_delta)
    {
        this.longitude_delta = longitude_delta;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [latitude_delta = "+latitude_delta+", longitude_delta = "+longitude_delta+"]";
    }
}
