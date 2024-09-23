package com.example.projeto.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {

    public ResponseEntity<UsuarioDTO> verificarUsuario(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://184.72.80.215:8080/usuario/" + cpf;
        return restTemplate.getForEntity(url, UsuarioDTO.class);
    }
}
