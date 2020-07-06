package com.google.sps;

import com.google.sps.servlets.CommentService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)

public final class CommentServiceTest {

  CommentService commentService = new CommentService();

  /**
   * A list of Strings should be returned for any public video that has comments. 
   */
  @Test
  public void publicVideoReturnsCommentsList() throws GeneralSecurityException, IOException {
    String videoId = "v3whnmU619I"; // Public video that has some comments.
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertFalse(actual.isEmpty());
  }

  /**
   * Unlisted videos should be accessible to the API if the video id is inputted. List of comments
   * should be returned for unlisted videos just like they would for a public video. 
   */
  @Test
  public void unlistedVideoReturnsCommentsList() throws GeneralSecurityException, IOException {
    String videoId = "9BUFUUhfXSg"; // Unlisted test video that has 2 comments. 
    List<String> expected = Arrays.asList("test comment 2", "test comment 1");
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertEquals(expected, actual);
  }

  /**
   * Private videos should not be accessible with the YouTube API. In this case, a 
   * GoogleJsonResponseException should be caught and null should be returned. 
   */
  @Test
  public void privateVideoReturnsNull() throws GeneralSecurityException, IOException {
    String videoId = "qYnSnsysgVk";
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertEquals(null, actual);
  }
  
  /**
   * For videos that have disabled commenting, a GoogleJsonResponseException should be caught and
   * null should be returned. 
   */
  @Test
  public void disabledCommentsReturnsNull() throws GeneralSecurityException, IOException {
    String videoId = "NEXFoP0JurI";
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertEquals(null, actual);
  }

  /**
   * Videos with no comments should return null to avoid lowering the average sentiment score. 
   */
  @Test
  public void noCommentsReturnsNull() throws GeneralSecurityException, IOException {
    String videoId = "ERL0EDZT1kE";
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertEquals(null, actual);
  }

  /**
   * Invalid IDs that don't refer to a video should return null. 
   */
  @Test
  public void invalidVideoIdReturnsNull() throws GeneralSecurityException, IOException {
    String videoId = "";
    List<String> actual = commentService.getCommentsFromId(videoId);

    Assert.assertEquals(null, actual);
  }

}