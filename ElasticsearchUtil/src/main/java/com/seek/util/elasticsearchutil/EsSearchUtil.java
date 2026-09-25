package com.seek.util.elasticsearchutil;


import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.seek.util.configobject.UtilObject.DTO.EsSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.ArrayList;
import java.util.List;

public class EsSearchUtil {

    private final ElasticsearchOperations elasticsearchOperations;
    @Autowired
    public EsSearchUtil(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    //快捷搜索并且处理结果
    public <T> EsSearchResult<T> search(NativeQuery nativeQuery, Class<T> entityClass, String indexName) {
        List<SearchHit<T>> searchResult=elasticsearchOperations.search(nativeQuery, entityClass, IndexCoordinates.of(indexName)).getSearchHits();
        if (searchResult.isEmpty()) return null;
        List<T> returnResult=new ArrayList<>();
        for (SearchHit<T> searchHit : searchResult) returnResult.add(searchHit.getContent());
        return new EsSearchResult<>(returnResult,searchResult.getLast().getSortValues());
    }


    //快速汇总查询条件
    private NativeQuery buildNativeQuery(Query searchQuery, List<SortOptions> sortList, List<Object> lastSearch, int need){
        //构建请求模板,并且绑定查询函数与排序函数
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                //包裹bool主查询与重算分,并且总分求和
                .withQuery(searchQuery)
                //绑定排序
                .withSort(sortList)
                //分页
                .withPageable(PageRequest.of(0,need));
        //判断是否需要进行SearchAfter
        if (lastSearch != null&&!lastSearch.isEmpty()) nativeQueryBuilder.withSearchAfter(lastSearch);
        return nativeQueryBuilder.build();
    }




}
