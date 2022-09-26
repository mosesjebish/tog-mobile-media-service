package org.tog.togmobilemediaservice.dto;

import lombok.Data;

@Data
public class Thumbnails {
    public Thumbnail defaultSize;
    public Thumbnail medium;
    public Thumbnail high;
    public Thumbnail standard;
    public Thumbnail maxRes;
}
