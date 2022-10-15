package org.tog.togmobilemediaservice.service;

import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoRequestDto;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;

import java.util.List;

public interface VideoService {
    List<YoutubeVideoInfoDto> searchYoutube(YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException;
    List<YoutubeVideoDataDto> fetchYoutubeData(YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException;

    List<YoutubeVideoDataDto> searchVideosByCategories(VideoRequestDto videoRequestDto) throws MediaServiceException;
}
