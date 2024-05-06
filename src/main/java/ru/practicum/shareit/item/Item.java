package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.practicum.shareit.request.ItemRequest;
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
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    protected User owner;
    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    protected ItemRequest request;

    public Item(int id, String name, String description, Boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}
