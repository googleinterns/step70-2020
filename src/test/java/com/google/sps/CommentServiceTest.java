package com.google.sps;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
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
  @Mock(answer = Answers.RETURNS_DEEP_STUBS) CommentThread commentThreadMock;
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
    List<CommentThread> threadList =
        new ArrayList<>(Arrays.asList(commentThreadMock, commentThreadMock));
    
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(anyString())
        .execute())
        .thenReturn(responseMock);
    when(responseMock.getItems()).thenReturn(threadList);
    when(commentThreadMock.getSnippet().getTopLevelComment().getSnippet().getTextDisplay())
        .thenReturn("a", "b"); // Returns "a" first time, "b" second time.
    
    List<String> expected = new ArrayList<>(Arrays.asList("a", "b"));
    
    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(expected, actual);
  }

  /**
   * If a video's comments can't be accessed (ex. the video has disabled commenting), a
   * GoogleJsonResponseException should be caught and an empty list should be returned. 
   */
  @Test
  public void inaccessibleCommentsReturnsEmptyList() throws GeneralSecurityException, IOException {
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(anyString())
        .execute())
        .thenThrow(exceptionMock);
    when(exceptionMock.getDetails().getMessage()).thenReturn("Can't get comments");
    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(Arrays.asList(), actual);
  }

  /**
   * Videos with zero comments should return an empty list.
   */
  @Test
  public void noCommentsReturnsEmptyList() throws GeneralSecurityException, IOException {
    when(requestMock.setKey(anyString())
        .setMaxResults(anyLong())
        .setOrder("relevance")
        .setTextFormat("plainText")
        .setVideoId(anyString())
        .execute())
        .thenReturn(responseMock);
    when(responseMock.getItems()).thenReturn(new ArrayList<>());
    
    List<String> actual = commentService.getCommentsFromId(VIDEO_ID);

    Assert.assertEquals(Arrays.asList(), actual);
  }

}