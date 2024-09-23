package com.example.projeto.projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    public ProjetoModel cadastrarProjeto(@RequestBody ProjetoModel projeto) {
        return projetoService.cadastrarProjeto(projeto);
    }

    @GetMapping
    public List<ProjetoModel> listarProjetos(@RequestParam(required = false) String status) {
        return projetoService.listarProjetos(status);
    }

    @PostMapping("/{id}/adicionarPessoa")
    public ProjetoModel adicionarPessoaAoProjeto(@PathVariable String id, @RequestParam String cpfPessoa) {
        return projetoService.adicionarPessoaAoProjeto(id, cpfPessoa);
    }
}
