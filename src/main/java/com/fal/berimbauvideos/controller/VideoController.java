package com.fal.berimbauvideos.controller;

import com.fal.berimbauvideos.model.Comentario;
import com.fal.berimbauvideos.model.Usuario;
import com.fal.berimbauvideos.model.Video;
import com.fal.berimbauvideos.repository.ListaFavoritosRepository;
import com.fal.berimbauvideos.service.VideoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    ListaFavoritosRepository listaFavoritosRepository;

    @GetMapping("/search")
    public String buscarVideo(@RequestParam String input, ModelMap model) {
        model.addAttribute("videos", videoService.buscarPorInput(input));
        model.addAttribute("titulo", "Busca: " + input);
        return "buscarvideo";
    }

    @GetMapping("/watch/arquivovideo/{videoId}")
    public void getArquivoVideo(@PathVariable int videoId,
                               HttpServletResponse response) {
        response.setContentType("video/mp4");
        Video video = videoService.buscarPorId(videoId);
        InputStream is = new ByteArrayInputStream(video.getArquivo());
        try {
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/arquivominiatura/{videoId}")
    public void getArquivoMiniatura(@PathVariable int videoId,
                                HttpServletResponse response) {
        response.setContentType("image/png");
        Video video = videoService.buscarPorId(videoId);
        InputStream is = new ByteArrayInputStream(video.getMiniatura());
        try {
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/watch/{videoId}")
    public String assistirVideo(@PathVariable int videoId, ModelMap model) {
        Video video = videoService.buscarPorId(videoId);
        if (UsuarioController.usuarioAtual != null) {
            List<Video> videoList = UsuarioController.usuarioAtual.getFavoritos();
            List<Video> favoritos = new ArrayList<>();
            for (Video v : videoList) {
                if (v.getVideoId() == video.getVideoId()) {
                    model.addAttribute("falca", true);
                    favoritos.add(video);
                }
            }
        }
        model.addAttribute("titulo", video.getNome());
        model.addAttribute("video", video);
        return "exibirvideo";
    }

    @PostMapping("/{videoId}/adicionarfavoritos")
    public String adicionarVideoFavoritos(@PathVariable int videoId, HttpSession session) {
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) session.getAttribute("user");
        UsuarioController.usuarioAtual.addFavoritos(videoService.adicionarVideoFavoritos(videoId, usuarios.get(0).getUsuarioId()));
        return "redirect:/video/watch/{videoId}";
    }

    @PostMapping("/{videoId}/comentarios/incluir")
    public String comentarVideo(@RequestParam String comentario, @PathVariable int videoId, ModelMap model) {
        List<Comentario> listComent = videoService.incluirComentario(comentario, UsuarioController.usuarioAtual.getUsuarioId(), videoId);
        model.addAttribute("comentario", listComent);
        return "redirect:/video/watch/{videoId}";
    }

    @PostMapping("/{videoId}/avaliarvideo")
    public String avaliarVideo(@RequestParam int avaliacao, @PathVariable int videoId) {
        videoService.avaliarVideo(videoId, avaliacao);
        return "redirect:/video/watch/{videoId}";
    }
}
