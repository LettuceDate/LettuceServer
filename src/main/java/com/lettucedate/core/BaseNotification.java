package com.lettucedate.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 4/20/16.
 */
public class BaseNotification {
    private static final Logger log = Logger.getLogger(BaseNotification.class.getName());

    public Long id;
    public Date date;
    public String	detail;
    public Integer		type;
    public Boolean		read;


    public void InitFromResultSet(ResultSet rs) {
        try {
            this.id = rs.getLong("id");
            this.date = rs.getDate("date");
            this.detail = rs.getString("detail");
            this.type = rs.getInt("type");
            this.read = rs.getBoolean("read");
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

    }

    public static BaseNotification CreateFromRecordSet(ResultSet rs) {
        BaseNotification newDate = new BaseNotification();
        newDate.InitFromResultSet(rs);

        return newDate;
    }

    public static List<BaseNotification> DoNotificationQuery(PreparedStatement statement) {
        List<BaseNotification> resultList = new ArrayList<>();

        try {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                BaseNotification newNotify = CreateFromRecordSet(rs);
                resultList.add(newNotify);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, "Error getting notifications - " + exp.getMessage());
        }

        return resultList;

    }

    public static List<BaseNotification> GetNotificationsForUser(long userId) {
        List<BaseNotification>  resultList = null;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT * FROM LettuceMaster.notifications WHERE targetUserId = ?";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            resultList = DoNotificationQuery(statement);
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
            resultList = new ArrayList<>();
        } finally {
            DBHelper.ReleaseConnection(connection);
        }

        return resultList;
    }

    public static int CountUsersNotifications(long userId) {
        int resultCount = 0;

        Connection connection = DBHelper.GetConnection();
        try {
            String statementStr = "SELECT COUNT(id) FROM LettuceMaster.notifications WHERE targetUserId = ?";
            PreparedStatement statement = connection.prepareStatement(statementStr);
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                resultCount = rs.getInt(1);
            }
        } catch (SQLException exp) {
            log.log(Level.SEVERE, exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
        }

        return resultCount;
    }

}
