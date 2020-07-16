package com.google.sps.servlets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommentService {

  private final String DEVELOPER_KEY = "DEV_KEY";
  private static final String APPLICATION_NAME = "YouTube Comments";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private final long MAX_RESULTS = 25L;
  private YouTube youtubeService;
  private YouTube.CommentThreads.List request;
  private CommentThreadListResponse response;

  /**
   * Initializes youtubeService. 
   * 
   * @throws GeneralSecurityException
   * @throws IOException 
   */
  public CommentService() throws GeneralSecurityException, IOException {
    youtubeService = getService();
  }

  /**
   * Builds the YouTube service to make calls to the API. 
   * 
   * @return a list of the top 25 comments for the video
   * @throws GeneralSecurityException
   * @throws IOException 
   */
  private YouTube getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /**
   * Calls YouTube Data API to get a list of comments for a video, identified by its video id. If
   * the video has no comments or comments are disabled, return an empty list.
   * 
   * @param videoId ID of YouTube video
   * @return a list of the top 25 comments for the video
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws GoogleJsonResponseException
   */
  public List<String> getCommentsFromId(String videoId)
      throws IOException, IllegalArgumentException, GoogleJsonResponseException {
    request = youtubeService.commentThreads().list(Collections.singletonList("snippet"));
    
    try { 
      // Call YouTube API and get the 25 comments that appear first in comment section of the video
      response = request.setKey(DEVELOPER_KEY)
          .setMaxResults(MAX_RESULTS)
          .setOrder("relevance")
          .setTextFormat("plainText")
          .setVideoId(videoId)
          .execute();
    } catch (GoogleJsonResponseException e) {
      System.err.println(e.getDetails().getMessage());
      
      for (GoogleJsonError.ErrorInfo err : e.getDetails().getErrors()) {
        if (err.getReason().equals("videoNotFound")) { // Invalid video id or private video
          throw new IllegalArgumentException("Video is private or does not exist.");
        } else if (err.getReason().equals("commentsDisabled")) { // Video with disabled comments
          return new ArrayList<>();
        }
        throw e;
      }
    }

    List<String> commentsList = new ArrayList<>();
    for (CommentThread commentThread : response.getItems()) {
      String commentContent = commentThread.getSnippet()
          .getTopLevelComment()
          .getSnippet()
          .getTextDisplay();
      commentsList.add(commentContent);
    }

    return commentsList;
  }
}