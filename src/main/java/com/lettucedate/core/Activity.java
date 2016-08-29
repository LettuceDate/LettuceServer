package com.lettucedate.core;

import com.google.cloud.sql.jdbc.Statement;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dave on 9/28/2015.
 */
public class Activity {
    private static final Logger log = Logger.getLogger(Activity.class.getName());
    public Long id;
    public String description;
    public int type;
    public String venueid;
    public int duration;
    public transient Long dateid;



    public Boolean Create() {
        Boolean didIt = false;
        Connection connection = DBHelper.GetConnection();

        try {

            int index = 1;

            String statementStr = "INSERT INTO LettuceMaster.activities (";
            String valueStr = "";
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if (description != null) {
                columnNames.add("description");
                values.add("?");
            }

            if (venueid != null) {
                columnNames.add("venueId");
                values.add("?");
            }


            columnNames.add("type");
            values.add("?");
            columnNames.add("duration");
            values.add("?");
            columnNames.add("dateid");
            values.add("?");

            statementStr += StringUtils.join(columnNames, ", ") + ") VALUES ( ";

            statementStr += StringUtils.join(values, ", ") + ")";

            PreparedStatement statement = connection.prepareStatement(statementStr, Statement.RETURN_GENERATED_KEYS);
            // fill in the statement
            if (description != null) {
                statement.setString(index++, description);
            }

            if (venueid != null) {
                statement.setString(index++, venueid);
            }

            statement.setInt(index++, type);
            statement.setInt(index++, duration);
            statement.setLong(index++, dateid);

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                id=rs.getLong(1);
            }
            rs.close();
            statement.close();
            didIt = true;
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
        }


        return didIt;
    }

    public static Activity CreateFromRecordSet(ResultSet rs) {
        Activity newActivity = new Activity();
        try {
            newActivity.dateid = rs.getLong("dateid");
            newActivity.description = rs.getString("description");
            newActivity.duration = rs.getInt("duration");
            newActivity.id = rs.getLong("id");
            newActivity.type = rs.getInt("type");
            newActivity.venueid = rs.getString("venueid");
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return newActivity;
    }

    public static ArrayList<Activity>    GetActivitiesForDate(Long dateid) {
        ArrayList<Activity> activityList = new ArrayList<>();

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT * FROM LettuceMaster.activities WHERE dateid = ?";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, dateid);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Activity newActivity = Activity.CreateFromRecordSet(rs);
                activityList.add(newActivity);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
        }

        return activityList;
    }
}
