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

public class StoreVideos {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public Entity incrementSearchCount(String id)
      throws EntityNotFoundException, ConcurrentModificationException {
    Key videoKey = KeyFactory.createKey("Video", id);
    Entity videoEntity = datastore.get(videoKey);
    Long numSearches = (long) videoEntity.getProperty("numSearches");
    videoEntity.setProperty("numSearches", numSearches + 1);
    return videoEntity;
  }

  public Entity createVideoEntity(String id, Float sentiment) {
    Entity videoEntity = new Entity("Video", id);
    videoEntity.setProperty("sentiment", sentiment);
    videoEntity.setProperty("numSearches", 1);
    return videoEntity;
  }
  
  public void addToDatabase(String id, Float sentiment) {
    Transaction transaction = datastore.beginTransaction();
    for (int numRetries = 3; numRetries > 0; numRetries--) {
      try {
        datastore.put(transaction, incrementSearchCount(id));
        transaction.commit();
        break;
      } catch (EntityNotFoundException e) {
        datastore.put(transaction, createVideoEntity(id, sentiment));
        transaction.commit();
        break;
      } catch (ConcurrentModificationException e) {
        if(numRetries == 1) {
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
