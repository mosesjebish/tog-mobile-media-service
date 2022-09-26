package org.tog.togmobilemediaservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "video_categories")
@Data
@EqualsAndHashCode
public class VideoCategories {

    @Column(name = "categories")
    private VideoCategories videoCategories;

    @Column(name = "sub_categories")
    private String subCategories;

}
