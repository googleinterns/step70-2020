package com.google.sps.servlets;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Transaction;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoreVideos extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public Entity createVideoEntity(String id, Float sentiment) {
    Entity videoEntity = new Entity("Video", id);
    videoEntity.setProperty("sentiment", sentiment);
    videoEntity.setProperty("numSearches", 0);
    return videoEntity;
  }

  public void addToDatabase(String id, Float sentiment) {
    Transaction transaction = datastore.beginTransaction();
    Entity videoEntity;

    // get datastore Entity or create one if needed
    try {
      Key videoKey = KeyFactory.createKey("Video", id);
      videoEntity = datastore.get(transaction, videoKey);
    } catch (EntityNotFoundException e) {
      videoEntity = createVideoEntity(id, sentiment);
    }

    // increment number of searches
    videoEntity.setProperty("numSearches", (int)videoEntity.getProperty("numSearches") + 1);
    //videoEntity.setProperty("numSearches",
        //Long.sum((Long) videoEntity.getProperty("numSearches"), 1L));

    // update database
    for (int numRetries = 2; numRetries >= 0; numRetries--) {
      try {
        datastore.put(transaction, videoEntity);
        transaction.commit();
        break;
      } catch (ConcurrentModificationException e) {
        if (numRetries == 0) {
          throw e;
        }
      } finally {
        if (transaction.isActive()) {
          transaction.rollback();
        }
      }
    }
  }
}
