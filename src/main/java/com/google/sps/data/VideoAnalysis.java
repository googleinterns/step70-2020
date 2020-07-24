package com.google.sps.data;

public class VideoAnalysis {

  public static class Builder {

    private Float score;
    private boolean dataAvailable;

    public Builder setScore(Float score){
      this.score = score;
      return this;
    }

    public Builder setDataAvailable(boolean dataAvailable){
      this.dataAvailable = dataAvailable;
      return this;
    }

    public VideoAnalysis build() {
      VideoAnalysis videoAnalysis = new VideoAnalysis();
      videoAnalysis.score = this.score;
      videoAnalysis.dataAvailable = this.dataAvailable;

      return videoAnalysis;
    }
  }

  private Float score;
  private boolean dataAvailable;

  private VideoAnalysis() {
    this.score = score;
    this.dataAvailable = dataAvailable;
  }

  public Float getScore() {
    return score;
  }

  public boolean getDataAvailable() {
    return dataAvailable;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public void setDataAvailable(boolean dataAvailable) {
    this.dataAvailable = dataAvailable;
  }
}