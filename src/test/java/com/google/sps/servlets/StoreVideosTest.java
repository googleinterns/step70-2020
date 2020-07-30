package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
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

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final Float SENTIMENT = 0.5f;
  private final String VIDEO_ID_1 = "test ID 1";

  @Spy DatastoreService datastoreSpy = DatastoreServiceFactory.getDatastoreService();

  @InjectMocks StoreVideos storeVideos;

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
    Assert.assertEquals(0, datastoreSpy.prepare(new Query("Video")).countEntities());
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT);
    Assert.assertEquals(1, datastoreSpy.prepare(new Query("Video")).countEntities());
  }

  /**
   * If a video ID already exists in Datastore, the numSearches property of that entity is 
   * incremented by 1.
   */
  @Test
  public void incrementsNumSearchesOfEntities() {
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT);
    storeVideos.addToDatabase(VIDEO_ID_1, SENTIMENT);

    PreparedQuery results = datastoreSpy.prepare(new Query("Video"));

    // Check that the entity isn't duplicated in datastore
    Assert.assertEquals(1, results.countEntities()); 

    // Check that numSearches was incremented
    for (Entity entity : results.asIterable()) {
      Long numSearches = (long) entity.getProperty("numSearches");
      Assert.assertTrue(numSearches.equals(2L));
    }
  }

  /**
   * A ConcurrentModificationException is thrown if addToDatabase must try 3 times to increment
   * numSearches.
   */
  @Test(expected = ConcurrentModificationException.class)
  public void throwsException() throws EntityNotFoundException, ConcurrentModificationException {
    StoreVideos storeVideosSpy = spy(storeVideos);
    
    // Add entity to database first
    storeVideosSpy.addToDatabase(VIDEO_ID_1, SENTIMENT);
    doThrow(new ConcurrentModificationException()).when(storeVideosSpy).incrementSearchCount(VIDEO_ID_1);
    // Try to add the entity a second time, throw ConcurrentModificationException instead
    storeVideosSpy.addToDatabase(VIDEO_ID_1, SENTIMENT);
  }
}