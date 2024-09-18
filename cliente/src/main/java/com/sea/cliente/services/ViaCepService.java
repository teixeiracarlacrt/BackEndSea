package com.sea.cliente.services;

import com.sea.cliente.model.ViaCepResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    @Value("${viacep.url}")
    private String viaCepUrl;

    private final RestTemplate restTemplate;

    public ViaCepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ViaCepResponse buscarEnderecoPorCep(String cep) {
        String url = String.format("%s/%s/json/", viaCepUrl, cep);
        return restTemplate.getForObject(url, ViaCepResponse.class);
    }
}
