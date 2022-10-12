package org.tog.togmobilemediaservice.dto;

import lombok.Data;

@Data
public class YoutubeVideoDataDto {
    private String id;
    private String title;
    private String description;
    private String publishedAt;
    private String videoUrl;
    private String thumbnailDefault;
    private String thumbnailMax;
    private String thumbnailMedium;
    private String thumbnailHigh;
    private String thumbnailStandard;
    private String[] tags;
}
