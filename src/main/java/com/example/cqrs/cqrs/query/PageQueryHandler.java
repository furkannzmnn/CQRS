package com.example.cqrs.cqrs.query;

import com.example.cqrs.elastic.ElasticSearchService;
import com.example.cqrs.model.Publication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class PageQueryHandler {

    private static final String INDEX = "publications";

    private final ElasticSearchService elasticSearchService;

    public PageQueryHandler(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    public List<?> getPublications(PageQuery query) throws IOException {
        return elasticSearchService.search(INDEX, Publication.class);
    }

}
