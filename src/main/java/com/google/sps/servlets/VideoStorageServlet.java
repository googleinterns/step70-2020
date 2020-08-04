package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/positive_videos")
public class VideoStorageServlet extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /** Responds with a JSON string containing video data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<String> videos = new ArrayList<>();

    Query query = new Query("Video")
      .addSort("sentiment", SortDirection.DESCENDING)
      .addSort("numSearches", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(24))) {
      Key videoKey = entity.getKey();
      String id = videoKey.getName();
      videos.add(id);
    }

    Gson gson = new Gson();
    String json = gson.toJson(videos);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
