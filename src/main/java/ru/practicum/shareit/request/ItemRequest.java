package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS", schema = "PUBLIC")
@Getter
@Setter
public class ItemRequest {
    @Id
    @Column(name = "REQUEST_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @NotBlank
    @Size(min = 1, max = 500)
    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;
    @Positive
    @Column(name = "REQUESTER_ID", nullable = false)
    protected int requester;
    @Column(name = "CREATED", nullable = false)
    protected LocalDateTime created;
}
