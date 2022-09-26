package org.tog.togmobilemediaservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/youtube")
public class YoutubeController {
    @GetMapping(value = "/")
    public String getUserInfo() {
       return "Hey this is the media service";
    }
}
