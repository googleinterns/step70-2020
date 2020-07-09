package com.google.sps.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CaptionService {
  public InputStream fetchStream(String videoId, String language) throws IOException {
    if (videoId == null) {
      throw new IllegalArgumentException("Video ID must be a non-null string.");
    }
    return new URL(
        String.format("http://video.google.com/timedtext?lang=%s&v=%s", language, videoId))
            .openStream();
  }
}
