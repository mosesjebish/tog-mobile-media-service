package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.Lists;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dao.YoutubeVideoDataDao;
import org.tog.togmobilemediaservice.dao.YoutubeVideoInfoDao;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;
import org.tog.togmobilemediaservice.external.YoutubeService;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoDataMapper;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoInfoMapper;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public List<YoutubeVideoInfoDto> search(VideoRequestDto videoRequestDto) throws MediaServiceException {
        List<YouTubeVideoInfo> entities = infoMapper.mapDtosToEntities(youtubeService.searchVideosByDate(getRfc3339Date(videoRequestDto.getFromDate()), getRfc3339Date(videoRequestDto.getToDate())));

        return infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoDao.saveAll(entities)));
    }

    @Override
    public List<YoutubeVideoDataDto> fetchVideoData(VideoRequestDto videoRequestDto) throws MediaServiceException {
        Iterable<YouTubeVideoInfo> infoEntities = getYouTubeVideoInfos(videoRequestDto);

        List<YoutubeVideoInfoDto> infoDtos = infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoEntities));

        List<YoutubeVideoDataDto> dataDtos =  youtubeService.fetchVideoData(infoDtos);

        List<YoutubeVideoData> dataEntities = dataMapper.mapDtosToEntities(dataDtos);
        return dataMapper.mapEntitiesToDtos(Lists.newArrayList(dataDao.saveAll(dataEntities)));
    }

    private Iterable<YouTubeVideoInfo> getYouTubeVideoInfos(VideoRequestDto videoRequestDto) throws MediaServiceException {
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

            Date fromDate = new Date(getRfc3339Date(videoRequestDto.getFromDate()).getValue());
            Date toDate = new Date(getRfc3339Date(videoRequestDto.getToDate()).getValue());

            if (fromDate.after(toDate)) {
                throw new MediaServiceException(HttpStatus.BAD_REQUEST.value(),"Invalid from/to date");
            }

            infoEntities = infoDao.getAllBetweenDates(fromDate,toDate);
        }
        return infoEntities;
    }

    private static com.google.api.client.util.DateTime getRfc3339Date(String date) throws MediaServiceException {
        if(Strings.isNullOrEmpty(date)){
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(date, formatter);

            DateTime dt = new DateTime(dateTime.getYear(),dateTime.getMonthValue(),dateTime.getDayOfMonth(),0,0,0,0, DateTimeZone.UTC);
            org.joda.time.format.DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            String outRfc = fmt.print(dt);

            return com.google.api.client.util.DateTime.parseRfc3339(outRfc);
        }
        catch (Exception e){
            throw new MediaServiceException(HttpStatus.BAD_REQUEST.value(),"Invalid date format. Expected format yyyy-mm-dd");
        }

    }
}
