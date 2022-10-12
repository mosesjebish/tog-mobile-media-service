package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class YoutubeServiceImpl implements YoutubeService{
    public List<YoutubeVideoInfoDto> searchVideosByChannel(DateTime from, DateTime to) {
        List<YoutubeVideoInfoDto> videos = new ArrayList<YoutubeVideoInfoDto>();

        try {
            //instantiate youtube object
            YouTube youtube = YoutubeHelper.getService();

            //define what info we want to get
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setChannelId("UCrJIvOsuOk8y725itafCZbg");

            //we only want video results
            search.setType("video");

            //set the fields that we're going to use
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)");

            if(from != null){
                search.setPublishedAfter(from);
            }

            if(to != null){
                search.setPublishedAfter(to);
            }

            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

            //perform the search and parse the results
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                for (SearchResult result : searchResultList) {
                    YoutubeVideoInfoDto video = new YoutubeVideoInfoDto();
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

    public List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos){
        List<List<YoutubeVideoInfoDto>> chunkList = new ArrayList<>();
        List<YoutubeVideoDataDto> youtubeVideoDataResponseDtos = new ArrayList<>();

        if(youtubeVideoInfoDtos.size() < 50){
            chunkList.add(youtubeVideoInfoDtos);
        }
        else {
            chunkList = Lists.partition(youtubeVideoInfoDtos,25);
        }

        chunkList.forEach(aChunk -> {
            try {

                List<String> idList = aChunk.stream().map(YoutubeVideoInfoDto::getId).collect(Collectors.toList());
                String ids = String.join(",",idList);

                YouTube youtubeService = YoutubeHelper.getService();
                YouTube.Videos.List request = youtubeService.videos()
                        .list("contentDetails,fileDetails,id,snippet");
                VideoListResponse response = request.setId(ids).execute();


                response.getItems().forEach(anItem -> {
                    YoutubeVideoDataDto dto = new YoutubeVideoDataDto();
                    dto.setId(anItem.getId());
                    dto.setTitle(anItem.getSnippet().getTitle());
                    dto.setTags(anItem.getSnippet().getTags().toArray(new String[0]));
                    dto.setDescription(anItem.getSnippet().getDescription());
                    dto.setPublishedAt(anItem.getSnippet().getPublishedAt().toString());
                    dto.setVideoUrl(YoutubeHelper.buildVideoUrl(anItem.getId()));
                    dto.setThumbnailDefault(anItem.getSnippet().getThumbnails().getDefault().getUrl());
                    dto.setThumbnailMax(anItem.getSnippet().getThumbnails().getMaxres().getUrl());
                    dto.setThumbnailHigh(anItem.getSnippet().getThumbnails().getHigh().getUrl());
                    dto.setThumbnailMedium(anItem.getSnippet().getThumbnails().getMedium().getUrl());
                    dto.setThumbnailStandard(anItem.getSnippet().getThumbnails().getStandard().getUrl());

                    youtubeVideoDataResponseDtos.add(dto);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return youtubeVideoDataResponseDtos;
    }
}
