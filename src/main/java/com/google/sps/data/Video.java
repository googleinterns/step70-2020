package com.google.sps.data;

import java.io.IOException;
import java.util.List;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class Video {

  private final List<String> comments;
  private final float commentScore;

  public Video(List<String> comments/*, List<String> captions*/) throws IOException {
    // Can add captions functionality later on.
    this.comments = comments;
    this.commentScore = calculateSentimentScore(String.join(". ", comments));
  }

  // Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
  private float calculateSentimentScore(String text) throws IOException {
    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    return score;
  }
  
  public List<String> getComments() {
    return comments;
  }

  public float getCommentScore() {
    return commentScore;
  }
}