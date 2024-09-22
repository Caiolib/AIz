package com.example.jogadores.jogador.repository;


import com.example.jogadores.jogador.model.Jogador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JogadorRepository extends MongoRepository<Jogador, String> {
}
