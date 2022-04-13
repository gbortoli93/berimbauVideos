package com.fal.berimbauvideos.service;

import com.fal.berimbauvideos.model.ListaFavoritos;
import com.fal.berimbauvideos.model.Usuario;
import com.fal.berimbauvideos.model.Video;
import com.fal.berimbauvideos.repository.ListaFavoritosRepository;
import com.fal.berimbauvideos.repository.UsuarioRepository;
import com.fal.berimbauvideos.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class UsuarioService {

    @Autowired
    ListaFavoritosRepository listaFavoritosRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    VideoRepository videoRepository;

    public void salvarUsuario(String nomeUsuario, String nome, String email, String senha) {

        String password = DigestUtils.md5DigestAsHex(senha.getBytes());
        Usuario usuario = new Usuario(nomeUsuario, nome, email, password);
        usuarioRepository.save(usuario);
    }

    public void editarUsuario(String nomeUsuario, String nome, String email, Usuario usuario) {
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuarioRepository.save(usuario);
    }

    public void editarUsuarioComSenha(String nomeUsuario, String nome, String email, String senha, Usuario usuario) {
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(DigestUtils.md5DigestAsHex(senha.getBytes()));
        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorUsuarioId(int usuarioId) {
        return usuarioRepository.findByUsuarioId(usuarioId);

    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuario(nomeUsuario);
    }


    public void adicionarVideoAosFavoritos(int videoId, int usuarioId) {
        ListaFavoritos listaFavoritos = new ListaFavoritos(usuarioId, videoId);
        listaFavoritosRepository.save(listaFavoritos);
    }

    public List<Video> buscarFavoritos(int usuarioId) {
        List<Integer> listaVideoIds = listaFavoritosRepository.findFavoritosByUsuarioId(usuarioId);
        List<Video> listaVideos = new ArrayList<>();
        for (int videoId : listaVideoIds) {
            listaVideos.add(videoRepository.findByVideoId(videoId));
        }
        return listaVideos;
    }

    public void removerUsuario(int usuarioId) {
        Usuario usuario = buscarPorUsuarioId(usuarioId);
        if (!listaFavoritosRepository.findAllByUsuarioId(usuarioId).isEmpty()) {
            listaFavoritosRepository.deleteAll(listaFavoritosRepository.findAllByUsuarioId(usuarioId));
        }
        usuarioRepository.delete(usuario);
    }

    public List<Usuario> todosUsuarios() {
        return usuarioRepository.findAll();
    }
}
