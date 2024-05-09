package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(int bookerId);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(int ownerId);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(int ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(int ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);

    List<Booking> findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(int itemId, LocalDateTime start);

    List<Booking> findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(int itemId, LocalDateTime start);

    List<Booking> findFirst1ByItemIdAndBookerIdAndEndIsBeforeOrderByEndDesc(int itemId, int userId, LocalDateTime end);

}
