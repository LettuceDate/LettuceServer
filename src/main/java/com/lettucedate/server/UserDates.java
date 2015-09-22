package com.lettucedate.server;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.DBHelper;
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
        List<TestDate> dateList = new ArrayList<TestDate>();

        try {
            Connection conn = DBHelper.GetConnection();
            ResultSet rs = DBHelper.ExecuteQuery("SELECT * FROM LettuceMaster.DateTest");



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
        catch (SQLException sqlexp) {
            System.out.println(sqlexp.getMessage());

        }
        finally {
            DBHelper.ReleaseConnection();
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
