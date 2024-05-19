package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBooker_IdOrderByStartDesc(int bookerId, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int bookerId, LocalDateTime start,
                                                                                  LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdOrderByStartDesc(int ownerId, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int ownerId, LocalDateTime start,
                                                                                      LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(int ownerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(int ownerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(int ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(int itemId, LocalDateTime start);

    List<Booking> findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(int itemId, LocalDateTime start);

    List<Booking> findFirst1ByItemIdAndBookerIdAndEndIsBeforeOrderByEndDesc(int itemId, int userId, LocalDateTime end);

}
