package com.example.webhookapp.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.webhookapp.model.GenerateWebhookRequest;
import com.example.webhookapp.model.GenerateWebhookResponse;
import com.example.webhookapp.model.FinalQueryRequest;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final SqlSolverService sqlSolverService;

    public WebhookService(SqlSolverService sqlSolverService) {
        this.sqlSolverService = sqlSolverService;
    }

    public void process() {

        // 1) Generate Webhook API
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        GenerateWebhookRequest req = new GenerateWebhookRequest(
                "Vidushi Agnihotri",             
                "22BLC1387",           
                "vidushi.agnihotri26@gmail.com"      
        );

        GenerateWebhookResponse response =
                restTemplate.postForObject(url, req, GenerateWebhookResponse.class);

        if (response == null) {
            System.out.println("Failed to generate webhook.");
            return;
        }

        String webhookUrl = response.getWebhook();
        String accessToken = response.getAccessToken();

        // 2) Generate SQL Query (based on regNo last digits)
        String finalQuery = sqlSolverService.solve("22BLC1387");

        // 3) Submit Final Query API
        String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        FinalQueryRequest body = new FinalQueryRequest(finalQuery);

        HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> result =
                restTemplate.postForEntity(submitUrl, entity, String.class);

        System.out.println("Response → " + result.getBody());
    }
}

