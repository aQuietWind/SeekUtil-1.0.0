package com.seek.util.elasticsearchutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

public class EsUpdateUtil {



    private final ElasticsearchOperations elasticsearchOperations;
    @Autowired
    public EsUpdateUtil(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    //文档直接覆盖
    public<T> UpdateResponse updateBySave(T entity, String indexName) {
        return elasticsearchOperations.update(entity, IndexCoordinates.of(indexName));
    }



}
