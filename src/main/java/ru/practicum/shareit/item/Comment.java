package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENTS", schema = "PUBLIC")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "COMMENT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "TEXT", nullable = false)
    protected String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "ITEM_ID")
    protected Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "AUTHOR_ID")
    protected User author;
    @Column(name = "CREATED", nullable = false)
    protected LocalDateTime created;

    public Comment(int id, String text, Item item, User author, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.author = author;
        this.created = created;
    }

    public Comment(String text, Item item, User author, LocalDateTime created) {
        this.text = text;
        this.item = item;
        this.author = author;
        this.created = created;
    }

    public Comment() {
    }
}
