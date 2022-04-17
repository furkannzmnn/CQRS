package com.example.cqrs.cqrs.query;

import com.example.cqrs.elastic.ElasticSearchService;
import com.example.cqrs.model.Publication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public record PageQueryHandler(ElasticSearchService elasticSearchService) {

    private static final String INDEX = "publications";

    public List<?> getPublications() throws IOException {
        return elasticSearchService.search(INDEX, Publication.class);
    }
}
