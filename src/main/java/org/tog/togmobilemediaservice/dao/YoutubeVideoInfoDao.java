package org.tog.togmobilemediaservice.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;

import java.util.Date;
import java.util.List;

@Repository
public interface YoutubeVideoInfoDao extends CrudRepository<YouTubeVideoInfo, String> {
    @Query(value = "from youtube_video_info t where publish_date BETWEEN :startDate AND :endDate")
    List<YouTubeVideoInfo> getAllBetweenDates(@Param("startDate")Date startDate, @Param("endDate") Date endDate);
}
