/*package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class VideoStorageServletTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private StringWriter stringWriter;
  private PrintWriter writer;
  private final Float HIGHER_SENTIMENT = 0.5f;
  private final Float LOWER_SENTIMENT = -0.5f;
  private final String VIDEO_ID_1 = "test ID 1";
  private final String VIDEO_ID_2 = "test ID 2";
  private final String VIDEO_ID_3 = "test ID 3";

  @Mock HttpServletRequest requestMock;

  @Spy HttpServletResponse responseSpy;
  @Spy DatastoreService datastoreSpy = DatastoreServiceFactory.getDatastoreService();

  @InjectMocks VideoStorageServlet videoStorageServlet;

  @Before
  public void setUp() throws IOException, Exception {
    helper.setUp();

    stringWriter = new StringWriter();
    writer = new PrintWriter(stringWriter);
    when(responseSpy.getWriter()).thenReturn(writer);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }*/

  /**
   * The query lists results in a list of video IDs, ordered by highest sentiment.
   *//*
  @Test
  public void queryResultsRespondWithVideoIdList() throws IOException {
    datastoreSpy.put(createVideoEntity(VIDEO_ID_1, LOWER_SENTIMENT, 2));
    datastoreSpy.put(createVideoEntity(VIDEO_ID_2, HIGHER_SENTIMENT, 1));

    videoStorageServlet.doGet(requestMock, responseSpy);

    String expected = String.format("[\"%s\",\"%s\"]\n", VIDEO_ID_2, VIDEO_ID_1);

    Assert.assertEquals(expected, stringWriter.toString());
  }
*/
  /**
   * The query lists results in a list of video IDs, ordered by highest sentiment first and then by
   * highest numSearches.
   *
   * Ex. Entity 1: sentiment = 0.1, numSearches = 2
   * Entity 2: sentiment = 0.1, numSearches = 5
   * Entity 3: sentiment = 0.8, numSearches = 2
   *
   * Result: [Entity 3, Entity 2, Entity 1] because Entity 3 has the highest sentiment score.
   * Entity 2 is listed higher than Entity 1 even though they have the same sentiment because
   * it has a higher numSearches value.
   *//*
  @Test
  public void queryResultsRespondWithVideoIdListInOrder() throws IOException {
    datastoreSpy.put(createVideoEntity(VIDEO_ID_1, LOWER_SENTIMENT, 2));
    datastoreSpy.put(createVideoEntity(VIDEO_ID_2, LOWER_SENTIMENT, 1));
    datastoreSpy.put(createVideoEntity(VIDEO_ID_3, HIGHER_SENTIMENT, 1));

    videoStorageServlet.doGet(requestMock, responseSpy);

    String expected =
        String.format("[\"%s\",\"%s\",\"%s\"]\n", VIDEO_ID_3, VIDEO_ID_1, VIDEO_ID_2);

    Assert.assertEquals(expected, stringWriter.toString());
  }
*/
  /**
   * If the database has no entities to query, doGet responds with an empty list.
   *//*
  @Test
  public void noQueryResultsRespondWithEmptyList() throws IOException {
    videoStorageServlet.doGet(requestMock, responseSpy);

    String expected = "[]\n";

    Assert.assertEquals(expected, stringWriter.toString());
  }

  private Entity createVideoEntity(String id, Float sentiment, int numSearches) {
    Entity entity = new Entity("Video", id);
    entity.setProperty("sentiment", sentiment);
    entity.setProperty("numSearches", numSearches);

    return entity;
  }
}*/
