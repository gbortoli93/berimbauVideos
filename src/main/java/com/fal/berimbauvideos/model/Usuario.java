package com.fal.berimbauvideos.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int usuarioId;

    @Column(unique = true)
    private String nomeUsuario;
    private String nome;
    private String email;
    private String senha;
    private String role;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="lista_favoritos", joinColumns=
            {@JoinColumn(name="usuarioId")}, inverseJoinColumns=
            {@JoinColumn(name="videoId")})
    private List<Video> favoritos = new ArrayList<>();

    public Usuario(String nomeUsuario, String nome, String email, String senha){
        this.nomeUsuario = nomeUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = "USER";
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void addFavoritos(Video video){
        this.favoritos.add(video);
    }

}



