package com.lettucedate.yelp;

/**
 * Created by Dave on 9/23/2015.
 */
public class YelpResults {
    private String total;

    private Region region;

    private Business[] businesses;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public Region getRegion ()
    {
        return region;
    }

    public void setRegion (Region region)
    {
        this.region = region;
    }

    public Business[] getBusinesses ()
    {
        return businesses;
    }

    public void setBusinesses (Business[] businesses)
    {
        this.businesses = businesses;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+", region = "+region+", businesses = "+businesses+"]";
    }

}
