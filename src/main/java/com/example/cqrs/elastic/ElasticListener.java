package com.example.cqrs.elastic;

import com.example.cqrs.model.Publication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record ElasticListener(ElasticSearchService elasticsearchService,
                              ObjectMapper objectMapper) {

    @KafkaListener(topics = "cqrs-events", groupId = "group_id")
    public void listen(String event) {
        try {
            Publication publication = objectMapper.readValue(event, Publication.class);
            elasticsearchService.createIndex("publications", publication.getId(), publication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
