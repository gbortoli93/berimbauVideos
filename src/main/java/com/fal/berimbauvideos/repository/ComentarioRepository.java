package com.fal.berimbauvideos.repository;

import com.fal.berimbauvideos.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    public Comentario findByComentarioId(int comentarioId);

    @Query(nativeQuery = true, value =  "SELECT * FROM comentario " +
            "WHERE video_id = ?1")
    public List<Comentario> findComentariosByVideoId(int videoId);

}
