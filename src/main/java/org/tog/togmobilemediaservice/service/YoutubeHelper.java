package org.tog.togmobilemediaservice.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YoutubeHelper {
    /**
     * Constructs the URL to play the YouTube video
     */
    public static String buildVideoUrl(String videoId) {
        StringBuilder builder = new StringBuilder();
        builder.append("https://www.youtube.com/watch?v=");
        builder.append(videoId);

        return builder.toString();
    }

    /**
     * Instantiates the YouTube object
     */
    public static YouTube getYouTube() {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                (reqeust) -> {}).setApplicationName("tog-media-service").build();

        return youtube;
    }
}
