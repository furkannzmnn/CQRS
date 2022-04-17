package com.example.cqrs.cqrs.command;

import com.example.cqrs.model.Publication;
import com.example.cqrs.repository.PublicationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public record CreatePageCommandHandler(PublicationRepository publicationRepository,
                                       ObjectMapper mapper,
                                       KafkaTemplate<String, String> kafkaTemplate) {

    public CommandResponse<?> handle(CreatePageCommand command) {

        final AtomicReference<CommandResponse<Object>> response = new AtomicReference<>(CommandResponse.ok(null));

        var publication = new Publication(
                command.getId(), command.getTitle(), command.getAuthor()
        );

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(
                        () -> publicationRepository.save(publication))
                .thenApply(entity -> {
                    try {
                        return kafkaTemplate.send("cqrs-events", mapper.writeValueAsString(entity));
                    } catch (JsonProcessingException e) {
                        response.set(new CommandResponse<>(null, false));
                        return response;
                    }
                })
                .thenAccept(call -> response.set(responseHandler(response)));

              future.join();
              return response.get();
    }


    private CommandResponse<Object> responseHandler(AtomicReference<CommandResponse<Object>> o) {
        if (o.get().getResponse() != null) {
            @SuppressWarnings("unchecked")
            ListenableFuture<SendResult<?,?>> result = (ListenableFuture<SendResult<?,?>>) o.get().getResponse();
            o.set(CommandResponse.ok(result));
            return o.get();
        }
         o.set(CommandResponse.error(null));
         return o.get();
    }

}
