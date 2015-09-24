package com.lettucedate.core;

import com.google.api.client.util.Joiner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
        try {

            int index = 1;

            String statementStr = "UPDATE LettuceMaster.users SET ";
            String valueStr = "";
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if (firstname != null) {
                columnNames.add("firstname = ?");
            }

            if (lastname != null) {
                columnNames.add("lastname = ?");
            }

            if (dob != null) {
                columnNames.add("dob = ?");
            }

            if (ethnicity != null) {
                columnNames.add("ethnicity = ?");
            }

            if (gender != null) {
                columnNames.add("gender = ?");
            }

            statementStr += StringUtils.join(columnNames, ", ") + " WHERE id = ?";

            PreparedStatement statement = DBHelper.PrepareStatement(statementStr, false);

            // fill in the statement
            if (firstname != null) {
                statement.setString(index++, firstname);
            }

            if (lastname != null) {
                statement.setString(index++, lastname);
            }

            if (dob != null) {
                statement.setDate(index++, new java.sql.Date(dob.getTime()));
            }

            if (ethnicity != null) {
                statement.setInt(index++, ethnicity);
            }

            if (gender != null) {
                statement.setInt(index++, gender);
            }

            // set the id
            statement.setLong(index++, id);

            statement.executeUpdate();

            statement.close();
            didIt = true;
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }


        return didIt;
    }

    public Boolean Delete() {
        Boolean didIt = false;

        return didIt;
    }

    public static UserRecord CreateFromFacebook(FacebookUser fbUser) {
        UserRecord newUser = new UserRecord();
        newUser.facebookid = fbUser.id;
        newUser.nickname = fbUser.name;

        // now the optional ones
        if (fbUser.first_name != null)
            newUser.firstname = fbUser.first_name;

        if (fbUser.last_name != null)
            newUser.lastname = fbUser.last_name;

        if (fbUser.birthday != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            try {
                newUser.dob = formatter.parse(fbUser.birthday);
            }
            catch (java.text.ParseException exp) {
                System.out.println(exp.getMessage());
            }
        }

        if (fbUser.gender != null) {
            try {
                String queryString = "SELECT id FROM LettuceMaster.genders WHERE typename = ?";
                PreparedStatement statement = DBHelper.PrepareStatement(queryString, false);
                statement.setString(1, fbUser.gender);
                ResultSet rs = DBHelper.ExecuteQuery(statement);
                if (rs.next()) {
                    newUser.gender = rs.getInt("id");
                }
            } catch (java.sql.SQLException exp) {
                System.out.println(exp.getMessage());
            }
        }

        return newUser;

    }

    public Boolean UpdateFromFacebook(FacebookUser fbUser) {
        Boolean madeChange = false;

        if (fbUser.first_name != null) {
            if ((firstname == null) || (fbUser.first_name.compareTo(firstname) != 0)) {
                firstname = fbUser.first_name;
                madeChange = true;
            }
        }

        if (fbUser.last_name != null) {
            if ((lastname == null) || (fbUser.last_name.compareTo(lastname) != 0)) {
                lastname = fbUser.last_name;
                madeChange = true;
            }
        }

        if (fbUser.birthday != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date dobDate = formatter.parse(fbUser.birthday);
                if ((dob == null) || (dobDate.compareTo(dob) != 0)) {
                    dob = dobDate;
                    madeChange = true;
                }
            }
            catch (java.text.ParseException exp) {
                System.out.println(exp.getMessage());
            }
        }

        if (fbUser.gender != null) {
            if (fbUser.gender != null) {
                try {
                    String queryString = "SELECT id FROM LettuceMaster.genders WHERE typename = ?";
                    PreparedStatement statement = DBHelper.PrepareStatement(queryString, false);
                    statement.setString(1, fbUser.gender);
                    ResultSet rs = DBHelper.ExecuteQuery(statement);
                    if (rs.next()) {
                        int theGender = rs.getInt("id");
                        if ((gender == null) || (theGender != gender)) {
                            gender = theGender;
                            madeChange = true;
                        }
                    }
                } catch (java.sql.SQLException exp) {
                    System.out.println(exp.getMessage());
                }
            }

        }

        if (madeChange) {
            Update();
        }


        return madeChange;
    }



}
