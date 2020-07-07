package com.google.sps.data;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VideoAnalysis {

  private final Float commentScore;

  public VideoAnalysis(Float commentScore) {
    this.commentScore = commentScore;
  }

  public Float getCommentScore() {
    return commentScore;
  }
}