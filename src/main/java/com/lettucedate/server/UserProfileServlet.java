package com.lettucedate.server;

import com.lettucedate.api.Authenticator;
import com.lettucedate.core.DBHelper;
import com.lettucedate.core.UserRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/23/15.
 */
public class UserProfileServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UserProfileServlet.class.getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        long currentUserId = Authenticator.CurrentUserId(session);

        if (currentUserId != 0) {
            String firstNameStr = request.getParameter("firstname");
            String lastNameStr = request.getParameter("lastname");
            String dobStr = request.getParameter("dob");
            String ethnicityStr = request.getParameter("ethnicity");
            String genderStr = request.getParameter("gender");

            if ((firstNameStr != null) || (lastNameStr != null) ||
                    (dobStr != null) || (ethnicityStr != null) || (genderStr != null)) {
                // Update the record
                DBHelper.EnsureConnection();
                UserRecord newUser = new UserRecord();
                newUser.id = currentUserId;
                if (firstNameStr != null)
                    newUser.firstname = firstNameStr;
                if (lastNameStr != null)
                    newUser.lastname = lastNameStr;
                if (ethnicityStr != null)
                    newUser.ethnicity = Integer.parseInt(ethnicityStr);
                if (genderStr != null)
                    newUser.gender = Integer.parseInt(genderStr);
                if (dobStr != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        newUser.dob = formatter.parse(dobStr);
                    } catch (java.text.ParseException exp) {
                        System.out.println(exp.getMessage());
                    }
                }

                newUser.Update();
                DBHelper.ReleaseConnection();
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
