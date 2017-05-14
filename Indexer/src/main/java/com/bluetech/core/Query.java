/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;

import com.bluetech.constraints.Constraint;
import com.bluetech.constraints.FieldEqual;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author yasin
 */
public class Query {
    
    protected static int DEFAULT_LIMIT = 25;

    protected List<Constraint> constraints = new ArrayList<>();
    protected List<Tuple<String, Boolean>> orderBys = new ArrayList<>();
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
        applyOrderBys(srb);
        applyQueriesAndFilters(srb);
        applyLimit(srb);
//        applyRouting(srb);
        return srb;
    }
    
    private void applyOrderBys(SearchRequestBuilder srb) {
        for (Tuple<String, Boolean> sort : orderBys) {
            srb.addSort(sort.getOne(), sort.getTwo() ? SortOrder.ASC : SortOrder.DESC);
        }
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
    
    private void applyLimit(SearchRequestBuilder srb) {
        if (start > 0) {
            srb.setFrom(start);
        }
        if (limit != null && limit > 0) {
            srb.setSize(limit);
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
    
    public Query eq(String field, Object value) {
        constraints.add(FieldEqual.on(field, value));
        return this;
    }
    
    private Query limit(int limit) {
        this.limit = Math.max(0, limit);
        return this;
    }

    public Query limit(int start, int limit) {
        return start(start).limit(limit);
    }
    
    private Query start(int start) {
        this.start = Math.max(start, 0);
        return this;
    }
    
    public Query orderByAsc(String field) {
        orderBys.add(new Tuple<>(field, true));
        return this;
    }


    public Query orderByDesc(String field) {
        orderBys.add(new Tuple<>(field, false));
        return this;
    }
}
