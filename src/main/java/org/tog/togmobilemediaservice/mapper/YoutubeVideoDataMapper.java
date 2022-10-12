package org.tog.togmobilemediaservice.mapper;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.tog.togmobilemediaservice.dto.YoutubeVideoDataDto;
import org.tog.togmobilemediaservice.entities.YoutubeVideoData;

import java.lang.reflect.Type;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public abstract class YoutubeVideoDataMapper {
//
//    public abstract YoutubeVideoData mapDtoToEntity(YoutubeVideoDataDto dto);
//
//    @InheritInverseConfiguration(name = "mapDtoToEntity")
//    public abstract YoutubeVideoDataDto mapEntityToDto(YoutubeVideoData entity);

    public abstract List<YoutubeVideoData> mapDtosToEntities(List<YoutubeVideoDataDto> dtos);

    @InheritInverseConfiguration(name = "mapDtosToEntities")
    public abstract List<YoutubeVideoDataDto> mapEntitiesToDtos(List<YoutubeVideoData> entities);

//    @AfterMapping
//    public void mapDtoExtraInfo(YoutubeVideoDataDto source, @MappingTarget YoutubeVideoData target){
//        if(source.getTags().length > 0){
//            target.setTags(new Gson().toJson(source.getTags()));
//        }
//    }
//
//    @AfterMapping
//    public void mapEntityExtraInfo(YoutubeVideoData source, @MappingTarget YoutubeVideoDataDto target){
//        if(!Strings.isNullOrEmpty(source.getTags())){
//            Type typeOfTags = new TypeToken<List<String>>() {
//            }.getType();
//            target.setTags(new Gson().fromJson(source.getTags(), typeOfTags));
//        }
//    }
}
