package com.fal.berimbauvideos.controller;

import com.fal.berimbauvideos.model.Usuario;
import com.fal.berimbauvideos.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    UsuarioService usuarioService;

    public static Usuario usuarioAtual;


    @GetMapping("/login")
    public String login(ModelMap model) {
        model.addAttribute("titulo", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String addUserSession(@RequestParam String nomeUsuario, @RequestParam String senha, HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorNomeUsuario(nomeUsuario);
        usuarioAtual = usuario;
        if(usuario == null || !usuario.getSenha().matches(DigestUtils.md5DigestAsHex(senha.getBytes()))){
            return "redirect:/user/login?error";
        }
        if(nomeUsuario.equals("admin") && !usuario.getSenha().matches(DigestUtils.md5DigestAsHex(senha.getBytes()))){
            return "redirect:/user/login?error";
        }

        List<Usuario> users = (List<Usuario>) request.getSession().getAttribute("NOTES_SESSION");
        if (users == null) {
            users = new ArrayList<>();
            request.getSession().setAttribute("user", users);
        }
        users.add(usuario);
        request.getSession().setAttribute("user", users);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String pageEditarDados(ModelMap model, HttpSession session){
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) session.getAttribute("user");
        model.addAttribute("titulo", "Editar Cadastro");
        model.addAttribute("usuario", usuarioService.buscarPorUsuarioId(usuarios.get(0).getUsuarioId()));
        return "editarcadastro";
    }


    @PostMapping("/edit")
    public String editarDados(@RequestParam String nomeUsuario, @RequestParam String nome, @RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.buscarPorNomeUsuario(nomeUsuario);
        if (usuario != null && !usuario.getNomeUsuario().equals(usuarioAtual.getNomeUsuario())){
            return "redirect:/user/edit?userError";
        }
        if(senha.equalsIgnoreCase("")){
            usuarioService.editarUsuario(nomeUsuario, nome, email, usuarioAtual);
        }else {
            usuarioService.editarUsuarioComSenha(nomeUsuario, nome, email, senha, usuarioAtual);
        }
        return "redirect:/user/edit?success";
    }


    @GetMapping("/favoritos")
    public String favoritos(ModelMap model, HttpSession session){
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) session.getAttribute("user");
        model.addAttribute("titulo", "Favoritos");
        model.addAttribute("favoritos", usuarioService.buscarFavoritos(usuarios.get(0).getUsuarioId()));
        return "favoritos";
    }
}
