package com.lettucedate.server.admin;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.ActivityType;
import com.lettucedate.core.DBHelper;
import com.lettucedate.core.Venue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ultradad on 9/23/15.
 */
public class AddVenue extends HttpServlet {
    private static final Logger log = Logger.getLogger(AddVenue.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

        List<BlobKey> blobKeys = blobs.get("file");

        if (blobKeys == null || blobKeys.isEmpty()) {
            log.log(Level.WARNING, "Failed to find image data");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0));
            String servingUrl = imagesService.getServingUrl(servingOptions);
            String venueName = request.getParameter("venuename");
            String desc = request.getParameter("venuedesc");

            Venue newVenue = new Venue();
            newVenue.name = venueName;
            newVenue.description = desc;
            newVenue.largeimage = servingUrl;
            newVenue.activityTypes = new ArrayList<>();

            for (ActivityType curType : ActivityType.GetActivityTypes()) {
                String curValue = request.getParameter(Long.toString(curType.id));
                if (curValue != null)
                    newVenue.activityTypes.add(curType);
            }

            // save it
            newVenue.Create();

            // write it to the user
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new GsonBuilder().create();
            gson.toJson(newVenue, out);
            out.flush();
            out.close();


        }
    }


}
