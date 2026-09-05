package com.gravin.MovieJava.showtimes.service;

import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.showtimes.dto.*;

import java.util.List;

public interface ShowtimeService {
    Showtime createShowtime(CreateShowtimeRequest req);

    PaginationData<Showtime> getShowtimes(GetShowtimesRequest req);

    Showtime getShowtime(Long id);

    Showtime updateShowtime(Long id, UpdateShowtimeRequest req);

    List<ShowtimesGroupedByBrandResponse> getMovieShowtimesGroupByBrand(GetShowTimeGroupedByBrandRequest req);

    void deleteShowtime(Long id);
}
