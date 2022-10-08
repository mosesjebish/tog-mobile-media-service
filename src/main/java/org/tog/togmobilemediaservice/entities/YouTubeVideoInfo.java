package org.tog.togmobilemediaservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.tog.togmobilemediaservice.enums.Production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "youtube_video_info")
@Data
@EqualsAndHashCode
public class YouTubeVideoInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "publish_date")
    private String publishDate;
}