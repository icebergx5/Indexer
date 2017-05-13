package com.bluetech.constraints;

import org.elasticsearch.index.query.QueryBuilder;

public interface Constraint {

    QueryBuilder createQuery();

}
