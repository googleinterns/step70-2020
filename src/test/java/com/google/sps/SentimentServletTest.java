package com.google.sps;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
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
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class SentimentServletTest {

  private final Float SCORE = new Float(0.05f);
  private StringWriter stringWriter;
  private PrintWriter writer;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS) HttpServletRequest requestMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) LanguageServiceClient languageServiceMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) Sentiment sentimentMock;

  @Spy HttpServletResponse responseSpy;

  @InjectMocks SentimentServlet sentimentServlet;

  @Before
  public void setUp() throws IOException {
    stringWriter = new StringWriter();
    writer = new PrintWriter(stringWriter);
    when(responseSpy.getWriter()).thenReturn(writer);
  }

  /**
   * A Json String in the form of an array should be converted to a String of period separated
   * comments. A sentiment score should be calculated and the score should be printed in a Json
   * object. 
   * Ex. "[foo, bar]" -> "{"commentScore":0.05}" 
   */
  @Test
  public void jsonArrayPrintsVideoAnalysisJsonObject() throws IOException {
    String json = "[\"a\",\"b\",\"c\"]"; // "["a","b","c"]"
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    when(languageServiceMock.analyzeSentiment(any(Document.class)).getDocumentSentiment())
        .thenReturn(sentimentMock);
    when(sentimentMock.getScore()).thenReturn(SCORE);
    doNothing().when(languageServiceMock).close();

    sentimentServlet.doGet(requestMock, responseSpy);

    Assert.assertEquals(stringWriter.toString(), "{\"commentScore\":0.05}\n");
  }

  /**
   * If the Json String is not in the form of an array, a JsonSyntaxException is caught and a 400
   * error is sent.
   */
  @Test
  public void notArraySendsHttpResponseError() throws IOException {
    String json = "foo";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    sentimentServlet.doGet(requestMock, responseSpy);

    verify(responseSpy, times(1)).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
  }

  /**
   * If the Json String is an empty array (ex. no comments), a 400 error is sent.
   */
  @Test
  public void emptyArraySendsHttpResponseError() throws IOException {
    String json = "[]";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    
    sentimentServlet.doGet(requestMock, responseSpy);

    verify(responseSpy, times(1)).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
  }

  /**
   * If the Natural Language API fails and throws an ApiException, a 500 error is sent.
   */
  @Test
  public void languageServiceFailureSendsHttpResponseError() throws IOException {
    String json = "[\"a\",\"b\",\"c\"]";
    when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    when(languageServiceMock.analyzeSentiment(any(Document.class))).thenThrow(ApiException.class);

    sentimentServlet.doGet(requestMock, responseSpy);

    verify(responseSpy, times(1))
        .sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), anyString());
  }

}