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
public class EthnicityType {
    private static final Logger log = Logger.getLogger(EthnicityType.class.getName());

    public int id;
    public String typename;
    public String icon;

    private static List<EthnicityType> _savedTypeList = null;

    public static List<EthnicityType>  GetEthnictyTypes() {
        return GetEthnictyTypes(false);
    }
    public static List<EthnicityType>  GetEthnictyTypes(Boolean force) {

        if ((_savedTypeList == null) || force) {
            _savedTypeList = new ArrayList<>();
            Connection connection = DBHelper.GetConnection();
            try {
                ResultSet rs = connection.createStatement().executeQuery("SELECT * from LettuceMaster.ethnicities");

                while (rs.next()) {
                    EthnicityType newType = EthnicityType.CreateFromRecordSet(rs);
                    if (newType != null)
                        _savedTypeList.add(newType);
                }
            } catch (java.sql.SQLException exp) {
                log.log(Level.SEVERE, String.format("error getting ethnicity types - %s", exp.getMessage()));
            } finally {
                DBHelper.ReleaseConnection(connection);
            }
        }

        return _savedTypeList;
    }

    public static EthnicityType CreateFromRecordSet(ResultSet rs) {
        EthnicityType newActivity = new EthnicityType();
        try {
            newActivity.id = rs.getInt("id");
            newActivity.typename = rs.getString("typename");
            newActivity.icon = rs.getString("icon");

        }
        catch (Exception exp) {
            log.log(Level.SEVERE, String.format("error parsing ethnicity type:  %s", exp.getMessage()));
            newActivity = null;
        }

        return newActivity;
    }
}
