package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(JUnit4.class)
public final class CaptionTest {
  @Mock
  private CaptionService captionServiceMock;

  @InjectMocks
  private Caption caption;

  final private String PROPER_XML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><transcript>"
      + "<text start=\"4.029\" dur=\"5.401\">line1</text>"
      + "<text start=\"9.43\" dur=\"5.06\">line2</text>"
      + "<text start=\"14.49\" dur=\"5.03\">line3</text>" + "</transcript>";

  final private String PROPER_XML_WITH_EMPTY_LINE =
      "<?xml version=\"1.0\" encoding=\"utf-8\" ?><transcript>"
          + "<text start=\"4.029\" dur=\"5.401\">line1</text>"
          + "<text start=\"9.43\" dur=\"5.06\">line2</text>"
          + "<text start=\"14.49\" dur=\"5.03\"></text>"
          + "<text start=\"19.52\" dur=\"5.00\">line3</text>" + "</transcript>";

  final private String PROPER_XML_CAPTION = "line1 line2 line3";

  final private String BAD_XML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><transcript>"
      + "<text start=\"4.029\" dur=\"5.401\">line1</text>";

  final private String VIDEO_ID = "sampleId";

  final private String LANG_EN = "en";

  final private String LANG_EN_GB = "en-GB";

  @Before
  public void init() {
    caption = new Caption();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void properXmlReturnsConcatenatedString() throws IOException {
    when(captionServiceMock.fetchStream(eq(VIDEO_ID), contains(LANG_EN)))
        .thenAnswer(invocation -> new ByteArrayInputStream(PROPER_XML.getBytes()));

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertEquals(PROPER_XML_CAPTION, actual);
  }

  @Test
  public void ignoreEmptyLineInXmlInConcatenatedString() throws IOException {
    when(captionServiceMock.fetchStream(eq(VIDEO_ID), contains(LANG_EN)))
        .thenAnswer(invocation -> new ByteArrayInputStream(PROPER_XML_WITH_EMPTY_LINE.getBytes()));

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertEquals(PROPER_XML_CAPTION, actual);
  }

  @Test
  public void badXmlReturnsNullIgnoringSAXException() throws IOException {
    when(captionServiceMock.fetchStream(eq(VIDEO_ID), contains(LANG_EN)))
        .thenAnswer(invocation -> new ByteArrayInputStream(BAD_XML.getBytes()));

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertTrue(actual.isEmpty());
  }

  @Test
  public void nonDefaultLanguageReturnsConcatenatedString() throws IOException {
    when(captionServiceMock.fetchStream(eq(VIDEO_ID), eq(LANG_EN_GB)))
        .thenAnswer(invocation -> new ByteArrayInputStream(PROPER_XML.getBytes()));
    when(captionServiceMock.fetchStream(eq(VIDEO_ID), not(eq(LANG_EN_GB))))
        .thenThrow(new IOException());

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertEquals(PROPER_XML_CAPTION, actual);
  }

  @Test
  public void nullIdReturnsNullIgnoringIOException() throws IOException {
    when(captionServiceMock.fetchStream(eq(null), anyString())).thenThrow(new IOException());

    String actual = caption.getCaptionFromId(null);

    assertTrue(actual.isEmpty());
  }

  @Test
  public void badIDReturnsNullIgnoringIOException() throws IOException {
    when(captionServiceMock.fetchStream(anyString(), anyString())).thenThrow(new IOException());

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertTrue(actual.isEmpty());
  }

  @Test
  public void nullStreamReturnsNullIgnoringIllegalArgumentException() throws IOException {
    when(captionServiceMock.fetchStream(anyString(), anyString())).thenReturn(null);

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertTrue(actual.isEmpty());
  }

  @Test
  public void ignoreParserConfigurationException() throws ParserConfigurationException {
    DocumentBuilderFactory docBuilderFactory = mock(DocumentBuilderFactory.class);
    when(docBuilderFactory.newDocumentBuilder()).thenThrow(new ParserConfigurationException());

    String actual = caption.getCaptionFromId(VIDEO_ID);

    assertTrue(actual.isEmpty());
  }

}
