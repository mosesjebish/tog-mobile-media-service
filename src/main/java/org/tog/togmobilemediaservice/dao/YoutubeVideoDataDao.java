package org.tog.togmobilemediaservice.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;

import java.util.Date;
import java.util.List;

@Repository
public interface YoutubeVideoDataDao extends CrudRepository<YoutubeVideoData, String> {
    @Query(value = "from youtube_video_data t where published_at BETWEEN :startDate AND :endDate")
    List<YoutubeVideoData> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
