package com.google.sps.data;

public class VideoAnalysis {

  private Float score;
  private boolean dataAvailable;

  public VideoAnalysis(Float score, boolean dataAvailable) {
    this.score = score;
    this.dataAvailable = dataAvailable;
  }

  public Float getScore() {
    return score;
  }

  public boolean getDataAvailable() {
    return dataAvailable;
  }
}