package org.tog.togmobilemediaservice.service;

import org.tog.togmobilemediaservice.dto.YouTubeVideoInfoDto;

import java.util.List;

public interface VideoService {
    public List<YouTubeVideoInfoDto> getVideoInfoFromYoutube();
}
