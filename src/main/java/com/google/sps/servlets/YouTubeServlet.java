package com.google.sps.servlets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/yt")
public class YouTubeServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Queries that does not require OAuth2.0 can be called this way
        String id = "kNovwPIWr3Q"; // <- sample video id
        List<String> ret = null;
        try {
            ret = new YouTubeServiceWrapper(null).getCommentsFromVideoID(id);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json;");
        response.getWriter().println(ret);
    }
}
