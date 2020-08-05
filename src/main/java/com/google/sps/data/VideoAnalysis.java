package com.google.sps.data;

public class VideoAnalysis {

  public static class Builder {

    private Float score;
    private boolean scoreAvailable;
    private String videoId;

    public Builder setScore(Float score){
      this.score = score;
      return this;
    }

    public Builder setScoreAvailable(boolean scoreAvailable){
      this.scoreAvailable = scoreAvailable;
      return this;
    }

    public Builder setVideoId(String id){
      this.videoId = id;
      return this;
    }

    public VideoAnalysis build() {
      VideoAnalysis videoAnalysis = new VideoAnalysis();
      videoAnalysis.score = this.score;
      videoAnalysis.scoreAvailable = this.scoreAvailable;
      videoAnalysis.videoId = this.videoId;

      return videoAnalysis;
    }
  }

  private Float score;
  private boolean scoreAvailable;
  private String videoId;

  private VideoAnalysis() {
    this.score = score;
    this.scoreAvailable = scoreAvailable;
    this.videoId = videoId;
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

  public void setVideoId(String id) {
    this.videoId = id;
  }
}
