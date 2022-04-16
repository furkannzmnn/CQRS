package com.example.cqrs.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ElasticSearchService {

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper mapper;

    public ElasticSearchService(ElasticClient restHighLevelClient, ObjectMapper mapper) {
        this.restHighLevelClient = restHighLevelClient.getElasticSearchClient();
        this.mapper = mapper;
    }

    public <T> void createIndex(String indexName, Long documentUniqueId, T source) throws IOException {
        String json = mapper.writeValueAsString(source);

        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(String.valueOf(documentUniqueId));
        indexRequest.source(json, XContentType.JSON);

        this.restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    public <S> List<QueryResponse<S>> search(String indexName, Class<S> clazz) throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.sort("id", SortOrder.DESC);
        sourceBuilder.from(0);
        sourceBuilder.size(2000);

        SearchRequest request = new SearchRequest(indexName);
        request.source(sourceBuilder);

        SearchResponse response = this.restHighLevelClient.search(request, RequestOptions.DEFAULT);

        return Stream.of(response.getHits().getHits())
                .map(hit -> read(hit, clazz))
                .map(QueryResponse::new)
                .collect(java.util.stream.Collectors.toList());
    }


    private <S> S read(SearchHit hit, Class<S> clazz) {
        try {
            return mapper.readValue(hit.getSourceAsString(), clazz);
        }catch (JsonProcessingException e) {
           return null;
        }
    }


    private record QueryResponse<S>(S hits) {
    }
}
