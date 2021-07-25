package com.example.demo.controller;

import com.example.demo.domain.Image;
import com.example.demo.service.ImageRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lpetrenas
 */
@CrossOrigin("*")
@RestController
public class ImageRecognitionController {

    @Autowired
    private ImageRecognitionService service;

    /**
     * @author lpetrenas
     */
    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> uploadImage(@RequestParam("image") MultipartFile multipartFile) throws Exception {
        return service.processImage(multipartFile);
    }

    /**
     * @author lpetrenas
     */
    @GetMapping("/api/findLatestFiveResults")
    public ResponseEntity<List<Image>> getLatestFiveResults() {
        return new ResponseEntity<>(service.findTop5ByOrderByIdDesc(), HttpStatus.OK);
    }

}