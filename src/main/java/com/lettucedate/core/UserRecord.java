package com.lettucedate.core;

import com.google.api.client.util.Joiner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ultradad on 9/22/15.
 */
public class UserRecord implements BaseDAO {
    public Long id;
    public String nickname;
    public String firstname;
    public String lastname;
    public Date dob;
    public String facebookid;
    public Integer ethnicity;
    public Integer gender;

    public static UserRecord FindByID(Long searchId) {
        UserRecord newUser = null;
        try {
            PreparedStatement statement = DBHelper.PrepareStatement("SELECT * FROM LettuceMaster.users WHERE id=?", false);
            statement.setLong(1, searchId);
            ResultSet theResults = DBHelper.ExecuteQuery(statement);

            if (theResults.next()) {
                newUser = CreateFromRecordSet(theResults);
            }
        }
        catch (Exception exp)
        {
            System.out.println(exp.getMessage());
        }
        return newUser;
    }

    public static UserRecord CreateFromRecordSet(ResultSet rs) {
        UserRecord newUser = new UserRecord();
        try {
            newUser.id = rs.getLong("id");
            newUser.nickname = rs.getString("nickname");
            newUser.firstname = rs.getString("firstname");
            newUser.lastname = rs.getString("lastname");
            newUser.dob = rs.getDate("dob");
            newUser.facebookid = rs.getString("facebookid");
            newUser.ethnicity = rs.getInt("ethnicity");
            newUser.gender = rs.getInt("gender");
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return newUser;
    }

    public static UserRecord FindByFBID(String searchId) {
        UserRecord newUser = null;
        try {
            PreparedStatement statement = DBHelper.PrepareStatement("SELECT * FROM LettuceMaster.users WHERE facebookid=?", false);
            statement.setString(1, searchId);
            ResultSet theResults = DBHelper.ExecuteQuery(statement);

            if (theResults.next()) {
                newUser = CreateFromRecordSet(theResults);
            }
        }
        catch (Exception exp)
        {
            System.out.println(exp.getMessage());
        }
        return newUser;
    }

    public Boolean Create() {
        Boolean didIt = false;
        try {

            int index = 1;

            String statementStr = "INSERT INTO LettuceMaster.users (";
            String valueStr = "";
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if (nickname != null) {
                columnNames.add("nickname");
                values.add("?");
            }

            if (firstname != null) {
                columnNames.add("firstname");
                values.add("?");
            }

            if (lastname != null) {
                columnNames.add("lastname");
                values.add("?");
            }

            if (dob != null) {
                columnNames.add("dob");
                values.add("?");
            }

            if (facebookid != null) {
                columnNames.add("facebookid");
                values.add("?");
            }

            if (ethnicity != null) {
                columnNames.add("ethnicity");
                values.add("?");
            }

            if (gender != null) {
                columnNames.add("gender");
                values.add("?");
            }

            statementStr += StringUtils.join(columnNames, ", ") + ") VALUES ( ";

            statementStr += StringUtils.join(values, ", ") + ")";

            PreparedStatement statement = DBHelper.PrepareStatement(statementStr, true);

            // fill in the statement
            if (nickname != null) {
                statement.setString(index++, nickname);
            }

            if (firstname != null) {
                statement.setString(index++, firstname);
            }

            if (lastname != null) {
                statement.setString(index++, lastname);
            }

            if (dob != null) {
                statement.setDate(index++, new java.sql.Date(dob.getTime()));
            }

            if (facebookid != null) {
                statement.setString(index++, facebookid);
            }

            if (ethnicity != null) {
                statement.setInt(index++, ethnicity);
            }

            if (gender != null) {
                statement.setInt(index++, gender);
            }

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
        }


        return didIt;
    }

    public Boolean Update() {
        Boolean didIt = false;

        return didIt;
    }

    public Boolean Delete() {
        Boolean didIt = false;

        return didIt;
    }


}
