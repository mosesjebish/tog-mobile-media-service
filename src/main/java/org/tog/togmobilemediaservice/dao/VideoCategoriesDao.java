package org.tog.togmobilemediaservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tog.togmobilemediaservice.entities.VideoCategory;

@Repository
public interface VideoCategoriesDao extends CrudRepository<VideoCategory, String> {
}
