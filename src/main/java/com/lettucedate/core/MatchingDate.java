package com.lettucedate.core;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try {
            CallableStatement statement = DBHelper.GetConnection().prepareCall("{call GetUserMatchingDates(?)}");
            statement.setLong(1, userId);
            resultList = DoMatchingDateQuery(statement);
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
            resultList = new ArrayList<>();
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
