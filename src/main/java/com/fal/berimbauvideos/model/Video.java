package com.fal.berimbauvideos.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int videoId;
    private String nome;
    private String descricao;
    private double avaliacao;
    private int numeroAvaliacoes;

    @Lob
    private byte[] miniatura;
    @Lob
    private byte[] arquivo;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name="video_comentario", joinColumns=
            {@JoinColumn(name="comentarioId")}, inverseJoinColumns=
            {@JoinColumn(name="videoId")})
    private List<Comentario> comentarios = new ArrayList<>();

    public void setComentario(Comentario comentario){
        comentarios.add(comentario);
    }

}
