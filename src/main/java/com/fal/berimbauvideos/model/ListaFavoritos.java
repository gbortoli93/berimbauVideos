package com.fal.berimbauvideos.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "lista_favoritos")
@NoArgsConstructor
public class ListaFavoritos {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int favoritosId;

    private int usuarioId;

    private int videoId;

    public ListaFavoritos(int usuarioId, int videoId){
        this.usuarioId = usuarioId;
        this.videoId = videoId;
    }

}
