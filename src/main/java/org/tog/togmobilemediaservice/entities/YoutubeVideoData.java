package org.tog.togmobilemediaservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "youtube_video_data")
@Data
@EqualsAndHashCode
public class YoutubeVideoData {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "publishedAt")
    private String publishedAt;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "thumbnail_default")
    private String thumbnailDefault;

    @Column(name = "thumbnail_max")
    private String thumbnailMax;

    @Column(name = "thumbnail_medium")
    private String thumbnailMedium;

    @Column(name = "thumbnail_high")
    private String thumbnailHigh;

    @Column(name = "thumbnail_standard")
    private String thumbnailStandard;

    @Column(name = "tags")
    private String tags;
}