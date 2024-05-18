package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ITEM_REQUESTS", schema = "PUBLIC")
@Getter
@Setter
public class ItemRequest {
    @Id
    @Column(name = "ITEM_REQUEST_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "ITEM_REQUESTER_ID")
    protected User itemRequester;
    @Column(name = "CREATED", nullable = false)
    protected LocalDateTime created;

    public ItemRequest(int id, String description, User itemRequester, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.itemRequester = itemRequester;
        this.created = created;
    }

    public ItemRequest(String description, User itemRequester, LocalDateTime created) {
        this.description = description;
        this.itemRequester = itemRequester;
        this.created = created;
    }

    public ItemRequest() {
    }
}
