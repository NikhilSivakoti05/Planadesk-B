package com.planadesk.backend.service;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ImageKitService {

    @Value("${imagekit.private.key}")
    private String privateKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://upload.imagekit.io/api/v1")
            .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
            .build();

    public String upload(MultipartFile file) {
        try {
            String auth = Base64.getEncoder()
                    .encodeToString((privateKey + ":").getBytes());

            Resource resource = file.getResource(); // repeatable

            return webClient.post()
                    .uri("/files/upload")
                    .header("Authorization", "Basic " + auth)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(
                            new LinkedMultiValueMap<>() {{
                                add("file", resource);
                                add("fileName", file.getOriginalFilename());
                                add("folder", "/products");
                            }}
                    ))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(json -> json.get("url").asText())
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("ImageKit upload failed", e);
        }
    }
}
