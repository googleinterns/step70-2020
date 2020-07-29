package com.google.sps;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.sps.servlets.Caption;
import com.google.sps.servlets.CommentService;
import com.google.sps.servlets.SentimentServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Arrays;
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

  private final String VIDEO_ID = "test id";
  private final Float SCORE = new Float(0.05f);
  private StringWriter stringWriter;
  private PrintWriter writer;
  private DecimalFormat df = new DecimalFormat("#.##");

  @Mock CommentService commentServiceMock;
  @Mock Caption captionServiceMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) HttpServletRequest requestMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) LanguageServiceClient languageServiceMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) Sentiment sentimentMock;

  @Spy HttpServletResponse responseSpy;

  @InjectMocks SentimentServlet sentimentServlet;

  @Before
  public void setUp() throws IOException, Exception {
    stringWriter = new StringWriter();
    writer = new PrintWriter(stringWriter);
    when(responseSpy.getWriter()).thenReturn(writer);
  }

  /**
   * A sentiment score should be calculated for a list of comments and String of captions and the
   * score should be printed in a Json object. 
   */
  @Test
  public void printsVideoAnalysisJsonObject() throws IOException {
    List<String> comments = Arrays.asList("foo","bar","foobar");
    String captions = "foo. bar. foobar.";

    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID)).thenReturn(comments);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);
    when(languageServiceMock.analyzeSentiment(any(Document.class)).getDocumentSentiment())
        .thenReturn(sentimentMock);
    when(sentimentMock.getScore()).thenReturn(SCORE);

    sentimentServlet.doGet(requestMock, responseSpy);

    String expected = "{\"score\":" + df.format(SCORE) + ",\"scoreAvailable\":true}\n";

    Assert.assertEquals(expected, stringWriter.toString());
  }

  /**
   * If captions are available and comments are not, a sentiment score should still calculated and
   * the score should be printed in a Json object. 
   */
  @Test
  public void onlyCaptionsPrintsVideoAnalysisJsonObject() throws IOException {
    List<String> comments = Arrays.asList();
    String captions = "foo. bar. foobar.";
    
    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID)).thenReturn(comments);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);
    when(languageServiceMock.analyzeSentiment(any(Document.class)).getDocumentSentiment())
        .thenReturn(sentimentMock);
    when(sentimentMock.getScore()).thenReturn(SCORE);

    sentimentServlet.doGet(requestMock, responseSpy);

    String expected = "{\"score\":" + df.format(SCORE) + ",\"scoreAvailable\":true}\n";

    Assert.assertEquals(expected, stringWriter.toString());
  }

  /**
   * If comments are available and captions are not, a sentiment score should still calculated and
   * the score should be printed in a Json object. 
   */
  @Test
  public void onlyCommentsPrintsVideoAnalysisJsonObject() throws IOException {
    List<String> comments = Arrays.asList("foo","bar","foobar");
    String captions = "";
    
    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID)).thenReturn(comments);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);
    when(languageServiceMock.analyzeSentiment(any(Document.class)).getDocumentSentiment())
        .thenReturn(sentimentMock);
    when(sentimentMock.getScore()).thenReturn(SCORE);

    sentimentServlet.doGet(requestMock, responseSpy);

    String expected = "{\"score\":" + df.format(SCORE) + ",\"scoreAvailable\":true}\n";

    Assert.assertEquals(expected, stringWriter.toString());
  }

  /**
   * If there's no comments or captions available to analyze, a Json object should still be printed
   * but without a score value (becaues score == null) and scoreAvailable == false. 
   */
  @Test
  public void noCommentsOrCaptionsPrintsJsonObjectWithoutScore() throws IOException {
    List<String> comments = Arrays.asList();
    String captions = "";
    
    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID)).thenReturn(comments);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);

    sentimentServlet.doGet(requestMock, responseSpy);
    
    String expectedNoData = "{\"scoreAvailable\":false}\n";

    Assert.assertEquals(expectedNoData, stringWriter.toString());
  }

  /**
   * When CommentService throws an IllegalArgumentException, a 500 error should be sent.
   */
  @Test
  public void invalidVideoIdSendsHttpResponseError() throws IOException {
    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID))
        .thenThrow(IllegalArgumentException.class);

    sentimentServlet.doGet(requestMock, responseSpy);

    verify(responseSpy).sendError(HttpServletResponse.SC_BAD_REQUEST,
        "Video is private or does not exist.");
  }

  /**
   * CommentService throws an GoogleJsonResponseException, a 500 error should be sent and only the
   * captions are analyzed.
   */
  @Test
  public void commentRetrievalFailureSendsHttpResponseError() throws IOException {
    String captions = "foo. bar. foobar.";

    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID))
        .thenThrow(GoogleJsonResponseException.class);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);
    when(languageServiceMock.analyzeSentiment(any(Document.class)).getDocumentSentiment())
        .thenReturn(sentimentMock);
    when(sentimentMock.getScore()).thenReturn(SCORE);

    sentimentServlet.doGet(requestMock, responseSpy);

    String expected = "{\"score\":" + df.format(SCORE) + ",\"scoreAvailable\":true}\n";

    verify(responseSpy).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        "Comments could not be retrieved.");
    Assert.assertEquals(expected, stringWriter.toString());
  }

  /**
   * If the Natural Language API fails and throws an ApiException, a 500 error should be sent.
   */
  @Test
  public void languageServiceFailureSendsHttpResponseError() throws IOException {
    List<String> comments = Arrays.asList("foo","bar","foobar");
    String captions = "foo. bar. foobar.";
    
    when(requestMock.getParameter("video-id")).thenReturn(VIDEO_ID);
    when(commentServiceMock.getCommentsFromId(VIDEO_ID)).thenReturn(comments);
    when(captionServiceMock.getCaptionFromId(VIDEO_ID)).thenReturn(captions);
    when(languageServiceMock.analyzeSentiment(any(Document.class))).thenThrow(ApiException.class);

    sentimentServlet.doGet(requestMock, responseSpy);

    verify(responseSpy).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        "Language service client failed.");
  }

}