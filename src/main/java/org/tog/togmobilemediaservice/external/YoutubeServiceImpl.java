package org.tog.togmobilemediaservice.external;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class YoutubeServiceImpl implements YoutubeService {

    static final String APPLICATION_NAME = "tog-mobile-media-service";

    static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final String CONTENT_DETAILS_FILE_DETAILS_ID_SNIPPET = "contentDetails,fileDetails,id,snippet";

    @Value( "${youtube.client-id}" )
    private String youtubeClientId;

    @Value( "${youtube.client-secret}" )
    private String youtubeClientSecret;

    @Value( "${youtube.refresh-token}" )
    private String youtubeRefreshToken;

    @Value( "${youtube.channel-id}" )
    private String youtubeChannelId;

    Logger logger = LoggerFactory.getLogger(YoutubeServiceImpl.class);

    public List<YoutubeVideoInfoDto> searchVideosByDate(DateTime from, DateTime to) throws MediaServiceException {
        List<YoutubeVideoInfoDto> videos = new ArrayList<YoutubeVideoInfoDto>();

        try {
            //instantiate youtube object
            YouTube youtube = getService();

            //define what info we want to get
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setChannelId(youtubeChannelId);

            //we only want video results
            search.setType("video");

            //set the fields that we're going to use
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)");

            if (from != null) {
                search.setPublishedAfter(from);
            }

            if (to != null) {
                search.setPublishedBefore(to);
            }

            //perform the search and parse the results
            SearchListResponse searchResponse = search.execute();
            if (searchResponse.getItems() != null) {
                List<SearchResult> searchResultList = searchResponse.getItems();
                logger.info("Fetched {} video(s) from your Youtube Channel", searchResultList.size());
                for (SearchResult result : searchResultList) {
                    YoutubeVideoInfoDto video = new YoutubeVideoInfoDto();
                    video.setId(result.getId().getVideoId());
                    if (!Objects.isNull(result.getSnippet())) {
                        video.setDescription(result.getSnippet().getDescription());
                        video.setTitle(result.getSnippet().getTitle());
                    }
                    DateTime dateTime = result.getSnippet().getPublishedAt();
                    Date publishDate = new Date(dateTime.getValue());
                    video.setPublishDate(publishDate);
                    videos.add(video);
                }
            }
            return videos;
        } catch (Exception e) {
            logger.error("Error while searching videos from Youtube");
            throw new MediaServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while searching videos from youtube");
        }
    }

    public List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos) {
        List<List<YoutubeVideoInfoDto>> chunkList = new ArrayList<>();
        List<YoutubeVideoDataDto> youtubeVideoDataResponseDtos = new ArrayList<>();

        if (youtubeVideoInfoDtos.size() < 50) {
            chunkList.add(youtubeVideoInfoDtos);
        } else {
            chunkList = Lists.partition(youtubeVideoInfoDtos, 25);
        }

        chunkList.forEach(aChunk -> {
            try {

                List<String> idList = aChunk.stream().map(YoutubeVideoInfoDto::getId).collect(Collectors.toList());
                String ids = String.join(",", idList);

                YouTube youtubeService = getService();
                YouTube.Videos.List request = youtubeService.videos()
                        .list(CONTENT_DETAILS_FILE_DETAILS_ID_SNIPPET);
                VideoListResponse response = request.setId(ids).execute();


                response.getItems().forEach(anItem -> {
                    YoutubeVideoDataDto dto = new YoutubeVideoDataDto();
                    dto.setId(anItem.getId());
                    dto.setVideoUrl(YoutubeHelper.buildVideoUrl(anItem.getId()));
                    if (anItem.getSnippet() != null) {
                        dto.setTitle(anItem.getSnippet().getTitle());
                        if (anItem.getSnippet().getTags().size() > 0) {
                            dto.setTags(String.join(",", anItem.getSnippet().getTags()));
                        }
                        dto.setDescription(anItem.getSnippet().getDescription());
                        DateTime dateTime = anItem.getSnippet().getPublishedAt();
                        Date publishDate = new Date(dateTime.getValue());
                        dto.setPublishedAt(publishDate);
                        dto.setThumbnailDefault(anItem.getSnippet().getThumbnails().getDefault().getUrl());
                        dto.setThumbnailMax(anItem.getSnippet().getThumbnails().getMaxres().getUrl());
                        dto.setThumbnailHigh(anItem.getSnippet().getThumbnails().getHigh().getUrl());
                        dto.setThumbnailMedium(anItem.getSnippet().getThumbnails().getMedium().getUrl());
                        dto.setThumbnailStandard(anItem.getSnippet().getThumbnails().getStandard().getUrl());
                    }

                    youtubeVideoDataResponseDtos.add(dto);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return youtubeVideoDataResponseDtos;
    }


    public Credential authorize() throws IOException {
        String clientID = youtubeClientId;
        String clientSecret = youtubeClientSecret;
        String refreshToken = youtubeRefreshToken;
        Credential credential = new GoogleCredential.Builder().setTransport(new NetHttpTransport())
                .setJsonFactory(new JacksonFactory())
                .setClientSecrets(clientID, clientSecret)
                .build();
        credential.setRefreshToken(refreshToken);
        return credential;
    }
    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
