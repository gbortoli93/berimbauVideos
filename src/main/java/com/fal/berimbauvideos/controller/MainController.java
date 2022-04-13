package com.fal.berimbauvideos.controller;


import com.fal.berimbauvideos.model.Video;
import com.fal.berimbauvideos.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private VideoService videoService;

    @GetMapping("/")
    public String index(ModelMap model) {
        List<Video> maisAvaliados = videoService.maisAvaliados();
        model.addAttribute("maisAvaliados", maisAvaliados);
        model.addAttribute("titulo", "Home");
        return "index";
    }
}
