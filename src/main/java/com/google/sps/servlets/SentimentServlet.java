package com.google.sps.servlets;

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

  private VideoAnalysis videoAnalysis;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comments = requestToString(request);
    Float commentsScore = calculateSentimentScore(comments);
    videoAnalysis = new VideoAnalysis(commentsScore);
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

    if (textStr.equals("[]")) {
      return null;
    }

    try {
      List<String> textList = Arrays.asList(gson.fromJson(textStr, String[].class));
      return String.join(". ", textList);
    } catch (JsonSyntaxException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  /**
   * Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
   */ 
  private Float calculateSentimentScore(String text) throws IOException {
    if (text == null) {
      return null;
    }

    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    return score;
  }
}
