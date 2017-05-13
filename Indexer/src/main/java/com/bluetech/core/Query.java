/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;

import com.bluetech.constraints.Constraint;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author yasin
 */
public class Query {
    
    protected static int DEFAULT_LIMIT = 25;

    protected List<Constraint> constraints = new ArrayList<>();
    protected int start;
    protected Integer limit = null;
    protected String query;
    protected String index;
    protected String type;
    protected String routing;
    
    public Query() {
    }

    public Query(String indexName) {
        this(indexName, ElasticClient.DEFAULT_TYPE);
    }

    public Query(String indexName, String type) {
        if (Strings.isEmpty(indexName)) {
            throw new NullPointerException("indexName must not be empty.");
        }
        this.index = indexName;
        this.type = type;
    }
    
    protected SearchRequestBuilder buildSearch(Client client) {
        SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type);
        srb.setVersion(true);
//        applyOrderBys(srb);
//        applyQueriesAndFilters(srb);
//        applyLimit(srb);
//        applyRouting(srb);
        return srb;
    }
    
    private void applyQueriesAndFilters(SearchRequestBuilder srb) {
        QueryBuilder qb = buildQuery();
        if (qb != null) {
            srb.setQuery(qb);
        }
        QueryBuilder fb = buildQuery();

        if (fb != null) {
            srb.setPostFilter(fb);
        }
    }
    
    
    protected QueryBuilder buildQuery() {
        List<QueryBuilder> queries = new ArrayList<QueryBuilder>();
        for (Constraint constraint : constraints) {
            QueryBuilder qb = constraint.createQuery();
            if (qb != null) {
                queries.add(qb);
            }
        }
        if (queries.isEmpty()) {
            return null;
        } else if (queries.size() == 1) {
            return queries.get(0);
        } else {
            BoolQueryBuilder result = QueryBuilders.boolQuery();
            for (QueryBuilder qb : queries) {
                result.must(qb);
            }
            return result;
        }
    }
}
