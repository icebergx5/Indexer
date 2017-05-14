/*
 * Copyright 2017 yasin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluetech.constraints;

import com.bluetech.core.ElasticClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author yasin
 */
public class FieldEqual implements Constraint {
    private final String field;
    private final Object value;
    private boolean isFilter;

    private FieldEqual(String field, Object value) {
        if (ElasticClient.ID.equalsIgnoreCase(field)) {
            this.field = ElasticClient.ID;
        } else {
            this.field = field;
        }
        this.value = value;
    }


    public static FieldEqual on(String field, Object value) {
        return new FieldEqual(field, value);
    }

    public FieldEqual asFilter() {
        this.isFilter = true;
        return this;
    }

    @Override
    public QueryBuilder createQuery() {
        if (this.value == null) {
            return null;
        }
        if (!isFilter) {
            return QueryBuilders.termQuery(this.field, this.value);
        }
        return null;
    }

    public QueryBuilder createFilter() {
        if (this.value == null) {
            return null;
        }
        if (isFilter) {
             return QueryBuilders.matchQuery(this.field, this.value);
        }
        return null;
    }

    @Override
    public String toString() {
        return this.field + " = '" + this.value + "'";
    }
    
}
