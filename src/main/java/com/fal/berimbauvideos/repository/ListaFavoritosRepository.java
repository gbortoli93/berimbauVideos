package com.fal.berimbauvideos.repository;

import com.fal.berimbauvideos.model.ListaFavoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaFavoritosRepository extends JpaRepository<ListaFavoritos, Integer> {


    @Query(nativeQuery = true, value =  "SELECT video_id FROM lista_favoritos " +
            "WHERE usuario_id = ?1")
    public List<Integer> findFavoritosByUsuarioId(int usuarioId);

    public List<ListaFavoritos> findAllByUsuarioId(int usuarioId);

    public List<ListaFavoritos> findAllByVideoId(int videoId);

}
