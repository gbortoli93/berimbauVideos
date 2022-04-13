package com.fal.berimbauvideos.repository;

import com.fal.berimbauvideos.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository <Video, Integer> {

   public Video findByVideoId(int id);

   @Query(nativeQuery = true, value =  "SELECT * FROM video " +
                                       "WHERE (nome LIKE %?1%) " +
                                       "OR " +
                                       "(descricao LIKE %?1%)")
   public List<Video> findByInput(String input);

   @Query(nativeQuery = true, value =  "SELECT * FROM video " +
           "ORDER BY avaliacao DESC " +
           "LIMIT 6")
   public List<Video> findByInputOrderByAvaliacao();

}
