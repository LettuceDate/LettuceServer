package com.lettucedate.server.admin;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.DBHelper;
import com.lettucedate.core.EthnicityType;
import com.lettucedate.core.GenderType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by ultradad on 10/2/15.
 */
public class EthnicityNames extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<EthnicityType> ethnicityTypes = EthnicityType.GetEthnictyTypes();

        // clean up and return
        response.setContentType("application/json");
        response.setStatus(HttpStatusCodes.STATUS_CODE_OK);
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        gson.toJson(ethnicityTypes, out);
        out.flush();
        out.close();
    }
}
