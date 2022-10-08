package org.tog.togmobilemediaservice.service;

import org.tog.togmobilemediaservice.dto.YouTubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;

import java.util.List;

public interface YoutubeService {
    public List<YouTubeVideoInfoDto> searchVideosByChannel();
}
