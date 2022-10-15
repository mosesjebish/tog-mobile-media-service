package org.tog.togmobilemediaservice.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;
import org.tog.togmobilemediaservice.service.VideoService;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(value = "/search")
    @ApiResponse(responseCode = "200", description = "Connects with YoutubeAPI and populates exhaustive list of data in database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = YoutubeVideoDataDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<List<YoutubeVideoDataDto>> search(@RequestBody VideoRequestDto videoRequestDto) throws MediaServiceException {
        try {
            List<YoutubeVideoDataDto> youtubeVideoDataDtos = videoService.searchVideosByCategories(videoRequestDto);
            return new ResponseEntity<>(youtubeVideoDataDtos, HttpStatus.OK);
        } catch (MediaServiceException e) {
            if (e.getErrCode() == HttpStatus.BAD_REQUEST.value()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
