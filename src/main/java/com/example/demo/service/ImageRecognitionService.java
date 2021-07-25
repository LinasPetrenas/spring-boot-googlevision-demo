package com.example.demo.service;

import com.example.demo.domain.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lpetrenas
 */
@Service
public interface ImageRecognitionService {

    /**
     * Image processing and object recognition using google vision api
     *
     * @return processed image wrapped in {@link ResponseEntity}
     * @author lpetrenas
     */
    ResponseEntity<Image> processImage(MultipartFile multipartFile) throws Exception;

    /**
     * Derived Query Method that searches five latest persisted rows in descending id order.
     *
     * @return {@link List<Image>} containing of <= five row results
     * @author lpetrenas
     */
    List<Image> findTop5ByOrderByIdDesc();

}

