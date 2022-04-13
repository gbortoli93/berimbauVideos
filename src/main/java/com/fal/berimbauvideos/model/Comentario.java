package com.fal.berimbauvideos.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comentarioId;

    private String texto;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "usuarioId", referencedColumnName = "usuarioId")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "videoId", referencedColumnName = "videoId")
    private Video video;

    public Comentario(String texto, Usuario autor, Video video) {
        this.texto = texto;
        this.usuario = autor;
        this.video = video;
    }
}
