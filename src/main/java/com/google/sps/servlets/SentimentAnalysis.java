package com.google.sps.servlets;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class SentimentAnalysis {

  private LanguageServiceClient languageService;

  public SentimentAnalysis() throws IOException {
    languageService = LanguageServiceClient.create();
  }

  /**
   * Calculates sentiment score of text. The score is from -1 (negative) to +1 (positive).
   */
  public float calculateSentimentScore(String text) throws ApiException {
    Document doc = Document.newBuilder()
        .setContent(text)
        .setTypeValue(Document.Type.PLAIN_TEXT_VALUE)
        .build();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();

    return score;
  }
}