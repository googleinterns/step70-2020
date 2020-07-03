package com.google.sps.servlets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CommentService {

  private final String DEVELOPER_KEY = "DEV_KEY";
  private static final String APPLICATION_NAME = "YouTube Comments";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private YouTube getService() throws GeneralSecurityException, IOException{
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /**
   * Calls YouTube Data API to return a list of the top 25 comments for a video, identified by its
   * video id. 
   */
  public List<String> getCommentsFromId(String videoId)
      throws GeneralSecurityException, IOException, GoogleJsonResponseException {
    YouTube youtubeService = getService();
    YouTube.CommentThreads.List request = youtubeService.commentThreads()
        .list(Collections.singletonList("snippet"));
    try { 
      // Call YouTube API and get the 25 comments that appear first in comment section of the video
      CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
          .setMaxResults(25L)
          .setOrder("relevance")
          .setTextFormat("plainText")
          .setVideoId(videoId)
          .execute();
      
      if (response.getItems().isEmpty()) {
        throw new Exception(String.format("Video %s has no comments.", videoId));
      }

      List<String> commentsList = new ArrayList<>();
      for (CommentThread commentThread : response.getItems()){
        String commentContent = commentThread.getSnippet()
            .getTopLevelComment()
            .getSnippet()
            .getTextDisplay();
        commentsList.add(commentContent);
      }
  
      return commentsList;

    } catch (GoogleJsonResponseException e) {
      System.out.println(e.getDetails().getMessage());
      // If comments cannot be retrieved, return null to indicate that sentiment score should not
      // be calculated. An arbitrary score of 0 will lower the average sentiment score between
      // captions and comments scores. 
      return null;
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
    
  }
}