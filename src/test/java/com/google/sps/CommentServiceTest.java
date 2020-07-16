package com.google.sps;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonError.ErrorInfo;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.sps.servlets.CommentService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public final class CommentServiceTest {

  String VIDEO_ID = ""; // Arbitrary video ID, never used.

  @Mock CommentThreadListResponse responseMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) YouTube youtubeServiceMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) YouTube.CommentThreads.List requestMock;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) GoogleJsonResponseException exceptionMock;

  @InjectMocks CommentService commentService;

  @Before
  public void setUp() throws IOException {
    when(youtubeServiceMock.commentThreads().list(any())).thenReturn(requestMock);
  }

  /**
   * A list of Strings should be returned for any public/unlisted video that has comments. 
   */
  @Test
  public void videoReturnsCommentsList() throws GeneralSecurityException, IOException {
    CommentThreadListResponse response = new CommentThreadListResponse();
    response.setItems(createCommentThreadList(new ArrayList<>(Arrays.asList("a", "b"))));

    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(VIDEO_ID)
        .execute())
        .thenReturn(response);
    
    List<String> expected = new ArrayList<>(Arrays.asList("a", "b"));
    
    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(expected, actual);
  }

  /**
   * If a video can't be accessed (ex. the video's ID doesn't refer to a video), an
   * IllegalArgumentException should be thrown as this is a user error.  
   */
  @Test(expected = IllegalArgumentException.class)
  public void inaccessibleVideoThrowsException() throws GeneralSecurityException, IOException {
    GoogleJsonError error =
        createExceptionDetails(new ArrayList<>(Arrays.asList("videoNotFound")),
            "Private video or invalid id");
    
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(VIDEO_ID)
        .execute())
        .thenThrow(exceptionMock);
    when(exceptionMock.getDetails()).thenReturn(error);

    commentService.getCommentsFromId(VIDEO_ID);
  }

  /**
   * If a video's comments are disabled, a GoogleJsonResponseException is caught and an empty list
   * will be returned. 
   */
  @Test
  public void disabledCommentsReturnsEmptyList() throws GeneralSecurityException, IOException {
    GoogleJsonError error =
        createExceptionDetails(new ArrayList<>(Arrays.asList("commentsDisabled")),
            "Video with disabled comments");
    
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(VIDEO_ID)
        .execute())
        .thenThrow(exceptionMock);
    when(exceptionMock.getDetails()).thenReturn(error);

    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(Arrays.asList(), actual);
  }

  /**
   * If a video's comments can't be accessed for any other reason (ex, processing failure), a
   * GoogleJsonResponseException should be thrown.  
   */
  @Test(expected = GoogleJsonResponseException.class)
  public void otherProcessingErrorThrowsException() throws GeneralSecurityException, IOException {
    GoogleJsonError error =
        createExceptionDetails(new ArrayList<>(Arrays.asList("testError")),
            "Other error detail (not commentsDisabled or videoNotFound)");
    
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(VIDEO_ID)
        .execute())
        .thenThrow(exceptionMock);
    when(exceptionMock.getDetails()).thenReturn(error);

    commentService.getCommentsFromId(VIDEO_ID);
  }

  /**
   * Videos with zero comments should return an empty list.
   */
  @Test
  public void noCommentsReturnsEmptyList() throws GeneralSecurityException, IOException {
    CommentThreadListResponse response = new CommentThreadListResponse();
    response.setItems(createCommentThreadList(new ArrayList<>(Arrays.asList())));

    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(VIDEO_ID)
        .execute())
        .thenReturn(response);
    
    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(Arrays.asList(), actual);
  }

  /**
   * Creates a list of CommentThread objects that stores comment texts specified by the parameter.
   * The returned list is set as the items of a CommentThreadListResponse. 
   */
  private List<CommentThread> createCommentThreadList(List<String> strList) {
    List<CommentThread> threadList = new ArrayList<>();

    for (String str : strList) {
      CommentThread thread = new CommentThread();
      thread.setSnippet(new CommentThreadSnippet()
          .setTopLevelComment(new Comment()
          .setSnippet(new CommentSnippet()
          .setTextDisplay(str))));
      threadList.add(thread);
    }

    return threadList;
  }

  /**
   * Creates and returns a GoogleJsonError that stores information about a
   * GoogleJsonResponseException (list of errors, reasons for each error, exception message) that's
   * needed for the tests. 
   */
  private GoogleJsonError createExceptionDetails(List<String> strList, String message) {
    List<GoogleJsonError.ErrorInfo> errorList = new ArrayList<>();

    for (String str : strList) {
      GoogleJsonError.ErrorInfo error = new ErrorInfo();
      error.setReason(str);
      errorList.add(error);
    }

    GoogleJsonError details = new GoogleJsonError();
    details.setErrors(errorList);
    details.setMessage(message);

    return details;
  }

}