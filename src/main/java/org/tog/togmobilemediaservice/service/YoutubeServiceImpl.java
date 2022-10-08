package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dto.YouTubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class YoutubeServiceImpl implements YoutubeService{
    public List<YouTubeVideoInfoDto> searchVideosByChannel() {
        List<YouTubeVideoInfoDto> videos = new ArrayList<YouTubeVideoInfoDto>();

        try {
            //instantiate youtube object
            YouTube youtube = YoutubeHelper.getYouTube();

            //define what info we want to get
            YouTube.Search.List search = youtube.search().list("id,snippet");

            //set our credentials
            String apiKey = "AIzaSyBrRIBOVkferDzHZV1J_e__2ceiBm8Uzm4";
            search.setKey(apiKey);
            search.setChannelId("UCrJIvOsuOk8y725itafCZbg");
            //we only want video results
            search.setType("video");

            //set the fields that we're going to use
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)");

            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

            //perform the search and parse the results
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                for (SearchResult result : searchResultList) {
                    YouTubeVideoInfoDto video = new YouTubeVideoInfoDto();
                    video.setId(result.getId().getVideoId());
                    if(!Objects.isNull(result.getSnippet())){
                        video.setDescription(result.getSnippet().getDescription());
                        video.setTitle(result.getSnippet().getTitle());
                    }
                    DateTime dateTime = result.getSnippet().getPublishedAt();
                    Date date = new Date(dateTime.getValue());
                    String dateString = df.format(date);
                    video.setPublishDate(dateString);
                    videos.add(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videos;
    }
}
