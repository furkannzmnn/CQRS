package com.example.cqrs.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Component
public class ElasticClient {

    private final RestHighLevelClient elasticSearchClient;

    public ElasticClient() {
        this.elasticSearchClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")
                        ));
    }

    public RestHighLevelClient getElasticSearchClient() {
        return elasticSearchClient;
    }


}
