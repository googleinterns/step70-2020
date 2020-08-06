package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ConcurrentModificationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class StoreVideosTest {

  FetchOptions limit = FetchOptions.Builder.withLimit(2);
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final Float SENTIMENT_SCORE = 0.5F;
  private final String VIDEO_ID_1 = "test ID 1";

  @Spy DatastoreService datastoreSpy = DatastoreServiceFactory.getDatastoreService();

  @InjectMocks StoreVideos storeVideos;
  @InjectMocks StoreVideos storeVideos2;

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  /**
   * If a video ID doesn't already exist in Datastore, the entity gets added to the database.
   */
  @Test
  public void addsNewEntitiesToDatastore() {
    Assert.assertEquals(0, datastoreSpy.prepare(new Query("Video")).countEntities(limit));
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);
    Assert.assertEquals(1, datastoreSpy.prepare(new Query("Video")).countEntities(limit));
  }

  /**
   * If a video ID already exists in Datastore, the numSearches property of that entity is
   * incremented by 1.
   */
  @Test
  public void incrementsNumSearchesOfEntities() {
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);

    PreparedQuery results = datastoreSpy.prepare(new Query("Video"));

    // Check that the entity isn't duplicated in datastore
    Assert.assertEquals(1, results.countEntities(limit));

    // Check that numSearches was incremented
    int numSearches = (int) results.asSingleEntity().getProperty("numSearches");
    Assert.assertTrue(numSearches == 2);
  }

  /**
   * An entity's numSearches should be incremented if two different users (two instances of the
   * storeVideos class) search for the same video.
   */
  @Test
  public void differentUsersSearchSameVideo() {
    StoreVideos storeVideos1Spy = spy(storeVideos);
    StoreVideos storeVideos2Spy = spy(storeVideos2);

    storeVideos1Spy.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);
    storeVideos2Spy.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);

    PreparedQuery results = datastoreSpy.prepare(new Query("Video"));

    // Check that the entity isn't duplicated in datastore
    Assert.assertEquals(1, results.countEntities(limit));

    // Check that numSearches was incremented
    int numSearches = (int) results.asSingleEntity().getProperty("numSearches");
    Assert.assertTrue(numSearches == 2);
  }

  /**
   * A ConcurrentModificationException is thrown if addToDatabase must try 3 times to increment
   * numSearches.
   */

  @Test(expected = ConcurrentModificationException.class)
  public void failureToIncrementThrowsException()
      throws EntityNotFoundException, ConcurrentModificationException {
    StoreVideos storeVideosSpy = spy(storeVideos);

    // Add entity to database first
    storeVideosSpy.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);

    // Try to add the entity a second time, throw ConcurrentModificationException instead
    doThrow(new ConcurrentModificationException())
        .when(datastoreSpy).put(any(Transaction.class), any(Entity.class));
    storeVideosSpy.addToDatabase(VIDEO_ID_1, SENTIMENT_SCORE);
  }
}
