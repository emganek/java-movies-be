package com.gravin.MovieJava.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginationData<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <D> PaginationData<D> of(
            List<D> content,
            int page,
            int size,
            long totalElements
    ) {
        int totalPages = (int) Math.ceilDiv(totalElements, size);

        return new PaginationData<>(content, page, size, totalElements, totalPages);
    }

    public static <D> PaginationData<D> from (
            Page<D> page
    ) {
        return new PaginationData<>(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
