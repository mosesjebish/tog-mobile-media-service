package org.tog.togmobilemediaservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "video_categories")
@Data
@EqualsAndHashCode
public class VideoCategory {
    @Id
    @Column(name = "video_category", unique = true)
    private String videoCategory;

    @Column(name = "tags")
    private String tags;
}
