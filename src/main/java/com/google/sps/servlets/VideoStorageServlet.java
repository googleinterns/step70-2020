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
import com.google.sps.data.VideoAnalysis;

@WebServlet("/positive_videos")
public class VideoStorageServlet extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /** Responds with a JSON string containing video data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<VideoAnalysis> videos = new ArrayList<>();

    Query query = new Query("Video")
      .addSort("sentiment", SortDirection.DESCENDING)
      .addSort("numSearches", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(24))) {
      Key videoKey = entity.getKey();
      double scoreDouble = (double) entity.getProperty("sentiment");
      String scoreStr = Double.toString(scoreDouble);
      VideoAnalysis videoAnalysis = new VideoAnalysis.Builder()
        .setScore(Float.parseFloat(scoreStr))
        .setVideoId(videoKey.getName())
        .build();
      videos.add(videoAnalysis);
    }

    Gson gson = new Gson();
    String json = gson.toJson(videos);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
