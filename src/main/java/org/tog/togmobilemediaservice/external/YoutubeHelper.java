package org.tog.togmobilemediaservice.external;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class YoutubeHelper {

    static final String APPLICATION_NAME = "tog-mobile-media-service";

    static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
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
    public static YouTube getYouTube() throws GeneralSecurityException, IOException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        YouTube youtube = new YouTube.Builder(httpTransport, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("YoutubeVideoInfo")
                .setYouTubeRequestInitializer
                        (new YouTubeRequestInitializer("AIzaSyBrRIBOVkferDzHZV1J_e__2ceiBm8Uzm4")).build();

        return youtube;
    }

    public static Credential authorize() throws IOException {
        String clientID = "966383157976-ea52ob0na9lkga77ib4ssg0adbbu81or.apps.googleusercontent.com";
        String clientSecret = "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";
        String refreshToken = "1//04KcKsYCdXxm7CgYIARAAGAQSNwF-L9IrdpdgSTXEDQyp3nxlRMQkbwhYY38PR0WnK0oDULzQBji6aRcNSPk_KQus69CavtM2mxY";
        Credential credential = new GoogleCredential.Builder().setTransport(new NetHttpTransport())
                .setJsonFactory(new JacksonFactory())
                .setClientSecrets(clientID, clientSecret)
                .build();
        credential.setRefreshToken(refreshToken);
        return credential;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
