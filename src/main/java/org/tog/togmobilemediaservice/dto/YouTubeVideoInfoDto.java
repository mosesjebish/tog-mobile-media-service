package org.tog.togmobilemediaservice.dto;

import lombok.Data;

@Data
public class YouTubeVideoInfoDto {
    private String id;

    private String tags;

    private YouTubeVideoSnippet snippet;

    private FileDetails fileDetails;
}
