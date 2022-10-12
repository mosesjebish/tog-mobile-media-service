package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.DateTime;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;

import java.util.List;

public interface YoutubeService {
    public List<YoutubeVideoInfoDto> searchVideosByChannel(DateTime from, DateTime to);
    List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos);
}
