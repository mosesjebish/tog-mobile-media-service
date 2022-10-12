package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.common.base.Strings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dao.YoutubeVideoInfoDao;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.dto.YoutubeSearchRequestDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoInfoMapper;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    private final YoutubeService youtubeService;
    private final YoutubeVideoInfoDao dao;

    private YoutubeVideoInfoMapper mapper = Mappers.getMapper(YoutubeVideoInfoMapper.class);

    public VideoServiceImpl(YoutubeService youtubeService, YoutubeVideoInfoDao dao) {
        this.youtubeService = youtubeService;
        this.dao = dao;
    }


    @Override
    public List<YoutubeVideoInfoDto> search(YoutubeSearchRequestDto youtubeSearchRequestDto) {
        List<YouTubeVideoInfo> entities = mapper.mapDtosToEntities(youtubeService.searchVideosByChannel(getDate(youtubeSearchRequestDto.getFromDate()), getDate(youtubeSearchRequestDto.getToDate())));

        return mapper.mapEntitiesToDtos(Lists.newArrayList(dao.saveAll(entities)));
    }

    @Override
    public List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos) {
        Iterable<YouTubeVideoInfo> entities = dao.findAll();
        List<YoutubeVideoInfoDto> dtos = mapper.mapEntitiesToDtos(Lists.newArrayList(entities));
        return youtubeService.fetchVideoData(dtos);
    }

    private static DateTime getDate(String date) {
        if(Strings.isNullOrEmpty(date)){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime;
    }
}
