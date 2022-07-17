package com.example.cqrs.elastic;

import com.example.cqrs.model.Publication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
public record ElasticListener(ElasticSearchService elasticsearchService,
                              ObjectMapper objectMapper, @Qualifier("taskExecutor") ScheduledExecutorService exetutorService) {

    private static byte RETRY_COUNT = 0;

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ElasticListener.class);

    @KafkaListener(topics = "cqrs-events", groupId = "group_id")
    public void listen(String event) {
        try {
            Publication publication = objectMapper.readValue(event, Publication.class);
            elasticsearchService.createIndex("publications", publication.getId(), publication);
            RETRY_COUNT++;
        } catch (Exception e) {
           if (RETRY_COUNT <= 3) {
               exetutorService.scheduleAtFixedRate(() -> listen(event), RETRY_COUNT, RETRY_COUNT, java.util.concurrent.TimeUnit.SECONDS);
           } else {
               LOGGER.info("Failed to save publication");
               e.printStackTrace();
               exetutorService.shutdown();
           }
        }
    }
}
