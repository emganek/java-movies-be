package com.gravin.MovieJava.common.repository;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryBuilder {
    @Getter
    private StringBuilder jpql;

    private Map<String, Object> params = new LinkedHashMap<>();

    public QueryBuilder(String baseJpql) {
        this.jpql = new StringBuilder(baseJpql);
    }

    public void addLike(
            String fieldName, String paramName, String value
    ) {
        if (value == null || value.isBlank() ) {
            return;
        }

        jpql.append(" AND LOWER(%s) LIKE LOWER(:%s)".formatted(fieldName,paramName));
        params.put(paramName, value);
    }

    public void addGreaterOrEqual(
            String fieldName, String paramName, Object value
    ) {
        if (value == null ) {
            return;
        }

        jpql.append(" AND %s >= LOWER(:%s)".formatted(fieldName,paramName));
        params.put(paramName, value);
    }

    public void addSmallerOrEqual(
            String fieldName, String paramName, Object value
    ) {
        if (value == null ) {
            return;
        }

        jpql.append(" AND %s <= LOWER(:%s)".formatted(fieldName,paramName));
        params.put(paramName, value);
    }

    public void addEqual(
            String fieldName, String paramName, Object value
    ) {
        if (value == null ) {
            return;
        }

        jpql.append(" AND %s = LOWER(:%s)".formatted(fieldName,paramName));
        params.put(paramName, value);
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
