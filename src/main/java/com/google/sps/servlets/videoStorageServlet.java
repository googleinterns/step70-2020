package com.google.sps.servlets;

import com.google.sps.data.Video;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Transaction;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class videoStorageServlet extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /** Responds with a JSON string containing video data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<String> videos = new ArrayList<>();

    Query query = new Query("Video") {
      Order = { { "sentiment", PropertyOrder.Types.Direction.Descending },
                { "numSearches", PropertyOrder.Types.Direction.Descending } }
    };
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      Key videoKey = entity.getKey();
      String id = videoKey.getName();
      float sentiment = (float) entity.getProperty("sentiment");
      videos.add(id);
    }

    Gson gson = new Gson();
    String json = gson.toJson(videos);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
