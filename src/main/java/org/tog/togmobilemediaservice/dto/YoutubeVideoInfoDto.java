package org.tog.togmobilemediaservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class YoutubeVideoInfoDto {
    private String id;

    private String title;

    private String description;

    private Date publishDate;
}
