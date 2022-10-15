package org.tog.togmobilemediaservice.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;
import org.tog.togmobilemediaservice.service.VideoService;

import java.util.ArrayList;
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
    @ApiResponse(responseCode = "200", description = "Searches and updates the video list from BAG youtube channel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoInfoDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<List<YoutubeVideoInfoDto>> search(@RequestBody VideoRequestDto videoRequestDto) {
        try {
            List<YoutubeVideoInfoDto> youTubeVideoInfoList = videoService.search(videoRequestDto);
            return new ResponseEntity<>(youTubeVideoInfoList, HttpStatus.OK);
        } catch (MediaServiceException e) {
            if (e.getErrCode() == HttpStatus.BAD_REQUEST.value()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/data")
    @ApiResponse(responseCode = "200", description = "Connects with YoutubeAPI and populates exhaustive list of data in database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoDataDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<List<YoutubeVideoDataDto>> data(@RequestBody VideoRequestDto videoRequestDto) throws MediaServiceException {
        try {
            List<YoutubeVideoDataDto> youtubeVideoDataDtos = videoService.fetchVideoData(videoRequestDto);
            return new ResponseEntity<>(youtubeVideoDataDtos, HttpStatus.OK);
        } catch (MediaServiceException e) {
            if (e.getErrCode() == HttpStatus.BAD_REQUEST.value()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
