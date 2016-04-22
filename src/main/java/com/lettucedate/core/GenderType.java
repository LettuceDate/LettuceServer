package com.lettucedate.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 10/2/15.
 */
public class GenderType {
    private static final Logger log = Logger.getLogger(GenderType.class.getName());

    public int id;
    public String typename;
    public String icon;

    private static List<GenderType> _savedTypeList = null;

    public static List<GenderType>  GetGenderTypes() {
        return GetGenderTypes(false);
    }
    public static List<GenderType>  GetGenderTypes(Boolean force) {

        if ((_savedTypeList == null) || force) {
            _savedTypeList = new ArrayList<>();

            Connection conn = DBHelper.GetConnection();
            try {
                ResultSet rs = conn.createStatement().executeQuery("SELECT * from LettuceMaster.genders");

                while (rs.next()) {
                    GenderType newType = GenderType.CreateFromRecordSet(rs);
                    if (newType != null)
                        _savedTypeList.add(newType);
                }
            } catch (java.sql.SQLException exp) {
                log.log(Level.SEVERE, String.format("error getting gender types - %s", exp.getMessage()));
            } finally {
                DBHelper.CloseConnection(conn);
            }
        }

        return _savedTypeList;
    }

    public static GenderType CreateFromRecordSet(ResultSet rs) {
        GenderType newActivity = new GenderType();
        try {
            newActivity.id = rs.getInt("id");
            newActivity.typename = rs.getString("typename");
            newActivity.icon = rs.getString("icon");

        }
        catch (Exception exp) {
            log.log(Level.SEVERE, String.format("error parsing gender type:  %s", exp.getMessage()));
            newActivity = null;
        }

        return newActivity;
    }
}
