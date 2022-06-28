package com.example.cqrs.api;

import com.example.cqrs.cqrs.command.CommandResponse;
import com.example.cqrs.cqrs.command.CreatePageCommand;
import com.example.cqrs.cqrs.command.CreatePageCommandHandler;
import com.example.cqrs.cqrs.query.PageQueryHandler;
import com.example.cqrs.elastic.ElasticSearchService;
import com.example.cqrs.model.Publication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public record PublicationController(CreatePageCommandHandler createPageCommandHandler,
                                    PageQueryHandler pageQueryHandler) {

    @PostMapping("/publication/create")
    public CommandResponse<Object> createPage(@RequestBody CreatePageCommand command) {
        return createPageCommandHandler.handle(command);
    }

    @GetMapping("/publication/pages")
    public ResponseEntity<List<ElasticSearchService.QueryResponse<Publication>>> getPages() throws IOException {
        return ResponseEntity.ok(pageQueryHandler.getPublications());
    }

}
