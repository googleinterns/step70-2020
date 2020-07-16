package com.google.sps.servlets;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.sps.data.VideoAnalysis;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {

  private LanguageServiceClient languageService;
  private Sentiment sentiment;

  public SentimentServlet() throws IOException {
    languageService = LanguageServiceClient.create();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comments = "";
    Float commentsScore = null;

    try {
      comments = requestToString(request);
    } catch (JsonSyntaxException e) {
      System.err.println(e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "Request to /sentiment must be an array of Strings.");
      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Converting request to String failed.");
      return;
    }

    if (comments.equals("")) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "No comments to analyze. Request must be a non-empty array.");
      return;
    }

    try {
      commentsScore = calculateSentimentScore(comments);
    } catch (ApiException e) {
      System.err.println(e.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Language service client failed.");
      return;
    }

    VideoAnalysis videoAnalysis = new VideoAnalysis(commentsScore);
    String json = new Gson().toJson(videoAnalysis);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Converts Json String of a request (that is in the form of an array) to a String, separated by
   * periods. If the Json cannot be converted to an array or is an empty array, null is returned. 
   * Ex. "["a","b","c"]" --> "a. b. c"
   */
  private String requestToString(HttpServletRequest request) throws IOException {
    Gson gson = new Gson();
    
    String textStr = request.getReader().lines().collect(Collectors.joining());
    List<String> textList = Arrays.asList(gson.fromJson(textStr, String[].class));
    return String.join(". ", textList);
  }

  /**
   * Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
   */ 
  private Float calculateSentimentScore(String text) {
    Document doc = Document.newBuilder()
        .setContent(text)
        .setTypeValue(Document.Type.PLAIN_TEXT_VALUE)
        .build();
    sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    Float score = new Float(sentiment.getScore());
    languageService.close();

    return score;
  }
}
