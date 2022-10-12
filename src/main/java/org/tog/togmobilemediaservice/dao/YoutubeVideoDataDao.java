package org.tog.togmobilemediaservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;

public interface YoutubeVideoDataDao extends CrudRepository<YoutubeVideoData, String> {
}
