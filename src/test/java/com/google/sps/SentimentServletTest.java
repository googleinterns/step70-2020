package com.google.sps;

import com.google.gson.JsonSyntaxException;
import com.google.sps.servlets.SentimentServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class SentimentServletTest {

  SentimentServlet sentimentServlet = new SentimentServlet();

  @Mock
  HttpServletRequest requestMock;

  @Spy
  HttpServletResponse responseMock;

  /**
   * A Json String in the form of an array should be converted to a String of period separated
   * comments. A sentiment score should be calculated and the score should be printed in a Json
   * object. 
   * Ex. "foo" -> "{"commentScore":X.XX}" where X.XX is a float, for any # of decimal places. 
   */
  @Test
  public void jsonArrayPrintsVideoAnalysisJsonObject() throws IOException {
    String json = "[\"a\",\"b\",\"c\"]"; // "["a","b","c"]"
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(responseMock.getWriter()).thenReturn(writer);

    new SentimentServlet().doGet(requestMock, responseMock);

    String pattern = "\\{\"commentScore\":[-+]?[0-9]*\\.?[0-9]+\\}\n";

    Assert.assertTrue(stringWriter.toString().matches(pattern));
  }

  /**
   * If the Json String is not in the form of an array, a JsonSyntaxException is caught and null is
   * returned. Gson excludes null values, therefore resulting in an empty Json object.
   * Ex. "foo" -> "{}"
   */
  @Test
  public void notArrayPrintsEmptyJsonObject() throws IOException {
    String json = "foo";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(responseMock.getWriter()).thenReturn(writer);

    new SentimentServlet().doGet(requestMock, responseMock);

    String pattern = "\\{\\}\n"; //"{}"

    Assert.assertTrue(stringWriter.toString().matches(pattern));
  }

  /**
   * If the Json String is an empty array, a JsonSyntaxException is caught and null is returned.
   * Gson excludes null values, therefore resulting in an empty Json object.
   * Ex. "[]" -> "{}"
   */
  @Test
  public void emptyArrayPrintsEmptyJsonObject() throws IOException {
    String json = "[]";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(responseMock.getWriter()).thenReturn(writer);

    new SentimentServlet().doGet(requestMock, responseMock);

    String pattern = "\\{\\}\n"; //"{}"

    Assert.assertTrue(stringWriter.toString().matches(pattern));
  }

}