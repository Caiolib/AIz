package com.example.projeto.projeto;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {
    List<ProjetoModel> findByStatus(String status);
}
