package com.lettucedate.yelp;

/**
 * Created by Dave on 9/23/2015.
 */
public class Region {
    private Center center;

    private Span span;

    public Center getCenter ()
    {
        return center;
    }

    public void setCenter (Center center)
    {
        this.center = center;
    }

    public Span getSpan ()
    {
        return span;
    }

    public void setSpan (Span span)
    {
        this.span = span;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [center = "+center+", span = "+span+"]";
    }

}
