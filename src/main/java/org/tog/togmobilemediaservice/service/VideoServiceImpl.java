package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.Lists;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dao.YoutubeVideoInfoDao;
import org.tog.togmobilemediaservice.dto.YouTubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoInfoMapper;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService{
    private final YoutubeService youtubeService;
    private final YoutubeVideoInfoDao dao;

    private YoutubeVideoInfoMapper mapper = Mappers.getMapper(YoutubeVideoInfoMapper.class);

    public VideoServiceImpl(YoutubeService youtubeService, YoutubeVideoInfoDao dao) {
        this.youtubeService = youtubeService;
        this.dao = dao;
    }

    @Override
    public List<YouTubeVideoInfoDto> getVideoInfoFromYoutube() {
        List<YouTubeVideoInfo> entities = mapper.mapDtosToEntities(youtubeService.searchVideosByChannel());
        return mapper.mapEntitiesToDtos(Lists.newArrayList(dao.saveAll(entities)));
    }
}
