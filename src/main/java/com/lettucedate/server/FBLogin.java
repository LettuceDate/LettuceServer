package com.lettucedate.server;

import com.google.appengine.repackaged.com.google.api.client.http.HttpStatusCodes;
import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.api.Authenticator;
import com.lettucedate.core.DBHelper;
import com.lettucedate.core.UserRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/22/15.
 */
public class FBLogin extends HttpServlet {
    private static final Logger log = Logger.getLogger(FBLogin.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String fbId = request.getParameter("id");
        String authToken = request.getParameter("token");

        if (Authenticator.AuthenticateFBUser(session, fbId, authToken)) {
            // yeah!
            long userId = Authenticator.CurrentUserId(session);
            UserRecord newUser = UserRecord.FindByID(userId);
            log.log(Level.INFO, String.format("Logging in as %s", newUser.nickname));
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new GsonBuilder().create();
            gson.toJson(newUser, out);
            out.flush();
            out.close();

        } else {
            // no luck
            log.log(Level.WARNING, "FB login Authorization Failed");
            response.setStatus(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
        }
    }
}
