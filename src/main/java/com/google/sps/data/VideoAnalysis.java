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
      return new VideoAnalysis(this);
    }
  }

  private final Float score;
  private final boolean dataAvailable;

  private VideoAnalysis(Builder builder) {
    this.score = builder.score;
    this.dataAvailable = builder.dataAvailable;
  }

  public Float getScore() {
    return score;
  }

  public boolean getDataAvailable() {
    return dataAvailable;
  }

  public static Builder builder() {
    return new Builder();
  }
}
