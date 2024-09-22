package com.example.jogadores.jogador.service;


import com.example.jogadores.jogador.model.Jogador;
import com.example.jogadores.jogador.repository.JogadorRepository;
import com.example.jogadores.time.RetornarTImeDTO;
import com.example.jogadores.time.TimeNaoEncontrandoException;
import com.example.jogadores.time.TimeService;
import jakarta.websocket.MessageHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class JogadorServiceTests {


    @InjectMocks
    private JogadorService jogadorService;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private TimeService timeService;



    @Test
    public void TestCadastrarJogador(){

        Jogador jogador = new Jogador();
        jogador.setNome("Matheus Pereira");
        jogador.setIdade(26);

        ArrayList<String> lista = new ArrayList<>();
        lista.add("CRU");
        jogador.setTimes(lista);

        Mockito.when(jogadorRepository.save(Mockito.any(Jogador.class))).thenReturn(jogador);

        Jogador retorno = jogadorService.salvar(jogador);

            // Verify the results
        Assertions.assertNotNull(retorno);
        Assertions.assertEquals(26, retorno.getIdade());
        Assertions.assertEquals("Matheus Pereira", retorno.getNome());
        Assertions.assertEquals(lista, retorno.getTimes());

    }
    @Test
    public void testListarJogadores() {

        // preparo
        Jogador jogador = new Jogador();
        jogador.setNome("Matheus Pereira");
        jogador.setIdade(26);

        ArrayList<String> lista = new ArrayList<>();
        lista.add("CRU");
        jogador.setTimes(lista);

        List<Jogador> partidas = new ArrayList<>();
        partidas.add(jogador);

        Mockito.when(jogadorRepository.findAll()).thenReturn(partidas);

        // chamada do codigo testado
        List<Jogador> resultado = jogadorService.listar();

        // verificacao dos resultados
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Matheus Pereira", resultado.getFirst().getNome());
        Assertions.assertEquals(26, resultado.getFirst().getIdade());
        Assertions.assertEquals(lista, resultado.getFirst().getTimes());
    }

    @Test
    public void testSalvarJogadorNotSucessful() {
        Jogador aposta = new Jogador();
        aposta.setId("1");

        RetornarTImeDTO partidaDTO = new RetornarTImeDTO();
        ResponseEntity<RetornarTImeDTO> responseEntity = new ResponseEntity<>(partidaDTO, HttpStatus.NOT_FOUND);


        Assertions.assertThrows(RuntimeException.class, () -> {
            jogadorService.addTime(aposta, 1);
        });
    }
    @Test
    public void testSalvarJogadorWhenStatusCodeIsNotSuccessful() {
        Jogador jogador = new Jogador();
        jogador.setNome("Matheus Pereira");
        jogador.setIdade(26);
        jogador.setId("1");

        ArrayList<String> lista = new ArrayList<>();
        lista.add("CRU");
        jogador.setTimes(lista);

        RetornarTImeDTO retornarPartidaDTO = new RetornarTImeDTO();
        ResponseEntity<RetornarTImeDTO> partidaDto = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.NOT_FOUND);

        Mockito.when(timeService.getTime(1)).thenReturn(partidaDto);
        Mockito.when(jogadorRepository.findById("1")).thenReturn(Optional.of(jogador));

        Assertions.assertThrows(TimeNaoEncontrandoException.class, () -> {
            jogadorService.addTime(jogador,1);

        });
    }





}
