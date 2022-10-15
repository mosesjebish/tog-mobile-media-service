package org.tog.togmobilemediaservice.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tog.togmobilemediaservice.dto.YoutubeVideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;
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

    Logger logger = LoggerFactory.getLogger(YoutubeController.class);

    @PostMapping(value = "/search")
    @ApiResponse(responseCode = "200", description = "Searches and updates the video list from BAG youtube channel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoInfoDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<List<YoutubeVideoInfoDto>> search(@RequestBody YoutubeVideoRequestDto youtubeVideoRequestDto) {
        try {
            List<YoutubeVideoInfoDto> youTubeVideoInfoList = videoService.searchYoutube(youtubeVideoRequestDto);
            return new ResponseEntity<>(youTubeVideoInfoList, HttpStatus.OK);
        } catch (MediaServiceException e) {
            logger.error("Error while searching youtube: Status: {}, Reason: {}",e.getErrCode(),e.getMessage());
            if (e.getErrCode() == HttpStatus.BAD_REQUEST.value()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/data")
    @ApiResponse(responseCode = "200", description = "Connects with YoutubeAPI and populates exhaustive list of data in database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoDataDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<List<YoutubeVideoDataDto>> data(@RequestBody YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException {
        try {
            List<YoutubeVideoDataDto> youtubeVideoDataDtos = videoService.fetchYoutubeData(youtubeVideoRequestDto);
            return new ResponseEntity<>(youtubeVideoDataDtos, HttpStatus.OK);
        } catch (MediaServiceException e) {
            if (e.getErrCode() == HttpStatus.BAD_REQUEST.value()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
