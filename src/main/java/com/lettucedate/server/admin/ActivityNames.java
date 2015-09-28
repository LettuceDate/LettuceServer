package com.lettucedate.server.admin;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.ActivityType;
import com.lettucedate.core.DBHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Dave on 9/27/2015.
 */
public class ActivityNames extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ActivityType> activityTypes = ActivityType.GetActivityTypes();

        // clean up and return
        DBHelper.ReleaseConnection();
        response.setContentType("application/json");
        response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        gson.toJson(activityTypes, out);
        out.flush();
        out.close();
    }
}
