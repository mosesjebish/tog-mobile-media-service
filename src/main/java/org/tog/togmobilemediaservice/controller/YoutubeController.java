package org.tog.togmobilemediaservice.controller;

import org.springframework.web.bind.annotation.*;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.dto.YoutubeSearchRequestDto;
import org.tog.togmobilemediaservice.service.VideoService;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/youtube")
public class YoutubeController {
    private final VideoService videoService;

    public YoutubeController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(value = "/search")
    public List<YoutubeVideoInfoDto> search(@RequestBody YoutubeSearchRequestDto youtubeSearchRequestDto) {
        List<YoutubeVideoInfoDto> youTubeVideoInfoList = videoService.search(youtubeSearchRequestDto);
       return youTubeVideoInfoList;
    }

    @PostMapping(value = "/data")
    public List<YoutubeVideoDataDto> data(@RequestBody YoutubeSearchRequestDto youtubeSearchRequestDto) {
        List<YoutubeVideoDataDto> youtubeVideoDataDtos = videoService.fetchVideoData(Collections.EMPTY_LIST);
        return youtubeVideoDataDtos;
    }
}
