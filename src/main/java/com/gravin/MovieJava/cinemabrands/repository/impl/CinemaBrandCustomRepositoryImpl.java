package com.gravin.MovieJava.cinemabrands.repository.impl;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.repository.CinemaBrandCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CinemaBrandCustomRepositoryImpl implements CinemaBrandCustomRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<CinemaBrand> findCinemaBrandsWithFilter(String name, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM cinema_brand_entity c WHERE 1=1");
        appendCinemaBrandFilters(jpql, name);

        TypedQuery<CinemaBrand> query = entityManager.createQuery(jpql.toString(), CinemaBrand.class);
        setCinemaBrandFilterParams(query, name);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countCinemaBrandsWithFilter(String name) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM cinema_brand_entity c WHERE 1=1");
        appendCinemaBrandFilters(jpql, name);

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        setCinemaBrandFilterParams(query, name);

        return query.getSingleResult();
    }

    private void appendCinemaBrandFilters(StringBuilder jpql, String name) {
        if (name != null && !name.isBlank()) {
            jpql.append(" AND LOWER(c.name) LIKE LOWER(:name)");
        }
    }

    private void setCinemaBrandFilterParams(TypedQuery<?> query, String name) {
        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name + "%");
        }
    }
}
