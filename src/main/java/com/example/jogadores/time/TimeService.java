package com.example.jogadores.time;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TimeService {

    public ResponseEntity<RetornarTImeDTO> getTime(Integer idTime) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(
                "http://54.237.220.2:8082/time/" + idTime,
                RetornarTImeDTO.class);
    }
}
