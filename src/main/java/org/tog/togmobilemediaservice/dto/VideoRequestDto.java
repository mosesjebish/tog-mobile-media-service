package org.tog.togmobilemediaservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequestDto {
    private String category;
    private String tags;
    private String fromDate;
    private String toDate;
}
