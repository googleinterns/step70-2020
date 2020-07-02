package com.google.sps.data;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VideoAnalysis {

  private final float commentScore;

  public VideoAnalysis(float commentScore) {
    this.commentScore = commentScore;
  }

  public float getCommentScore() {
    return commentScore;
  }
}