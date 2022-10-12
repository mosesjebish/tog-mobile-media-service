package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.common.base.Strings;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dao.YoutubeVideoDataDao;
import org.tog.togmobilemediaservice.dao.YoutubeVideoInfoDao;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;
import org.tog.togmobilemediaservice.external.YoutubeService;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoDataMapper;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoInfoMapper;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    private final YoutubeService youtubeService;
    private final YoutubeVideoInfoDao infoDao;

    private final YoutubeVideoDataDao dataDao;

    private YoutubeVideoInfoMapper infoMapper = Mappers.getMapper(YoutubeVideoInfoMapper.class);

    private YoutubeVideoDataMapper dataMapper = Mappers.getMapper(YoutubeVideoDataMapper.class);

    public VideoServiceImpl(YoutubeService youtubeService, YoutubeVideoInfoDao infoDao, YoutubeVideoDataDao dataDao) {
        this.youtubeService = youtubeService;
        this.infoDao = infoDao;
        this.dataDao = dataDao;
    }


    @Override
    public List<YoutubeVideoInfoDto> search(VideoRequestDto videoRequestDto) {
        List<YouTubeVideoInfo> entities = infoMapper.mapDtosToEntities(youtubeService.searchVideosByDate(getDate(videoRequestDto.getFromDate()), getDate(videoRequestDto.getToDate())));

        return infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoDao.saveAll(entities)));
    }

    @Override
    public List<YoutubeVideoDataDto> fetchVideoData(VideoRequestDto videoRequestDto) throws Exception {
        Iterable<YouTubeVideoInfo> infoEntities = getYouTubeVideoInfos(videoRequestDto);

        List<YoutubeVideoInfoDto> infoDtos = infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoEntities));

        List<YoutubeVideoDataDto> dataDtos =  youtubeService.fetchVideoData(infoDtos);

        List<YoutubeVideoData> dataEntities = dataMapper.mapDtosToEntities(dataDtos);
        return dataMapper.mapEntitiesToDtos(Lists.newArrayList(dataDao.saveAll(dataEntities)));
    }

    private Iterable<YouTubeVideoInfo> getYouTubeVideoInfos(VideoRequestDto videoRequestDto) throws Exception {
        Iterable<YouTubeVideoInfo> infoEntities;
        if(Strings.isNullOrEmpty(videoRequestDto.getFromDate())  && Strings.isNullOrEmpty(videoRequestDto.getToDate())){
            infoEntities = infoDao.findAll();
        }
        else {
            if(Strings.isNullOrEmpty(videoRequestDto.getFromDate())){
                videoRequestDto.setFromDate("1990-01-01T12:00:00Z");
            }

            if(Strings.isNullOrEmpty(videoRequestDto.getToDate())){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
                Date date = new Date();
                videoRequestDto.setToDate(formatter.format(date));
            }

            Date fromDate = new Date(getDate(videoRequestDto.getFromDate()).getValue());
            Date toDate = new Date(getDate(videoRequestDto.getToDate()).getValue());

            if (fromDate.after(toDate)) {
                throw new Exception("Invalid Date Time");
            }

            infoEntities = infoDao.getAllBetweenDates(fromDate,toDate);
        }
        return infoEntities;
    }

    private static DateTime getDate(String date) {
        if(Strings.isNullOrEmpty(date)){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime;
    }
}
