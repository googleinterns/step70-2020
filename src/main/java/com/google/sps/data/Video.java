package com.google.sps.data;

public final class Video {

  private final String id;
  private final double sentiment;
  private final int numSearches;

  public Video(String id, double sentiment, int numSearches) {
    this.id = id;
    this.sentiment = sentiment;
    this.numSearches = 0;
  }
}
