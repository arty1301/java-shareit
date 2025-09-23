package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdOrderByStartDesc(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.end < :end ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
            @Param("ownerId") Long ownerId, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start > :start ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            @Param("ownerId") Long ownerId, @Param("start") LocalDateTime start, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.status = :status ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(
            @Param("ownerId") Long ownerId, @Param("status") BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :currentTime AND b.end > :currentTime ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            @Param("ownerId") Long ownerId,
            @Param("currentTime") LocalDateTime currentTime,
            Pageable pageable);

    List<Booking> findByItemIdAndEndIsBeforeOrderByStartDesc(Long itemId, LocalDateTime end);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartDesc(Long itemId, LocalDateTime start);

    List<Booking> findByItemIdAndBookerIdAndEndIsBefore(Long itemId, Long bookerId, LocalDateTime end);

    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemIdAndEndIsBeforeOrderByStartDesc(Long itemId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartDesc(Long itemId, LocalDateTime start, Pageable pageable);
}