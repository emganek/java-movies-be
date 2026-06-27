package com.gravin.MovieJava.movies.repository.impl;

import com.gravin.MovieJava.movies.domain.Movie;
import com.gravin.MovieJava.movies.repository.MovieCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieCustomRepositoryImpl implements MovieCustomRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Movie> findMoviesWithFilter(String name, LocalDate premiereDateFrom, LocalDate premiereDateTo, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Movie m WHERE 1=1");
        appendMovieFilters(jpql, name, premiereDateFrom, premiereDateTo);

        TypedQuery<Movie> query = entityManager.createQuery(jpql.toString(), Movie.class);
        setMovieFilterParams(query, name, premiereDateFrom, premiereDateTo);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countMoviesWithFilter(String name, LocalDate premiereDateFrom, LocalDate premiereDateTo) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(m) FROM Movie m WHERE 1=1");
        appendMovieFilters(jpql, name, premiereDateFrom, premiereDateTo);

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        setMovieFilterParams(query, name, premiereDateFrom, premiereDateTo);

        return query.getSingleResult();
    }

    private void appendMovieFilters(
            StringBuilder jpql,
            String name,
            LocalDate premiereDateFrom,
            LocalDate premiereDateTo
    ) {
        if (name != null && !name.isBlank()) {
            jpql.append(" AND LOWER(m.name) LIKE LOWER(:name)");
        }

        if (premiereDateFrom != null) {
            jpql.append(" AND m.premiereDate >= :premiereDateFrom");
        }

        if (premiereDateTo != null) {
            jpql.append(" AND m.premiereDate <= :premiereDateTo");
        }
    }

    private void setMovieFilterParams(
            TypedQuery<?> query,
            String name,
            LocalDate premiereDateFrom,
            LocalDate premiereDateTo
    ) {
        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name + "%");
        }

        if (premiereDateFrom != null) {
            query.setParameter("premiereDateFrom", premiereDateFrom);
        }

        if (premiereDateTo != null) {
            query.setParameter("premiereDateTo", premiereDateTo);
        }
    }
}
