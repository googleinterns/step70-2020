package com.google.sps.servlets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class YouTubeServiceWrapper {
    private final String DEVELOPER_KEY = "DEV_KEY";
    private final Credential CREDENTIAL;
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public YouTubeServiceWrapper(Credential credential){
        this.CREDENTIAL = credential;
    }

    private YouTube getService() throws GeneralSecurityException, IOException{
        if (CREDENTIAL == null){
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            return new YouTube.Builder(httpTransport, JSON_FACTORY, null).build();
        }
        return new YouTube.Builder(CREDENTIAL.getTransport(), CREDENTIAL.getJsonFactory(), CREDENTIAL).build();
    }

    // This is an example of an API call that does not requires OAuth2.0
    public List<String> getCommentsFromVideoID (String videoID) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        YouTube.CommentThreads.List req = youtubeService.commentThreads().list(Collections.singletonList("snippet"));
        CommentThreadListResponse res = req.setKey(DEVELOPER_KEY).setVideoId(videoID).execute();

        List<String> ret = new ArrayList<>();
        for (CommentThread commentThread :res.getItems()){
            String commentContent = commentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay();
            ret.add(commentContent);
        }
        return ret;
    }

    // This is an example of an API call that requires OAuth2.0. The credential will be generated in YTAuthServlet.
    public String getMyChannelDetail() throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        YouTube.Channels.List req = youtubeService.channels().list(Arrays.asList("snippet", "contentDetails"));
        ChannelListResponse res = req.setMine(true).execute();
        return res.getItems().toString();
    }

    // More functions can be added here.
}
