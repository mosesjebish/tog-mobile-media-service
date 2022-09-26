package org.tog.togmobilemediaservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class YouTubeVideoSnippet {
    public String publishedAt;
    public String title;
    public String description;
    public Thumbnails thumbnails;
    public List<String> tags;
}
