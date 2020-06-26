package com.google.sps.servlets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.stream.Collectors;
import com.google.sps.data.Video;

@WebServlet("/yt")
public class YouTubeServlet extends HttpServlet {

  private Video video;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String json = gson.toJson(video);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentsStr = request.getReader().lines().collect(Collectors.joining());
    Gson gson = new Gson();
    String[] commentsArr = gson.fromJson(commentsStr, String[].class);
    List<String> commentsList = Arrays.asList(commentsArr);
    video = new Video(commentsList);
  }
}
