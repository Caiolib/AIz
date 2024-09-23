package com.example.projeto.service;

import com.example.projeto.usuario.UsuarioDTO;
import com.example.projeto.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.example.projeto.projeto.ProjetoService;
import com.example.projeto.projeto.ProjetoModel;
import com.example.projeto.projeto.ProjetoController;
import com.example.projeto.projeto.ProjetoRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTests {

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Test
    public void testCadastrarProjetoComGerenteExistente() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome("Projeto Teste");
        projeto.setDescricao("Descrição Teste");
        projeto.setStatus("PLANEJAMENTO");
        projeto.setCpfGerente("12345678900");

        // Mock da verificação do gerente
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCpf("12345678900");
        ResponseEntity<UsuarioDTO> responseEntity = new ResponseEntity<>(usuarioDTO, HttpStatus.OK);

        Mockito.when(usuarioService.verificarUsuario("12345678900")).thenReturn(responseEntity);
        Mockito.when(projetoRepository.save(Mockito.any(ProjetoModel.class))).thenReturn(projeto);

        // Execução do método de teste
        ProjetoModel retorno = projetoService.cadastrarProjeto(projeto);

        // Verificação dos resultados
        Assertions.assertNotNull(retorno);
        Assertions.assertEquals("Projeto Teste", retorno.getNome());
        Assertions.assertEquals("Descrição Teste", retorno.getDescricao());
        Assertions.assertEquals("PLANEJAMENTO", retorno.getStatus());
        Assertions.assertEquals("12345678900", retorno.getCpfGerente());
    }

    @Test
    public void testCadastrarProjetoComGerenteNaoExistente() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome("Projeto Teste");
        projeto.setDescricao("Descrição Teste");
        projeto.setStatus("PLANEJAMENTO");
        projeto.setCpfGerente("12345678900");

        // Mock da verificação do gerente
        ResponseEntity<UsuarioDTO> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(usuarioService.verificarUsuario("12345678900")).thenReturn(responseEntity);

        // Execução do método de teste
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.cadastrarProjeto(projeto);
        });

        // Verificação dos resultados
        Assertions.assertEquals("Gerente não encontrado", exception.getMessage());
    }

    @Test
    public void testListarProjetosComStatus() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome("Projeto Teste");
        projeto.setDescricao("Descrição Teste");
        projeto.setStatus("PLANEJAMENTO");

        List<ProjetoModel> projetos = new ArrayList<>();
        projetos.add(projeto);

        Mockito.when(projetoRepository.findByStatus("PLANEJAMENTO")).thenReturn(projetos);

        // Execução do método de teste
        List<ProjetoModel> resultado = projetoService.listarProjetos("PLANEJAMENTO");

        // Verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Projeto Teste", resultado.get(0).getNome());
        Assertions.assertEquals("Descrição Teste", resultado.get(0).getDescricao());
        Assertions.assertEquals("PLANEJAMENTO", resultado.get(0).getStatus());
    }

    @Test
    public void testListarTodosProjetos() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome("Projeto Teste");
        projeto.setDescricao("Descrição Teste");
        projeto.setStatus("PLANEJAMENTO");

        List<ProjetoModel> projetos = new ArrayList<>();
        projetos.add(projeto);

        Mockito.when(projetoRepository.findAll()).thenReturn(projetos);

        // Execução do método de teste
        List<ProjetoModel> resultado = projetoService.listarProjetos(null);

        // Verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Projeto Teste", resultado.get(0).getNome());
        Assertions.assertEquals("Descrição Teste", resultado.get(0).getDescricao());
        Assertions.assertEquals("PLANEJAMENTO", resultado.get(0).getStatus());
    }

    @Test
    public void testAdicionarPessoaAoProjetoComSucesso() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setId("1");
        projeto.setStatus("PLANEJAMENTO");
        projeto.setMembros(new ArrayList<>());

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCpf("98765432100");
        ResponseEntity<UsuarioDTO> responseEntity = new ResponseEntity<>(usuarioDTO, HttpStatus.OK);

        Mockito.when(projetoRepository.findById("1")).thenReturn(Optional.of(projeto));
        Mockito.when(usuarioService.verificarUsuario("98765432100")).thenReturn(responseEntity);
        Mockito.when(projetoRepository.save(Mockito.any(ProjetoModel.class))).thenReturn(projeto);

        // Execução do método de teste
        ProjetoModel resultado = projetoService.adicionarPessoaAoProjeto("1", "98765432100");

        // Verificação dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.getMembros().contains("98765432100"));
    }

    @Test
    public void testAdicionarPessoaAoProjetoFinalizado() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setId("1");
        projeto.setStatus("FINALIZADO");

        Mockito.when(projetoRepository.findById("1")).thenReturn(Optional.of(projeto));

        // Execução do método de teste
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto("1", "98765432100");
        });

        // Verificação dos resultados
        Assertions.assertEquals("Não é possível adicionar pessoas a um projeto finalizado", exception.getMessage());
    }

    @Test
    public void testAdicionarPessoaAoProjetoNaoExistente() {
        // Mock de projeto inexistente
        Mockito.when(projetoRepository.findById("1")).thenReturn(Optional.empty());

        // Execução do método de teste
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto("1", "98765432100");
        });

        // Verificação dos resultados
        Assertions.assertEquals("Projeto não encontrado", exception.getMessage());
    }

    @Test
    public void testAdicionarPessoaAoProjetoPessoaNaoEncontrada() {
        // Criação do objeto projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setId("1");
        projeto.setStatus("PLANEJAMENTO");

        ResponseEntity<UsuarioDTO> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(projetoRepository.findById("1")).thenReturn(Optional.of(projeto));
        Mockito.when(usuarioService.verificarUsuario("98765432100")).thenReturn(responseEntity);

        // Execução do método de teste
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            projetoService.adicionarPessoaAoProjeto("1", "98765432100");
        });

        // Verificação dos resultados
        Assertions.assertEquals("Pessoa não encontrada", exception.getMessage());
    }
}
