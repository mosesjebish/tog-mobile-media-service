package org.tog.togmobilemediaservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.tog.togmobilemediaservice.enums.Production;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "youtube_video_info")
@Data
@EqualsAndHashCode
public class YouTubeVideoInfo {

    @Column(name = "id")
    private String id;

    @Type(type = "jsonb")
    @Column(name = "tags")
    private String tags;

    @Type(type = "jsonb")
    @Column(name = "snippet")
    private String snippet;

    @Type(type = "jsonb")
    @Column(name = "file_details")
    private String fileDetails;

    @Column(name = "production")
    private Production production;
}