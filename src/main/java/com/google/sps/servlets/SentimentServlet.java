package com.google.sps.servlets;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.sps.data.VideoAnalysis;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
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
  private final String INVALID_INPUT_ERROR = "Video is private or does not exist.";
  private final String COMMENTS_FAILED_ERROR = "Comments could not be retrieved.";
  private final String NLP_API_ERROR = "Language service client failed.";

  public SentimentServlet() throws IOException, GeneralSecurityException {
    languageService = LanguageServiceClient.create();
    captionService = new Caption();
    commentService = new CommentService();
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

    String captions = captionService.getCaptionFromId(videoId);

    if (commentsList.isEmpty() && captions.isEmpty()) {
      String json = createResponseJson(null, false);
      response.setContentType("application/json;");
      response.getWriter().println(json);
      return;
    }
    
    Float commentsScore = null;
    if (!commentsList.isEmpty()) {
      commentsScore = 0f;
      for (String comment : commentsList) {
        try {
          commentsScore += calculateSentimentScore(comment);
        } catch (ApiException e) {
          System.err.println(e.getMessage());
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, NLP_API_ERROR);
          return;
        }
      }
      commentsScore = commentsScore / commentsList.size();
    }

    Float captionsScore = null;
    if (!captions.isEmpty()) {
      try {
        captionsScore = calculateSentimentScore(captions);
      } catch (ApiException e) {
        System.err.println(e.getMessage());
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, NLP_API_ERROR);
        return;
      }
    }

    String json = "";
    if (commentsScore != null && captionsScore != null) {
      json = createResponseJson((commentsScore + captionsScore) / 2f, true);
    } else if (commentsScore == null && captionsScore != null) {
      json = createResponseJson(captionsScore, true);
    } else {
      json = createResponseJson(commentsScore, true);
    }
    
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

  /**
   * Creates a Json object of a VideoAnalysis object.
   */ 
  private String createResponseJson(Float score, boolean available) {
    VideoAnalysis videoAnalysis = new VideoAnalysis.Builder()
        .setScore(score)
        .setScoreAvailable(available)
        .build();
    return new Gson().toJson(videoAnalysis);
  }
}
