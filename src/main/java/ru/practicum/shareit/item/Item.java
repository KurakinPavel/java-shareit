package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS", schema = "PUBLIC")
@Getter
@Setter
public class Item {
    @Id
    @Column(name = "ITEM_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "NAME", nullable = false)
    protected String name;
    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;
    @Column(name = "AVAILABLE", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    protected Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "OWNER_ID")
    protected User owner;

    public Item(int id, String name, String description, Boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item(String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item() {
    }
}
