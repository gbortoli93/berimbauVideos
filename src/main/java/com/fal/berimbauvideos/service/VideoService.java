package com.fal.berimbauvideos.service;

import com.fal.berimbauvideos.model.Comentario;
import com.fal.berimbauvideos.model.Usuario;
import com.fal.berimbauvideos.model.Video;
import com.fal.berimbauvideos.repository.ComentarioRepository;
import com.fal.berimbauvideos.repository.ListaFavoritosRepository;
import com.fal.berimbauvideos.repository.UsuarioRepository;
import com.fal.berimbauvideos.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    ListaFavoritosRepository listaFavoritosRepository;

    @Value("${java.io.tmpdir}")
    private String tmpdir;

    public List<Video> buscarPorInput(String input) {
        return videoRepository.findByInput(input);
    }

    @Transactional
    public Video salvarVideo(String nome, String descricao, MultipartFile video, MultipartFile miniatura) {

        byte[] arquivoVideo = new byte[0];
        byte[] arquivoMiniatura = new byte[0];
        try {
            arquivoVideo = video.getBytes();
            arquivoMiniatura = miniatura.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Video videoPersistido = new Video();
        videoPersistido.setNome(nome);
        videoPersistido.setDescricao(descricao);
        videoPersistido.setAvaliacao(0.0);
        videoPersistido.setArquivo(arquivoVideo);
        videoPersistido.setMiniatura(arquivoMiniatura);
        videoRepository.save(videoPersistido);
        return videoPersistido;
    }

    public List<Video> maisAvaliados() {
        return videoRepository.findByInputOrderByAvaliacao();
    }

    public Video buscarPorId(int videoId) {
        return videoRepository.findByVideoId(videoId);
    }

    public List<Video> todosVideos() {
        return videoRepository.findAll();
    }

    public void editarVideo(int videoId, String nome, String descricao) {
        Video video = videoRepository.findByVideoId(videoId);
        video.setNome(nome);
        video.setDescricao(descricao);
        videoRepository.save(video);
    }

    public List<Comentario> incluirComentario(String comentario, int usuarioId, int videoId) {

        Usuario usuario = usuarioRepository.findByUsuarioId(usuarioId);
        Video video = videoRepository.findByVideoId(videoId);
        Comentario coment = new Comentario(comentario, usuario, video);
        comentarioRepository.save(coment);
        video.setComentario(coment);
        videoRepository.save(video);
        List<Comentario> comentList = comentarioRepository.findComentariosByVideoId(videoId);
        return comentList;
    }

    public void deletarComentario(int comentarioId, int videoId) {

        Video video = videoRepository.findByVideoId(videoId);
        List<Comentario> comentList = video.getComentarios();
        for (Iterator<Comentario> iterator = comentList.iterator(); iterator.hasNext(); ) {
            Comentario comentario = iterator.next();
            if (comentario.getComentarioId() == comentarioId) {
                iterator.remove();
            }
        }
        videoRepository.save(video);
        comentarioRepository.delete(comentarioRepository.findByComentarioId(comentarioId));
    }


    public Video adicionarVideoFavoritos(int videoId, int usuarioId) {
        usuarioService.adicionarVideoAosFavoritos(videoId, usuarioId);
        Video video = buscarPorId(videoId);
        return video;
    }

    public void removerVideo(int videoId) {
        if(!listaFavoritosRepository.findAllByVideoId(videoId).isEmpty()){
            listaFavoritosRepository.deleteAll(listaFavoritosRepository.findAllByVideoId(videoId));
        }
        Video video = videoRepository.findByVideoId(videoId);
        videoRepository.delete(video);
    }

    public void avaliarVideo(int videoId, int avaliacao) {
        Video video = buscarPorId(videoId);
        if (video.getNumeroAvaliacoes() == 0) {
            video.setNumeroAvaliacoes(video.getNumeroAvaliacoes() + 1);
            video.setAvaliacao(avaliacao);
        } else {
            video.setNumeroAvaliacoes(video.getNumeroAvaliacoes() + 1);
            video.setAvaliacao((video.getAvaliacao() + avaliacao) / 2);
        }
        videoRepository.save(video);
    }

}
