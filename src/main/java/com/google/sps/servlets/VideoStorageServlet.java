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
import com.google.sps.data.VideoAnalysis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    Query query = new Query("Video")
      .addSort("sentiment", SortDirection.DESCENDING)
      .addSort("numSearches", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    // Create list of video IDs stored in datastore.
    ArrayList<VideoAnalysis> videos = new ArrayList<>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(24))) {
      Key videoKey = entity.getKey();
      double scoreDouble = (double) entity.getProperty("sentiment");
      String scoreStr = Double.toString(scoreDouble);
      VideoAnalysis videoAnalysis = new VideoAnalysis.Builder()
        .setScore(((Double) entity.getProperty("sentiment")).floatValue())//(Float.parseFloat(scoreStr))
        .setId(videoKey.getName())
        .setScoreAvailable(true)
        .build();
      videos.add(videoAnalysis);
    }
    /*ArrayList<VideoAnalysis> videos = StreamSupport
        .stream(results.asIterable(FetchOptions.Builder.withLimit(24)).spliterator(), false)
        .map(entity -> new VideoAnalysis.Builder()
            .setId(entity.getKey().getName())
            .setScore(((Double) entity.getProperty("sentiment")).floatValue())
            .setScoreAvailable(true)
            .build())
        .collect(Collectors.toCollection(ArrayList::new));*/

    Gson gson = new Gson();
    String json = gson.toJson(videos);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
