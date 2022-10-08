package org.tog.togmobilemediaservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tog.togmobilemediaservice.dto.YouTubeVideoInfoDto;
import org.tog.togmobilemediaservice.service.VideoService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/youtube")
public class YoutubeController {
    private final VideoService videoService;

    public YoutubeController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping(value = "/")
    public List<YouTubeVideoInfoDto> getUserInfo() {
        List<YouTubeVideoInfoDto> youTubeVideoInfoList = videoService.getVideoInfoFromYoutube();
       return youTubeVideoInfoList;
    }
}
