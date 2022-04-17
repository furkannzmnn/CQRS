package com.example.cqrs.api;

import com.example.cqrs.cqrs.command.CommandResponse;
import com.example.cqrs.cqrs.command.CreatePageCommand;
import com.example.cqrs.cqrs.command.CreatePageCommandHandler;
import com.example.cqrs.cqrs.query.PageQuery;
import com.example.cqrs.cqrs.query.PageQueryHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public record PublicationController(CreatePageCommandHandler createPageCommandHandler,
                                    PageQueryHandler pageQueryHandler) {

    @PostMapping("/publication/create")
    public CommandResponse<?> createPage(@RequestBody CreatePageCommand command) throws ExecutionException, InterruptedException {
        CommandResponse<?> response = createPageCommandHandler.handle(command);

        return response;
    }

    @GetMapping("/publication/pages")
    public ResponseEntity<?> getPages() throws IOException {
        return ResponseEntity.ok(pageQueryHandler.getPublications());
    }

}
