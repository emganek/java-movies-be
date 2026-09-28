package com.gravin.MovieJava.showtimes.service.impl;

import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.showtimes.domain.Seat;
import com.gravin.MovieJava.showtimes.repository.SeatRepository;
import com.gravin.MovieJava.showtimes.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public Integer markSeatsBooked(Long showtimeId, List<Long> seatIds) {
        List<Long> filteredSeatIds = seatIds.stream()
                .distinct()
                .toList();
        if (filteredSeatIds.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_REQUIRED);
        }

        return seatRepository.markSeatsBooked(showtimeId, filteredSeatIds);
    }

    @Override
    @Transactional
    public Integer releaseSeats(Long showtimeId, List<Long> seatIds) {
        List<Long> filteredSeatIds = seatIds.stream()
                .distinct()
                .toList();
        if (filteredSeatIds.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_REQUIRED);
        }

        return seatRepository.releaseSeats(showtimeId, filteredSeatIds);
    }

    @Override
    @Transactional
    public List<Seat> lockSeatsForBooking(Long showtimeId, List<Long> seatIds) {
        List<Long> filteredSeatIds = seatIds.stream()
                .distinct()
                .toList();
        if (filteredSeatIds.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_REQUIRED);
        }

        return seatRepository.lockSeatsForBooking(showtimeId, filteredSeatIds);
    }
}
