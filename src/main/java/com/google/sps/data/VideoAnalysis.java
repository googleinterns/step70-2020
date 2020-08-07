package com.google.sps.data;

public class VideoAnalysis {

  public static class Builder {

    private String id;
    private Float score;
    private boolean scoreAvailable;

    public Builder setId(String id){
      this.id = id;
      return this;
    }

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
      videoAnalysis.id = this.id;
      videoAnalysis.score = this.score;
      videoAnalysis.scoreAvailable = this.scoreAvailable;

      return videoAnalysis;
    }
  }

  private String id;
  private Float score;
  private boolean scoreAvailable;

  private VideoAnalysis() {
    this.id = id;
    this.score = score;
    this.scoreAvailable = scoreAvailable;
  }

  public String getId() {
    return id;
  }

  public Float getScore() {
    return score;
  }

  public boolean getScoreAvailable() {
    return scoreAvailable;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public void setScoreAvailable(boolean scoreAvailable) {
    this.scoreAvailable = scoreAvailable;
  }
}