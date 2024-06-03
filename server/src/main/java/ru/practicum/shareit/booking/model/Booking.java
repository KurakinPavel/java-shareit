package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "BOOKINGS", schema = "PUBLIC")
@Getter
@Setter
public class Booking {
    @Id
    @Column(name = "BOOKING_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "START_DATE", nullable = false)
    protected LocalDateTime start;
    @Column(name = "END_DATE", nullable = false)
    protected LocalDateTime end;
    @Enumerated(EnumType.ORDINAL)
    protected BookingStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "BOOKER_ID")
    protected User booker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "ITEM_ID")
    protected Item item;

    public Booking(int id, LocalDateTime start, LocalDateTime end, BookingStatus status, User booker, Item item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    public Booking(LocalDateTime start, LocalDateTime end, BookingStatus status, User booker, Item item) {
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    public Booking() {
    }
}
