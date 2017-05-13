/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;


import org.elasticsearch.client.Client;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yasin
 */
public class ElasticClient {
    
    private static final Logger logger = LoggerFactory.getLogger(ElasticClient.class);

    public static final String ID = "_id";

    public static final String DEFAULT_TYPE = "default";
    
    protected Client client;

    public ElasticClient(Client c) {
        this.client = c;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean index(String indexName, IndexObject indexObject) {
        return index(indexName, Arrays.asList(indexObject));
    }
    
    public boolean index(String indexName, List<IndexObject> indexObjects) {

        if (indexName == null) {
            throw new NullPointerException("indexName must not be null.");
        }

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (IndexObject indexObject : indexObjects) {
            try {
                bulkRequestBuilder.add(buildIndexRequest(indexName, indexObject));
            } catch (IOException e) {
                throw new ElasticeException(e);
            }
        }

        BulkResponse responses = bulkRequestBuilder.execute().actionGet(10, TimeUnit.SECONDS);
        
        //BulkResponse responses = bulkRequestBuilder.get();
        
        if (responses.hasFailures()) {
            logger.error("index failed:" + responses.buildFailureMessage());
            return false;
        }
        return true;
    }
    
    private IndexRequestBuilder buildIndexRequest(String index, IndexObject indexObject) throws IOException {
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, DEFAULT_TYPE);
        if (!Strings.isEmpty(indexObject.id)) {
            indexRequestBuilder.setId(indexObject.id);
        }
        if (!Strings.isEmpty(indexObject.routing)) {
            indexRequestBuilder.setRouting(indexObject.routing);
        }

        if (indexObject.ttl != -1) {
            indexRequestBuilder.setTTL(indexObject.ttl);
        }

        return indexRequestBuilder.setSource(indexObject.build());
    }
    
    public boolean update(String indexName, IndexObject indexObject) {
        return update(indexName, Arrays.asList(indexObject));
    }
    
     public boolean update(String indexName, List<IndexObject> indexObjects) {
        if (indexName == null) {
            throw new NullPointerException("indexName must not be null.");
        }

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (IndexObject indexObject : indexObjects) {
            try {
                bulkRequestBuilder.add(buildUpdateRequest(indexName, indexObject));
            } catch (IOException e) {
                throw new ElasticeException(e);
            }
        }

        BulkResponse responses = bulkRequestBuilder.execute().actionGet(10, TimeUnit.SECONDS);
        if (responses.hasFailures()) {
            logger.error("update index failed:" + responses.buildFailureMessage());
            return false;
        }
        return true;
    }
    
    private UpdateRequestBuilder buildUpdateRequest(String index, IndexObject indexObject) throws IOException {
        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(index, "default", indexObject.id);
        try {
            if (Strings.isEmpty(indexObject.id)) {
                throw new IllegalArgumentException("id must be set when using update");
            }
            if (!Strings.isEmpty(indexObject.routing)) {
                updateRequestBuilder.setRouting(indexObject.routing);
            }
            return updateRequestBuilder.setDoc(indexObject.build());
        } catch (Exception e) {
            throw new ElasticeException(e);
        }
    }
    
    public boolean delete(String indexName, String id) {
        return delete(indexName, Arrays.asList(id));
    }

    public boolean delete(String indexName, List<String> ids) {
        if (indexName == null) {
            throw new NullPointerException("indexName must not be null.");
        }

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (String id : ids) {
            bulkRequestBuilder.add(client.prepareDelete(indexName, "default", id));
        }

        BulkResponse responses = bulkRequestBuilder.execute().actionGet(10, TimeUnit.SECONDS);
        if (responses.hasFailures()) {
            logger.error("update index failed:" + responses.buildFailureMessage());
            return false;
        }
        return true;
    }
    
     public List<Map<String, Object>> queryList(Query query) {
        return queryResultList(query);
    }


    private List<Map<String, Object>> queryResultList(Query query) {
        try {
            if (query.limit == null) {
                query.limit = Query.DEFAULT_LIMIT;
            }
            SearchRequestBuilder srb = query.buildSearch(client);
            return transform(srb);
        } catch (Throwable e) {
            throw new ElasticeException(e);
        }
    }
   
    protected List<Map<String, Object>> transform(SearchRequestBuilder builder) throws Exception {
        SearchResponse searchResponse = builder.execute().actionGet();
        List<Map<String, Object>> list = new ArrayList<>((int) searchResponse.getHits().getTotalHits());

        for (SearchHit hit : searchResponse.getHits()) {
            Map<String, Object> source = hit.getSource();
            source.put(ElasticClient.ID, hit.getId());
            list.add(source);
        }
        return list;
    }
    
     public Page queryPage(Query query) {
        if (query.limit == null) {
            throw new IllegalStateException("limit must be set when using queryPage");
        }
        int originalLimit = query.limit;
        query.limit++;
        List<Map<String, Object>> result = queryResultList(query);
        boolean hasMore = false;
        if (result.size() > originalLimit) {
            hasMore = true;
            result.remove(result.size() - 1);
        }
        return new Page().start(query.start + 1)
                .items(result)
                .more(hasMore)
                .pageSize(originalLimit);
    }
    
     
    public boolean close(){
        client.close();
        return true;
    }
}
