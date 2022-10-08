package org.tog.togmobilemediaservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.service.YoutubeService;

import java.util.List;

@Repository
public interface YoutubeVideoInfoDao extends CrudRepository<YouTubeVideoInfo, String> {
}
