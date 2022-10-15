package org.tog.togmobilemediaservice.service;

import com.google.api.client.util.Lists;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tog.togmobilemediaservice.dao.VideoCategoriesDao;
import org.tog.togmobilemediaservice.dao.YoutubeVideoDataDao;
import org.tog.togmobilemediaservice.dao.YoutubeVideoInfoDao;
import org.tog.togmobilemediaservice.dto.VideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoRequestDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.VideoCategory;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;
import org.tog.togmobilemediaservice.exceptions.MediaServiceException;
import org.tog.togmobilemediaservice.external.YoutubeService;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoDataMapper;
import org.tog.togmobilemediaservice.mapper.YoutubeVideoInfoMapper;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    private final YoutubeService youtubeService;
    private final YoutubeVideoInfoDao infoDao;

    private final YoutubeVideoDataDao dataDao;

    private final VideoCategoriesDao videoCategoriesDao;

    private YoutubeVideoInfoMapper infoMapper = Mappers.getMapper(YoutubeVideoInfoMapper.class);

    private YoutubeVideoDataMapper dataMapper = Mappers.getMapper(YoutubeVideoDataMapper.class);

    public VideoServiceImpl(YoutubeService youtubeService, YoutubeVideoInfoDao infoDao, YoutubeVideoDataDao dataDao, VideoCategoriesDao videoCategoriesDao) {
        this.youtubeService = youtubeService;
        this.infoDao = infoDao;
        this.dataDao = dataDao;
        this.videoCategoriesDao = videoCategoriesDao;
    }


    @Override
    public List<YoutubeVideoInfoDto> searchYoutube(YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException {
        List<YouTubeVideoInfo> entities = infoMapper.mapDtosToEntities(youtubeService.searchVideosByDate(getRfc3339Date(youtubeVideoRequestDto.getFromDate()), getRfc3339Date(youtubeVideoRequestDto.getToDate())));

        return infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoDao.saveAll(entities)));
    }

    @Override
    public List<YoutubeVideoDataDto> fetchYoutubeData(YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException {
        Iterable<YouTubeVideoInfo> infoEntities = getYouTubeVideoInfos(youtubeVideoRequestDto);

        List<YoutubeVideoInfoDto> infoDtos = infoMapper.mapEntitiesToDtos(Lists.newArrayList(infoEntities));

        List<YoutubeVideoDataDto> dataDtos =  youtubeService.fetchVideoData(infoDtos);

        List<YoutubeVideoData> dataEntities = dataMapper.mapDtosToEntities(dataDtos);
        return dataMapper.mapEntitiesToDtos(Lists.newArrayList(dataDao.saveAll(dataEntities)));
    }

    @Override
    public List<YoutubeVideoDataDto> searchVideosByCategories(VideoRequestDto videoRequestDto) throws MediaServiceException {
        Iterable<YoutubeVideoData> dataEntities = getYoutubeVideoData(videoRequestDto);

        List<YoutubeVideoDataDto> allVideosForDateRange = dataMapper.mapEntitiesToDtos(Lists.newArrayList(dataEntities));

        if(Strings.isNullOrEmpty(videoRequestDto.getCategory())){
            return allVideosForDateRange;
        }

        Optional<VideoCategory> videoCategory = videoCategoriesDao.findById(videoRequestDto.getCategory());

        if(!videoCategory.isPresent()){
            throw new MediaServiceException(HttpStatus.NO_CONTENT.value(), "No valid categories found!");
        }

        List<String> tags = Arrays.asList(videoCategory.get().getTags().split(","));

        List<YoutubeVideoDataDto> filteredList = new ArrayList<>();
        allVideosForDateRange.forEach(aDto -> {
            List<String> aDtoTags = Arrays.asList(aDto.getTags().split(","));
            if(tags.stream().anyMatch(element -> aDtoTags.contains(element.trim()))){
                filteredList.add(aDto);
            }
        });

        return filteredList;
    }

    private Iterable<YoutubeVideoData> getYoutubeVideoData(VideoRequestDto videoRequestDto) throws MediaServiceException {
        Iterable<YoutubeVideoData> dataEntities;
        if(Strings.isNullOrEmpty(videoRequestDto.getFromDate())  && Strings.isNullOrEmpty(videoRequestDto.getToDate())){
            dataEntities = dataDao.findAll();
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

            dataEntities = dataDao.getAllBetweenDates(fromDate,toDate);
        }

        return dataEntities;
    }

    private Iterable<YouTubeVideoInfo> getYouTubeVideoInfos(YoutubeVideoRequestDto youtubeVideoRequestDto) throws MediaServiceException {
        Iterable<YouTubeVideoInfo> infoEntities;
        if(Strings.isNullOrEmpty(youtubeVideoRequestDto.getFromDate())  && Strings.isNullOrEmpty(youtubeVideoRequestDto.getToDate())){
            infoEntities = infoDao.findAll();
        }
        else {
            if(Strings.isNullOrEmpty(youtubeVideoRequestDto.getFromDate())){
                youtubeVideoRequestDto.setFromDate("1990-01-01T12:00:00Z");
            }

            if(Strings.isNullOrEmpty(youtubeVideoRequestDto.getToDate())){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
                Date date = new Date();
                youtubeVideoRequestDto.setToDate(formatter.format(date));
            }

            Date fromDate = new Date(getRfc3339Date(youtubeVideoRequestDto.getFromDate()).getValue());
            Date toDate = new Date(getRfc3339Date(youtubeVideoRequestDto.getToDate()).getValue());

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
