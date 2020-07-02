package com.google.sps.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class CaptionService {
  public InputStream fetchStream(String videoId, String language)
      throws MalformedURLException, IOException {
    if (videoId == null) {
      throw new MalformedURLException();
    }
    return new URL(
        String.format("http://video.google.com/timedtext?lang=%s&v=%s", language, videoId))
            .openStream();
  }
}
