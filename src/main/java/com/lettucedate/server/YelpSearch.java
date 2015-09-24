package com.lettucedate.server;

import com.lettucedate.yelp.YelpAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



/**
 * Created by ultradad on 9/23/15.
 */
public class YelpSearch extends HttpServlet {
    private static final String CONSUMER_KEY = "uS8BH_rVcaHamIK5wOgOwg";
    private static final String CONSUMER_SECRET = "y42vx0LiN8-5s1yLHbOyuigcWd0";
    private static final String TOKEN = "6dT8SAnpfrKQXW2QjhdUrXmB122J7i-R";
    private static final String TOKEN_SECRET = "QXeXhsR2isU8Z9VUjsiDFkCuWvk";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String searchParam = request.getParameter("search");

        if (searchParam == null)
            searchParam = "pizza";

        YelpAPI yelp = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        String resultSet = yelp.searchForBusinessesByLocation(searchParam, "Los Angeles, CA");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(resultSet);
        out.flush();
        out.close();
    }

    // yelp stuff...



}


