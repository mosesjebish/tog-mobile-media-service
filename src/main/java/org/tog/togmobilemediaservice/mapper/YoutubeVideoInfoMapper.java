package org.tog.togmobilemediaservice.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.tog.togmobilemediaservice.dto.YoutubeVideoInfoDto;
import org.tog.togmobilemediaservice.entities.YouTubeVideoInfo;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class YoutubeVideoInfoMapper {
//    public abstract YouTubeVideoInfo mapDtoToEntity(YouTubeVideoInfoDto dto);
//
//    @InheritInverseConfiguration(name = "mapDtoToEntity")
//    public abstract YouTubeVideoInfoDto mapEntityToDto(YouTubeVideoInfo entity);

    public abstract List<YouTubeVideoInfo> mapDtosToEntities(List<YoutubeVideoInfoDto> dtos);

    @InheritInverseConfiguration(name = "mapDtosToEntities")
    public abstract List<YoutubeVideoInfoDto> mapEntitiesToDtos(List<YouTubeVideoInfo> entities);
}
