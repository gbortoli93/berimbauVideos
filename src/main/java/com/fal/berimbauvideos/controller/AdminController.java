package com.fal.berimbauvideos.controller;

import com.fal.berimbauvideos.model.Video;
import com.fal.berimbauvideos.service.UsuarioService;
import com.fal.berimbauvideos.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    VideoService videoService;


    @GetMapping("/adicionarvideo")
    public String pageAdicionarVideo(ModelMap model) {
        model.addAttribute("titulo", "Adicionar Vídeo");
        return "adicionarvideo";
    }

    @PostMapping("/adicionarvideo")
    public String adicionarVideo(@RequestParam String nome, @RequestParam String descricao, @RequestParam MultipartFile video, @RequestParam MultipartFile miniatura, ModelMap model) {
        Video videoAdicionado = videoService.salvarVideo(nome, descricao, video, miniatura);
        model.addAttribute("videoId", videoAdicionado.getVideoId());
        return "redirect:/admin/adicionarvideo?success";
    }

    @GetMapping("/listusers")
    public String todosUsuarios(ModelMap model) {
        model.addAttribute("usuarios", usuarioService.todosUsuarios());
        model.addAttribute("titulo", "Buscar Usuários");
        return "listausuarios";
    }

    @GetMapping("/{usuarioId}/edituser")
    public String pageEditarCadastro(@PathVariable int usuarioId, ModelMap modelMap) {
        modelMap.addAttribute("titulo", "Editar Cadastro");
        modelMap.addAttribute("usuario", usuarioService.buscarPorUsuarioId(usuarioId));
        return "editarcadastro";
    }

    @GetMapping("/listavideos")
    public String listarVideos(ModelMap model) {
        model.addAttribute("titulo", "Lista de Vídeos");
        model.addAttribute("videos", videoService.todosVideos());
        return "listavideos";
    }

    @GetMapping("/listausuarios")
    public String listarUsuarios(ModelMap model) {
        model.addAttribute("titulo", "Lista de Usuários");
        model.addAttribute("usuarios", usuarioService.todosUsuarios());
        return "listausuarios";
    }

    @GetMapping("/{videoId}/editvideo")
    public String pageEditarInformacoesVideo(@PathVariable int videoId, ModelMap model) {
        model.addAttribute("titulo", "Editar Vídeo - " + videoId);
        model.addAttribute("video", videoService.buscarPorId(videoId));
        return "editarvideo";
    }

    @PostMapping("/{videoId}/editvideo")
    public String editarInformacoesVideo(@RequestParam int videoId, @RequestParam String nome, @RequestParam String descricao) {
        videoService.editarVideo(videoId, nome, descricao);
        return "redirect:/admin/" + videoId + "/editvideo?success";
    }

    @PostMapping("/deletevideo")
    public String deletarVideo(@RequestParam int videoId) {
        videoService.removerVideo(videoId);
        return "redirect:/admin/listavideos?deleted";
    }

    @PostMapping("/comentario/delete/{comentarioId}")
    public String deletarComentario(@PathVariable int comentarioId, @RequestParam int videoId) {

        videoService.deletarComentario(comentarioId, videoId);
        return "redirect:/video/watch/" + videoId;
    }

    @PostMapping("/deleteusuario")
    public String deletarUsuario(@RequestParam int usuarioId) {
        if (usuarioId == UsuarioController.usuarioAtual.getUsuarioId()) {
            return "redirect:/admin/listausuarios?deleteError";
        }
        usuarioService.removerUsuario(usuarioId);
        return "redirect:/admin/listausuarios?deleted";
    }


}
