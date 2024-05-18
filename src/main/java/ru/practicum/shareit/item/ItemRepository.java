package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(int ownerId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "   or upper(i.description) like upper(concat('%', ?1, '%')) and available=true")
    List<Item> search(String text);

    List<Item> findAllByItemRequest_Id(int itemRequestId);

}
