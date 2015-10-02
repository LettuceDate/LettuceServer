package com.lettucedate.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davevr on 10/1/15.
 */
public class MatchingDate extends BaseDate {
    private static final Logger log = Logger.getLogger(BaseDate.class.getName());

    public Boolean pinned;
    public Boolean applied;
    public int status;

    public MatchingDate() {
        pinned = false;
        applied = false;
    }

    public static List<MatchingDate> GetDatesForUser(long userId) {
        List<MatchingDate>  resultList = null;
        Connection connection = DBHelper.GetConnection();
        try {
            CallableStatement statement = connection.prepareCall("{call LettuceMaster.GetUserMatchingDates(?)}");
            statement.setLong(1, userId);
            resultList = DoMatchingDateQuery(statement);
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
            resultList = new ArrayList<>();
        } finally {
            DBHelper.CloseConnection(connection);
        }

        return resultList;
    }

    public static List<MatchingDate> DoMatchingDateQuery(PreparedStatement statement) {
        List<MatchingDate> dateList = new ArrayList<>();

        try {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                MatchingDate newDate = CreateFromRecordSet(rs);
                dateList.add(newDate);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, "Error getting dates - " + exp.getMessage());
        }

        return dateList;

    }

    public static int CountDatesForUser(long userId) {
        int resultCount = 0;
        Connection connection = DBHelper.GetConnection();
        try {
            CallableStatement statement = connection.prepareCall("{call LettuceMaster.CountUserMatchingDates(?)}");
            statement.setLong(1, userId);
            ResultSet rs  = statement.executeQuery();
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

    public void InitFromResultSet(ResultSet rs) {
        super.InitFromResultSet(rs);
        try {
            this.pinned = rs.getInt("pinned") == 1;
            this.applied = rs.getInt("applied") == 1;
            this.status = rs.getInt("status");
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

    }

    public static MatchingDate CreateFromRecordSet(ResultSet rs) {
        MatchingDate newDate = new MatchingDate();
        newDate.InitFromResultSet(rs);

        return newDate;
    }
}
