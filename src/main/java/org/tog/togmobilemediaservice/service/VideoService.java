package org.tog.togmobilemediaservice.service;

import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;

import java.util.List;

public interface VideoService {
    List<YoutubeVideoInfoDto> search(VideoRequestDto videoRequestDto);
    List<YoutubeVideoDataDto> fetchVideoData(VideoRequestDto videoRequestDto) throws Exception;
}
