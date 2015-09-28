package com.lettucedate.server;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.api.Authenticator;
import com.lettucedate.core.BaseDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


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

    }
}
