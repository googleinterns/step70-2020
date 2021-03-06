package com.google.sps.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Caption {
  /*
   * Usage: String caption = new Caption().getCaptionFromID("abcdefg");
   * 
   * Return null if not found.
   */

  private CaptionService captionService = new CaptionService();
  private DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

  public String getCaptionFromId(String videoId) {
    // BCP47 language tags
    final String[] languages = {"en", "en-US", "en-GB", "en-AU", "en-IE", "en-ZA"};

    ExecutorService executor = Executors.newFixedThreadPool(languages.length);

    List<Callable<String>> captionCallables =
        Arrays.stream(languages).map(language -> new Callable<String>() {
          @Override
          public String call() throws Exception {
            return parseXmlFromStream(captionService.fetchStream(videoId, language));
          }
        }).collect(Collectors.toList());

    String caption = "";
    try {
      caption = executor.invokeAny(captionCallables);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    } finally {
      executor.shutdown();
    }
    return caption;
  }

  private String parseXmlFromStream(InputStream inputStream)
      throws SAXException, ParserConfigurationException, IOException, IllegalArgumentException {
    Document doc = docBuilderFactory.newDocumentBuilder().parse(inputStream);
    NodeList textDom = doc.getElementsByTagName("text");
    StringBuilder caption = new StringBuilder();
    for (int i = 0; i < textDom.getLength(); i++) {
      Node child = textDom.item(i).getFirstChild();
      if (child != null) {
        caption.append(textDom.item(i).getFirstChild().getNodeValue());
        caption.append(" ");
      }
    }
    return caption.toString().trim();
  }

}
