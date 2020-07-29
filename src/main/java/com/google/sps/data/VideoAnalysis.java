package com.google.sps.data;

public class VideoAnalysis {

  public static class Builder {

    private Float score;
    private boolean scoreAvailable;

    public Builder setScore(Float score){
      this.score = score;
      return this;
    }

    public Builder setScoreAvailable(boolean scoreAvailable){
      this.scoreAvailable = scoreAvailable;
      return this;
    }

    public VideoAnalysis build() {
      VideoAnalysis videoAnalysis = new VideoAnalysis();
      videoAnalysis.score = this.score;
      videoAnalysis.scoreAvailable = this.scoreAvailable;

      return videoAnalysis;
    }
  }

  private Float score;
  private boolean scoreAvailable;

  private VideoAnalysis() {
    this.score = score;
    this.scoreAvailable = scoreAvailable;
  }

  public Float getScore() {
    return score;
  }

  public boolean getScoreAvailable() {
    return scoreAvailable;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public void setScoreAvailable(boolean scoreAvailable) {
    this.scoreAvailable = scoreAvailable;
  }
}