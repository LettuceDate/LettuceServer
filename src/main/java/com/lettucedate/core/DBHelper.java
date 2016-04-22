package com.lettucedate.core;

import com.google.appengine.api.utils.SystemProperty;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/22/15.
 */
public class DBHelper {
    private static final Logger log = Logger.getLogger(DBHelper.class.getName());

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
                    _currentConnection = DriverManager.getConnection(url);
                } else {
                    // Connecting from an external network.
                    Class.forName("com.mysql.jdbc.Driver");
                    //url = "jdbc:mysql://173.194.244.58:3306";
                    //url = "jdbc:mysql://2001:4860:4864:1:47c4:133a:7346:81b7:3306";
                    url = "jdbc:mysql://address=(protocol=tcp)(host=2001:4860:4864:1:47c4:133a:7346:81b7)(port=3306)";

                    _currentConnection = DriverManager.getConnection(url, "davevr", "Love4Runess");
                }
            } catch (ClassNotFoundException exp) {
                System.out.println(exp.getMessage());
            } catch (SQLException sqlexp) {
                System.out.println(sqlexp.getMessage());
            }
        }

        return _currentConnection;
    }

    public static void CloseConnection(Connection conn) {
        try {
            conn.close();
        }
        catch (SQLException exp)
        {
            log.log(Level.SEVERE, "error closing connection: %s", exp.getMessage());
        }
    }

}
