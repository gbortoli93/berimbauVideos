package com.fal.berimbauvideos.repository;

import com.fal.berimbauvideos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByUsuarioId(int id);

    public Usuario findByNomeUsuario(String nomeUsuario);

}
