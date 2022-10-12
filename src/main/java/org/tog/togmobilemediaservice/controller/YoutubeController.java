package org.tog.togmobilemediaservice.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
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

    @PostMapping(value = "/search")
    @ApiResponse(responseCode = "200", description = "Searches and updates the video list from BAG youtube channel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoInfoDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public List<YoutubeVideoInfoDto> search(@RequestBody VideoRequestDto videoRequestDto) {
        List<YoutubeVideoInfoDto> youTubeVideoInfoList = videoService.search(videoRequestDto);
       return youTubeVideoInfoList;
    }

    @PostMapping(value = "/data")
    @ApiResponse(responseCode = "200", description = "Connects with YoutubeAPI and populates exhaustive list of data in database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoDataDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public List<YoutubeVideoDataDto> data(@RequestBody VideoRequestDto videoRequestDto) throws Exception {
        List<YoutubeVideoDataDto> youtubeVideoDataDtos = videoService.fetchVideoData(videoRequestDto);
        return youtubeVideoDataDtos;
    }
}
