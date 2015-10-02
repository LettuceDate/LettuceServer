package com.lettucedate.core;

import com.google.appengine.repackaged.org.joda.time.format.DateTimeParser;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dave on 9/28/2015.
 */
public class BaseDate {
    private static final Logger log = Logger.getLogger(BaseDate.class.getName());
    public Long id;
    public String title;
    public String startTimeStr;
    public transient Date starttime;  // transient to make it not serialize to JSON
    public String description;
    public int paymentstyle;
    public Long proposerid;
    public Boolean active;
    public String selfie;
    public Boolean booked;
    public ArrayList<Activity> activities;

    public BaseDate()
    {
        active = false;
    }

    public void InitFromData()
    {
        if (startTimeStr != null) {
            try {
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                starttime = df1.parse(startTimeStr);
            }
            catch (java.text.ParseException exp) {
                log.log(Level.WARNING, "Invalid Date: " + exp.getMessage());
                starttime = null;
            }
        }
    }

    public void PrepForSave()
    {
        if (starttime != null) {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            startTimeStr = df1.format(starttime);
        }
    }

    public Boolean Create() {
        Boolean didIt = false;
        Connection connection = DBHelper.GetConnection();
        try {

            int index = 1;

            String statementStr = "INSERT INTO LettuceMaster.dates (";
            String valueStr = "";
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if (title != null) {
                columnNames.add("title");
                values.add("?");
            }

            if (starttime != null) {
                columnNames.add("starttime");
                values.add("?");
            }

            if (description != null) {
                columnNames.add("description");
                values.add("?");
            }

            if (selfie != null) {
                columnNames.add("selfie");
                values.add("?");
            }

            columnNames.add("paymentstyle");
            values.add("?");
            columnNames.add("proposerid");
            values.add("?");
            columnNames.add("active");
            values.add("?");

            statementStr += StringUtils.join(columnNames, ", ") + ") VALUES ( ";

            statementStr += StringUtils.join(values, ", ") + ")";

            PreparedStatement statement = connection.prepareStatement(statementStr, Statement.RETURN_GENERATED_KEYS);

            // fill in the statement
            if (title != null) {
                statement.setString(index++, title);
            }

            if (starttime != null) {
                statement.setDate(index++, new java.sql.Date(starttime.getTime()));
            }

            if (description != null) {
                statement.setString(index++, description);
            }

            if (selfie != null) {
                statement.setString(index++, selfie);
            }

            statement.setInt(index++, paymentstyle);
            statement.setLong(index++, proposerid);
            statement.setInt(index++, active ? 1 : 0);


            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                id=rs.getLong(1);
            }
            rs.close();
            statement.close();

            // now save the activities
            if (activities != null){
                for (Activity curActivity : activities) {
                    curActivity.dateid = id;
                    curActivity.Create();
                }
            }
            didIt = true;
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        } finally {
            DBHelper.CloseConnection(connection);
        }


        return didIt;
    }

    public void InitFromResultSet(ResultSet rs) {
        try {
            this.id = rs.getLong("id");
            this.title = rs.getString("title");
            this.starttime = rs.getDate("starttime");
            this.description = rs.getString("description");
            this.paymentstyle = rs.getInt("paymentstyle");
            this.proposerid = rs.getLong("proposerid");
            this.selfie = rs.getString("selfie");
            this.active = rs.getInt("active") == 1;
            this.booked = rs.getInt("booked") == 1;

            this.activities = Activity.GetActivitiesForDate(this.id);
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

    }

    public static BaseDate CreateFromRecordSet(ResultSet rs) {
        BaseDate newDate = new BaseDate();
        newDate.InitFromResultSet(rs);

        return newDate;
    }

    public static List<BaseDate> DoDateQuery(PreparedStatement statement) {
        List<BaseDate> dateList = new ArrayList<>();

        try {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                BaseDate newDate = CreateFromRecordSet(rs);
                dateList.add(newDate);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, "Error getting dates - " + exp.getMessage());
        }

        return dateList;

    }



    public static List<BaseDate>    GetBookedDatesForUser(long userId) {
        List<BaseDate> resultList = null;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT * FROM LettuceMaster.dates WHERE proposerid = ? and booked = 1";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            resultList = DoDateQuery(statement);
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
            resultList = new ArrayList<>();
        } finally {
            DBHelper.CloseConnection(connection);
        }

        return resultList;
    }

    public static List<BaseDate>    GetUsersOwnDates(long userId) {
        List<BaseDate>  resultList = null;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT * FROM LettuceMaster.dates WHERE proposerid = ?";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            resultList = DoDateQuery(statement);
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
            resultList = new ArrayList<>();
        } finally {
            DBHelper.CloseConnection(connection);
        }

        return resultList;
    }



    public static int CountBookedDatesForUser(long userId) {
        int resultCount = 0;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT COUNT(id) FROM LettuceMaster.dates WHERE proposerid = ? and booked = 1";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                resultCount = rs.getInt(1);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
        } finally {
            DBHelper.CloseConnection(connection);
        }

        return resultCount;
    }

    public static int CountUsersOwnDates(long userId) {
        int resultCount = 0;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT COUNT(id) FROM LettuceMaster.dates WHERE proposerid = ?";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                resultCount = rs.getInt(1);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
        } finally {
            DBHelper.CloseConnection(connection);
        }

        return resultCount;
    }

}
