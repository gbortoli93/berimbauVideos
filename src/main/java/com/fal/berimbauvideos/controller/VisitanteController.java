package com.fal.berimbauvideos.controller;

import com.fal.berimbauvideos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/visitor")
public class VisitanteController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/signup")
    public String cadastro(ModelMap model) {
        model.addAttribute("titulo", "Cadastro");
        return "cadastro";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String nomeUsuario, @RequestParam String nome, @RequestParam String email, @RequestParam String senha) {

        if (usuarioService.buscarPorNomeUsuario(nomeUsuario) != null){
            return "redirect:/visitor/signup?userError";
        }
        usuarioService.salvarUsuario(nomeUsuario, nome, email, senha);
        return "redirect:/user/login?cadastro";
    }

}
