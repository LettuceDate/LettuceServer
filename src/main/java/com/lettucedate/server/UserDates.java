package com.lettucedate.server;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.TestDate;
import com.google.api.client.http.HttpStatusCodes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davevr on 9/21/15.
 */
public class UserDates extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = null;
        List<TestDate> dateList = new ArrayList<TestDate>();

        try {
            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                // Connecting from App Engine.
                // Load the class that provides the "jdbc:google:mysql://"
                // prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
                url =
                        "jdbc:google:mysql://lettuce-1045:lettuce-db-server?user=root";
            } else {
                // Connecting from an external network.
                Class.forName("com.mysql.jdbc.Driver");
                url = "jdbc:mysql://173.194.244.58:3306?user=davevr";
            }


            Connection conn = DriverManager.getConnection(url);
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM LettuceMaster.DateTest");



            while (rs.next()) {
                TestDate newDate = new TestDate();
                newDate.Id = rs.getLong("id");
                newDate.userId = rs.getLong("userid");
                newDate.title = rs.getString("title");
                newDate.description = rs.getString("description");
                newDate.startTime = rs.getDate("starttime");
                newDate.endTime = rs.getDate("endtime");
                dateList.add(newDate);
            }
        }
        catch (ClassNotFoundException exp) {
            System.out.println(exp.getMessage());
        }
        catch (SQLException sqlexp) {
            System.out.println(sqlexp.getMessage());

        }

        // write the result
        response.setContentType("application/json");
        response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        gson.toJson(dateList, out);
        out.flush();
        out.close();


    }
}
