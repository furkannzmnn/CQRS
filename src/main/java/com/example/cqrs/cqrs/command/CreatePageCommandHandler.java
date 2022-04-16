package com.example.cqrs.cqrs.command;

import com.example.cqrs.model.Publication;
import com.example.cqrs.repository.PublicationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public record CreatePageCommandHandler(PublicationRepository publicationRepository,
                                       ObjectMapper mapper,
                                       KafkaTemplate<String, String> kafkaTemplate) {

    public CommandResponse<?> handle(CreatePageCommand command) throws JsonProcessingException {

        var publication = new Publication(
                command.getId(), command.getTitle(), command.getAuthor()
        );

        Publication save = publicationRepository.save(publication);

        kafkaTemplate.send("cqrs-events", mapper.writeValueAsString(save));

        return new CommandResponse<>(null, true);

    }

}
