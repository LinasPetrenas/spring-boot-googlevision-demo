package com.example.demo.repository;

import com.example.demo.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lpetrenas
 */
@Repository
public interface ImageRecognitionRepository extends JpaRepository<Image, Long> {

    /**
     * Finds latest five ordered by id in descending order {@link Image} objects.
     *
     * @return {@link List<Image>}
     * @author lpetrenas
     */
    List<Image> findTop5ByOrderByIdDesc();
}
