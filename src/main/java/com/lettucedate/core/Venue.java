package com.lettucedate.core;

import com.google.cloud.sql.jdbc.Statement;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/23/15.
 */
public class Venue {
    private static final Logger log = Logger.getLogger(Venue.class.getName());

    public long id;
    public String name;
    public String description;
    public String thumbnail;
    public String largeimage;
    public List<ActivityType> activityTypes;

    public Boolean Create() {
        Boolean didIt = false;
        Connection connection = DBHelper.GetConnection();
        try {

            int index = 1;

            String statementStr = "INSERT INTO LettuceMaster.venues (";
            String valueStr = "";
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if (name != null) {
                columnNames.add("name");
                values.add("?");
            }

            if (description != null) {
                columnNames.add("description");
                values.add("?");
            }

            if (thumbnail != null) {
                columnNames.add("thumbnail");
                values.add("?");
            }

            if (largeimage != null) {
                columnNames.add("largeimage");
                values.add("?");
            }


            statementStr += StringUtils.join(columnNames, ", ") + ") VALUES ( ";

            statementStr += StringUtils.join(values, ", ") + ")";

            PreparedStatement statement = connection.prepareStatement(statementStr, Statement.RETURN_GENERATED_KEYS);

            // fill in the statement
            if (name != null) {
                statement.setString(index++, name);
            }

            if (description != null) {
                statement.setString(index++, description);
            }

            if (thumbnail != null) {
                statement.setString(index++, thumbnail);
            }

            if (largeimage != null) {
                statement.setString(index++, largeimage);
            }


            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                id=rs.getLong(1);
            }
            rs.close();
            statement.close();

            // now add the activity types
            for (ActivityType curType : activityTypes) {
                String queryString = "INSERT INTO LettuceMaster.venueactvitymap (venueid, activitytypeid) VALUES (?, ?)";
                PreparedStatement actStatement = connection.prepareStatement(queryString);
                actStatement.setLong(1, id);
                actStatement.setInt(2, curType.id);
                connection.createStatement().executeQuery(queryString);
            }
            didIt = true;
        } catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
        }


        return didIt;
    }
}
