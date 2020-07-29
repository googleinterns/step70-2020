package com.google.sps.servlets;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
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
    for (String language : languages) {
      try {
        return parseXmlFromStream(captionService.fetchStream(videoId, language));
      } catch (SAXException | ParserConfigurationException | IOException
          | IllegalArgumentException e) {
        continue;
      }
    }
    return "";
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
