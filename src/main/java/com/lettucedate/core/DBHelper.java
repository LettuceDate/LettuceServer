package com.lettucedate.core;

import com.google.appengine.api.utils.SystemProperty;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

/**
 * Created by ultradad on 9/22/15.
 */
public class DBHelper {

    public static Connection _currentConnection;

    public static Connection GetConnection() {

        if (_currentConnection != null)
            return _currentConnection;
        else {
            String url = null;
            try {
                if (SystemProperty.environment.value() ==
                        SystemProperty.Environment.Value.Production) {
                    // Connecting from App Engine.
                    // Load the class that provides the "jdbc:google:mysql://"
                    // prefix.
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    url = "jdbc:google:mysql://lettuce-1045:lettuce-db-server?user=root";
                } else {
                    // Connecting from an external network.
                    Class.forName("com.mysql.jdbc.Driver");
                    url = "jdbc:mysql://173.194.244.58:3306?user=davevr";
                }


                _currentConnection = DriverManager.getConnection(url);

                }
                catch (ClassNotFoundException exp) {
                    System.out.println(exp.getMessage());
                }
                catch (SQLException sqlexp) {
                    System.out.println(sqlexp.getMessage());

            }

            return _currentConnection;
        }
    }

    public static void EnsureConnection() {
        GetConnection();
    }

    public static void ReleaseConnection() {
        if (_currentConnection != null) {
            try {
                _currentConnection.close();
                _currentConnection = null;
            }
            catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        }
    }

    public static PreparedStatement PrepareStatement(String theStatement, Boolean returnKeys) {
        PreparedStatement statement = null;

        try {
            if (returnKeys)
                statement = GetConnection().prepareStatement(theStatement, Statement.RETURN_GENERATED_KEYS);
            else
                statement = GetConnection().prepareStatement(theStatement);

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return statement;
    }

    public static ResultSet ExecuteQuery(String theQuery) {
        ResultSet theResult = null;
        try {
            theResult = GetConnection().createStatement().executeQuery(theQuery);
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return theResult;
    }

    public static ResultSet ExecuteQuery(PreparedStatement theStatement) {
        ResultSet theResult = null;
        try {
            theResult = theStatement.executeQuery();
        }
        catch (Exception exp) {
            System.out.println(exp.getMessage());
        }

        return theResult;
    }

}
