package com.lettucedate.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.cloud.sql.jdbc.Statement;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ultradad on 9/22/15.
 */
public class UserRecord implements BaseDAO {
    public Long id;
    public String nickname;
    public String firstname;
    public String lastname;
    public String fullname;
    public Date dob;
    public String facebookid;
    public Integer ethnicity;
    public Integer gender;
    public String description;
    public String city;
    public String state;
    public String zipcode;
    public String locationname;

    public static UserRecord FindByID(Long searchId) {
        UserRecord newUser = null;
        Connection connection = DBHelper.GetConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LettuceMaster.users WHERE id=?");
            statement.setLong(1, searchId);
            ResultSet theResults = statement.executeQuery();

            if (theResults.next()) {
                newUser = CreateFromRecordSet(theResults);
            }
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }  finally {
            DBHelper.ReleaseConnection(connection);
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
            newUser.fullname = rs.getString("fullname");
            newUser.description = rs.getString("description");
            newUser.city = rs.getString("city");
            newUser.state = rs.getString("state");
            newUser.zipcode = rs.getString("zipcode");
            newUser.zipcode = rs.getString("locationname");

        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return newUser;
    }

    public static UserRecord FindByFBID(String searchId) {
        UserRecord newUser = null;
        Connection connection = DBHelper.GetConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM LettuceMaster.users WHERE facebookid=?");
            statement.setString(1, searchId);
            ResultSet theResults = statement.executeQuery();
            if (theResults.next()) {
                newUser = CreateFromRecordSet(theResults);
            }
        }
        catch (Exception exp)
        {
            System.out.println(exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
        }

        return newUser;
    }

    public Boolean Create() {
        Boolean didIt = false;
        Connection connection = DBHelper.GetConnection();
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

            if (fullname != null) {
                columnNames.add("fullname");
                values.add("?");
            }

            if (fullname != null) {
                columnNames.add("city");
                values.add("?");
            }

            if (fullname != null) {
                columnNames.add("state");
                values.add("?");
            }

            if (fullname != null) {
                columnNames.add("zipcode");
                values.add("?");
            }

            if (fullname != null) {
                columnNames.add("locationname");
                values.add("?");
            }

            if (description != null) {
                columnNames.add("description");
                values.add("?");
            }



            statementStr += StringUtils.join(columnNames, ", ") + ") VALUES ( ";

            statementStr += StringUtils.join(values, ", ") + ")";

            PreparedStatement statement = connection.prepareStatement(statementStr, Statement.RETURN_GENERATED_KEYS);

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

            if (fullname != null) {
                statement.setString(index++, fullname);
            }

            if (city != null) {
                statement.setString(index++, city);
            }

            if (state != null) {
                statement.setString(index++, state);
            }

            if (zipcode != null) {
                statement.setString(index++, zipcode);
            }

            if (locationname != null) {
                statement.setString(index++, locationname);
            }

            if (description != null) {
                statement.setString(index++, description);
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
        } finally {
            DBHelper.ReleaseConnection(connection);
        }


        return didIt;
    }


    public Boolean Update() {
        Boolean didIt = false;
        Connection connection = DBHelper.GetConnection();
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

            if (fullname != null) {
                columnNames.add("fullname = ?");
            }

            if (description != null) {
                columnNames.add("description = ?");
            }

            if (city != null) {
                columnNames.add("city = ?");
            }

            if (state != null) {
                columnNames.add("state = ?");
            }

            if (zipcode != null) {
                columnNames.add("zipcode = ?");
            }

            if (locationname != null) {
                columnNames.add("locationname = ?");
            }

            statementStr += StringUtils.join(columnNames, ", ") + " WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(statementStr);
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

            if (fullname != null) {
                statement.setString(index++, fullname);
            }

            if (description != null) {
                statement.setString(index++, description);
            }

            if (city != null) {
                statement.setString(index++, city);
            }

            if (state != null) {
                statement.setString(index++, state);
            }

            if (zipcode != null) {
                statement.setString(index++, zipcode);
            }

            if (locationname != null) {
                statement.setString(index++, locationname);
            }

            // set the id
            statement.setLong(index++, id);

            statement.executeUpdate();

            statement.close();
            didIt = true;
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        } finally {
            DBHelper.ReleaseConnection(connection);
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
            Connection connection = DBHelper.GetConnection();
            try {
                String queryString = "SELECT id FROM LettuceMaster.genders WHERE typename = ?";
                PreparedStatement statement = connection.prepareStatement(queryString);
                statement.setString(1, fbUser.gender);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    newUser.gender = rs.getInt("id");
                }
            } catch (java.sql.SQLException exp) {
                System.out.println(exp.getMessage());
            } finally {
                DBHelper.ReleaseConnection(connection);
            }
        }

        if (fbUser.location != null) {
            if (fbUser.location.city != null)
                newUser.city = fbUser.location.city;
            if (fbUser.location.state != null)
                newUser.state = fbUser.location.state    ;
            if (fbUser.location.zip != null)
                newUser.zipcode = fbUser.location.zip;
            if (fbUser.location.name != null)
                newUser.locationname = fbUser.location.name;

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

        if (fbUser.name != null) {
            if ((fullname == null) || (fbUser.name.compareTo(fullname) != 0)) {
                fullname = fbUser.name;
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
                for (GenderType curGender : GenderType.GetGenderTypes()) {
                    if (curGender.typename.compareTo(fbUser.gender) == 0) {
                        if ((gender == null) || (curGender.id != gender)) {
                            gender = curGender.id;
                            madeChange = true;
                        }
                    }
                }
            }
        }

        if (fbUser.location != null) {
            if (fbUser.location.city != null) {
                if ((city == null) || (fbUser.location.city.compareTo(city) != 0)) {
                    city = fbUser.location.city;
                    madeChange = true;
                }
            }
            if (fbUser.location.state != null) {
                if ((state == null) || (fbUser.location.state.compareTo(state) != 0)) {
                    state = fbUser.location.state;
                    madeChange = true;
                }
            }
            if (fbUser.location.zip != null) {
                if ((zipcode == null) || (fbUser.location.zip.compareTo(zipcode) != 0)) {
                    zipcode = fbUser.location.zip;
                    madeChange = true;
                }
            }
            if (fbUser.location.name != null) {
                if ((locationname == null) || (fbUser.location.name.compareTo(locationname) != 0)) {
                    locationname = fbUser.location.name;
                    madeChange = true;
                }
            }
        }

        if (madeChange) {
            Update();
        }


        return madeChange;
    }
}
