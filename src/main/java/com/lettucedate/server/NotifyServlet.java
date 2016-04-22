package com.lettucedate.server;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.api.Authenticator;
import com.lettucedate.core.BaseNotification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by davidvronay on 4/20/16.
 */
public class NotifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        long currentUserId = Authenticator.CurrentUserId(session);

        if (currentUserId != 0) {

            String countStr = request.getParameter("count");

            if (countStr == null) {
                List<BaseNotification> notifyList = null;

                notifyList = BaseNotification.GetNotificationsForUser(currentUserId);

                Gson gson = new GsonBuilder().create();
                response.setContentType("application/json");
                response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
                PrintWriter out = response.getWriter();
                gson.toJson(notifyList, out);

                out.flush();
                out.close();
            } else {
                // just return counts
                int resultCount = BaseNotification.CountUsersNotifications(currentUserId);

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
