package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.stream.Collectors;
import com.google.sps.data.VideoAnalysis;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {

  private VideoAnalysis videoAnalysis;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comments = requestToString(request);
    float commentsScore = calculateSentimentScore(comments);
    videoAnalysis = new VideoAnalysis(commentsScore);
    String json = new Gson().toJson(videoAnalysis);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  // Converts JSON String of a request (that is in the form of an array) to a String, separated by
  // periods. If the Json cannot be converted to an array, an empty String is returned. 
  // Ex. "["a","b","c"]" --> "a. b. c"
  public String requestToString(HttpServletRequest request) throws IOException {
    Gson gson = new Gson();
    
    String textStr = request.getReader().lines().collect(Collectors.joining());
    List<String> textList;
    try {
      textList = Arrays.asList(gson.fromJson(textStr, String[].class));
    } catch (JsonSyntaxException e) {
      System.out.println(e.getMessage());
      textList = Arrays.asList();
    }

    return String.join(". ", textList);
  }

  // Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
  public float calculateSentimentScore(String text) throws IOException {
    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    return score;
  }
}
