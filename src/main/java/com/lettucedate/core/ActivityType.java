package com.lettucedate.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/23/15.
 */
public class ActivityType  {
    private static final Logger log = Logger.getLogger(ActivityType.class.getName());

    public long id;
    public String typename;
    public String icon;

    private static List<ActivityType> _savedTypeList = null;

    public static List<ActivityType>  GetActivityTypes() {
        return GetActivityTypes(false);
    }
    public static List<ActivityType>  GetActivityTypes(Boolean force) {

        if ((_savedTypeList == null) || force) {
            _savedTypeList = new ArrayList<ActivityType>();

            try {
                ResultSet rs = DBHelper.ExecuteQuery("SELECT * from LettuceMaster.activitytypes");

                while (rs.next()) {
                    ActivityType newType = ActivityType.CreateFromRecordSet(rs);
                    if (newType != null)
                        _savedTypeList.add(newType);
                }
            } catch (java.sql.SQLException exp) {
                log.log(Level.SEVERE, String.format("error getting activity types - %s", exp.getMessage()));
            }
        }

        return _savedTypeList;
    }

    public static ActivityType CreateFromRecordSet(ResultSet rs) {
        ActivityType newActivity = new ActivityType();
        try {
            newActivity.id = rs.getLong("id");
            newActivity.typename = rs.getString("typename");
            newActivity.icon = rs.getString("icon");

        }
        catch (Exception exp) {
            log.log(Level.SEVERE, String.format("error parsing activity type:  %s", exp.getMessage()));
            newActivity = null;
        }

        return newActivity;
    }
}
