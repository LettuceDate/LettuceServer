package com.lettucedate.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Service;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.scribe.builder.api.DefaultApi10a;

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
    public class YelpAPI {

        private static final String API_HOST = "api.yelp.com";
        private static final String DEFAULT_TERM = "dinner";
        private static final String DEFAULT_LOCATION = "San Francisco, CA";
        private static final int SEARCH_LIMIT = 3;
        private static final String SEARCH_PATH = "/v2/search";
        private static final String BUSINESS_PATH = "/v2/business";

        /*
         * Update OAuth credentials below from the Yelp Developers API site:
         * http://www.yelp.com/developers/getting_started/api_access
         */


        OAuthService service;
        Token accessToken;

        /**
         * Setup the Yelp API OAuth credentials.
         *
         * @param consumerKey Consumer key
         * @param consumerSecret Consumer secret
         * @param token Token
         * @param tokenSecret Token secret
         */
        public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
            ServiceBuilder builder = new ServiceBuilder();
            TwoStepOAuth api = new TwoStepOAuth();
            builder.api = api;
            builder.apiKey(consumerKey);
            builder.apiSecret(consumerSecret);
            service = builder.build();

                    //new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                     //       .apiSecret(consumerSecret).build();
            this.accessToken = new Token(token, tokenSecret);
        }

        /**
         * Creates and sends a request to the Search API by term and location.
         * <p>
         * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
         * for more info.
         *
         * @param term <tt>String</tt> of the search term to be queried
         * @param location <tt>String</tt> of the location
         * @return <tt>String</tt> JSON Response
         */
        public String searchForBusinessesByLocation(String term, String location) {
            OAuthRequest request = createOAuthRequest(SEARCH_PATH);
            request.addQuerystringParameter("term", term);
            request.addQuerystringParameter("location", location);
            request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
            return sendRequestAndGetResponse(request);
        }

        /**
         * Creates and sends a request to the Business API by business ID.
         * <p>
         * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
         * for more info.
         *
         * @param businessID <tt>String</tt> business ID of the requested business
         * @return <tt>String</tt> JSON Response
         */
        public String searchByBusinessId(String businessID) {
            OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
            return sendRequestAndGetResponse(request);
        }

        /**
         * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
         *
         * @param path API endpoint to be queried
         * @return <tt>OAuthRequest</tt>
         */
        private OAuthRequest createOAuthRequest(String path) {
            OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
            return request;
        }

        /**
         * Sends an {@link OAuthRequest} and returns the {@link Response} body.
         *
         * @param request {@link OAuthRequest} corresponding to the API request
         * @return <tt>String</tt> body of API response
         */
        private String sendRequestAndGetResponse(OAuthRequest request) {
            System.out.println("Querying " + request.getCompleteUrl() + " ...");
            this.service.signRequest(this.accessToken, request);
            Response response = request.send();
            return response.getBody();
        }
    }

    public class TwoStepOAuth extends DefaultApi10a {

        @Override
        public String getAccessTokenEndpoint() {
            return null;
        }

        @Override
        public String getAuthorizationUrl(Token arg0) {
            return null;
        }

        @Override
        public String getRequestTokenEndpoint() {
            return null;
        }
    }
}

