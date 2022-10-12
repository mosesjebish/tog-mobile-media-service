package org.tog.togmobilemediaservice.service;

import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.dto.YoutubeSearchRequestDto;

import java.util.List;

public interface VideoService {
    public List<YoutubeVideoInfoDto> search(YoutubeSearchRequestDto youtubeSearchRequestDto);
    List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos);
}
