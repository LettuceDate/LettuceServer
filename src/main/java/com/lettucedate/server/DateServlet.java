package com.lettucedate.server;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.api.Authenticator;
import com.lettucedate.core.BaseDate;
import com.lettucedate.core.DBHelper;
import com.lettucedate.core.MatchingDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Created by Dave on 9/28/2015.
 */
public class DateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        long currentUserId = Authenticator.CurrentUserId(session);

        if (currentUserId != 0) {
            String dateStr = request.getParameter("date");

            Gson gson = new GsonBuilder().create();
            BaseDate sentDate = gson.fromJson(dateStr, BaseDate.class);
            sentDate.InitFromData();
            sentDate.proposerid = currentUserId;

            sentDate.Create();

            DBHelper.ReleaseConnection();
            BaseDate newDate = sentDate;

            response.setContentType("application/json");
            response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
            PrintWriter out = response.getWriter();
            gson.toJson(newDate, out);
            out.flush();
            out.close();

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        long currentUserId = Authenticator.CurrentUserId(session);

        if (currentUserId != 0) {
            String matches = request.getParameter("matches");
            String booked = request.getParameter("booked");
            String countStr = request.getParameter("count");

            if (countStr == null) {
                List<BaseDate> dateList = null;
                List<MatchingDate> matchList = null;

                if (matches != null) {
                    // returning matching dates
                    matchList = MatchingDate.GetDatesForUser(currentUserId);

                } else if (booked != null) {
                    // return booked dates
                    dateList = BaseDate.GetBookedDatesForUser(currentUserId);

                } else {
                    // return my own dates
                    dateList = BaseDate.GetUsersOwnDates(currentUserId);
                }


                DBHelper.ReleaseConnection();
                Gson gson = new GsonBuilder().create();
                response.setContentType("application/json");
                response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
                PrintWriter out = response.getWriter();
                if (matchList != null)
                    gson.toJson(matchList, out);
                else
                    gson.toJson(dateList, out);

                out.flush();
                out.close();
            } else {
                // just return counts
                int resultCount = 0;

                if (matches != null) {
                    // returning matching dates
                    resultCount = BaseDate.CountDatesForUser(currentUserId);

                } else if (booked != null) {
                    // return booked dates
                    resultCount = BaseDate.CountBookedDatesForUser(currentUserId);

                } else {
                    // return my own dates
                    resultCount = BaseDate.CountUsersOwnDates(currentUserId);
                }
                DBHelper.ReleaseConnection();
                Gson gson = new GsonBuilder().create();
                response.setContentType("application/json");
                response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
                PrintWriter out = response.getWriter();
                gson.toJson(resultCount, out);
                out.flush();
                out.close();
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
