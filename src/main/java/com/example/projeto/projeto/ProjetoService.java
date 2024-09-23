package com.example.projeto.projeto;

import com.example.projeto.usuario.UsuarioService;
import com.example.projeto.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public ProjetoModel cadastrarProjeto(ProjetoModel projeto) {
        // Verifica se o gerente existe
        ResponseEntity<UsuarioDTO> gerenteResponse = usuarioService.verificarUsuario(projeto.getCpfGerente());
        if (gerenteResponse.getStatusCode().is2xxSuccessful()) {
            return projetoRepository.save(projeto);
        } else {
            throw new RuntimeException("Gerente não encontrado");
        }
    }

    public List<ProjetoModel> listarProjetos(String status) {
        if (status != null) {
            return projetoRepository.findByStatus(status);
        } else {
            return projetoRepository.findAll();
        }
    }

    public ProjetoModel adicionarPessoaAoProjeto(String idProjeto, String cpfPessoa) {
        Optional<ProjetoModel> projetoOpt = projetoRepository.findById(idProjeto);
        if (projetoOpt.isEmpty()) {
            throw new RuntimeException("Projeto não encontrado");
        }

        ProjetoModel projeto = projetoOpt.get();
        if ("FINALIZADO".equals(projeto.getStatus())) {
            throw new RuntimeException("Não é possível adicionar pessoas a um projeto finalizado");
        }

        // Verifica se a pessoa existe
        ResponseEntity<UsuarioDTO> pessoaResponse = usuarioService.verificarUsuario(cpfPessoa);
        if (pessoaResponse.getStatusCode().is2xxSuccessful()) {
            projeto.getMembros().add(cpfPessoa);
            return projetoRepository.save(projeto);
        } else {
            throw new RuntimeException("Pessoa não encontrada");
        }
    }
}
