package com.gravin.MovieJava.common.repository;

import com.gravin.MovieJava.common.response.PaginationData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseCustomRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    protected <T> List<T> getResultList(
        String jpql,
        Class<T> clazz,
        Map<String, Object> queryParams,
        int page,
        int size
    ) {
        TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
        queryParams.forEach(query::setParameter);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    protected Long getCount(
            String jpql,
            Map<String, Object> queryParams
    ) {
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        queryParams.forEach(query::setParameter);

        return query.getSingleResult();
    }

    protected <T> PaginationData<T> paginate(
            String jpql,
            Class<T> clazz,
            Map<String, Object> queryParams,
            int page,
            int size
    ) {
        String countJpql = "SELECT COUNT(*) " + jpql.substring(jpql.toUpperCase().indexOf("FROM"));
        List<T> content = getResultList(jpql, clazz, queryParams, page, size);
        Long totalElement = getCount(countJpql, queryParams);

        return PaginationData.of(content, page, size, totalElement);
    }
}
