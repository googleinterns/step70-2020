package com.google.sps;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.StringReader;
import com.google.sps.servlets.SentimentServlet;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class SentimentServletTest {

  SentimentServlet sentimentServlet = new SentimentServlet();

  @Mock
  HttpServletRequest requestMock;

  /**
   * Takes a Json String of an array and returns the elements of the array as a String, separated
   * by periods.
   * Ex. "["foo","bar"]" -> "foo. bar"
   */
  @Test
  public void jsonArrayConvertsToPeriodSeparatedString() throws IOException {
    String json = "[\"a\",\"b\",\"c\"]"; // "["a","b","c"]"
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    String actual = sentimentServlet.requestToString(requestMock);

    Assert.assertEquals("a. b. c", actual);
  }

  /**
   * If the Json String is an empty array, an empty String should be returned. 
   * Ex. "[]" -> ""
   */
  @Test
  public void emptyJsonArrayConvertsToEmptyString() throws IOException {
    String json = "[]";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    String actual = sentimentServlet.requestToString(requestMock);

    Assert.assertEquals("", actual);
  }

  /**
   * If the Json String is not in the form of an array, a JsonSyntaxException is caught and an
   * empty String is returned. 
   * Ex. "foo" -> ""
   */
  @Test
  public void notArrayConvertsToEmptyString() throws IOException {
    String json = "foo";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

    String actual = sentimentServlet.requestToString(requestMock);

    Assert.assertEquals("", actual);
  }

}