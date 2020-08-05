package com.google.sps.servlets;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.sps.data.VideoAnalysis;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.security.GeneralSecurityException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {

  private CommentService commentService;
  private Caption captionService;
  private LanguageServiceClient languageService;
  private Sentiment sentiment;
  private StoreVideos database;
  private final String INVALID_INPUT_ERROR = "Video is private or does not exist.";
  private final String COMMENTS_FAILED_ERROR = "Comments could not be retrieved.";
  private final String NLP_API_ERROR = "Language service client failed.";
  private final String NO_DATA_ERROR = "No comments or captions available to analyze.";
  private final String READER_ERROR = "Reading video ID failed.";

  public SentimentServlet() throws IOException, GeneralSecurityException {
    languageService = LanguageServiceClient.create();
    captionService = new Caption();
    commentService = new CommentService();
    database = new StoreVideos();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String videoId = request.getParameter("video-id");

    List<String> commentsList = new ArrayList<>();
    try {
      commentsList = commentService.getCommentsFromId(videoId);
    } catch (IllegalArgumentException e) { // video not found
      System.err.println(e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_INPUT_ERROR);
      return;
    } catch (GoogleJsonResponseException e) { // some other problem with requesting comment data
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, COMMENTS_FAILED_ERROR);
    }
    String comments = String.join(". ", commentsList);

    String captions = captionService.getCaptionFromId(videoId);

    if (comments.isEmpty() && captions.isEmpty()) {
      VideoAnalysis videoAnalysis = new VideoAnalysis.Builder()
          .setScore(null)
          .setScoreAvailable(false)
          .setVideoId(videoId)
          .build();
      String json = new Gson().toJson(videoAnalysis);

      response.setContentType("application/json;");
      response.getWriter().println(json);
      return;
    }

    String text = comments + ". " + captions;

    Float score = null;

    try {
      score = calculateSentimentScore(text);
    } catch (ApiException e) {
      System.err.println(e.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, NLP_API_ERROR);
      return;
    }

    database.addToDatabase(videoId, score);

    VideoAnalysis videoAnalysis = new VideoAnalysis.Builder()
        .setScore(score)
        .setScoreAvailable(true)
        .setVideoId(videoId)
        .build();
    String json = new Gson().toJson(videoAnalysis);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
   */
  private Float calculateSentimentScore(String text) throws ApiException {
    Document doc = Document.newBuilder()
        .setContent(text)
        .setTypeValue(Document.Type.PLAIN_TEXT_VALUE)
        .build();
    sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    Float score = new Float(sentiment.getScore());

    return score;
  }
}
