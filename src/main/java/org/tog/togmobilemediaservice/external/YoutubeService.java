package org.tog.togmobilemediaservice.external;

import com.google.api.client.util.DateTime;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;

import java.util.List;

public interface YoutubeService {
    public List<YoutubeVideoInfoDto> searchVideosByDate(DateTime from, DateTime to);
    List<YoutubeVideoDataDto> fetchVideoData(List<YoutubeVideoInfoDto> youtubeVideoInfoDtos);
}
