package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findAllByItemRequester_IdOrderByCreatedDesc(int itemRequesterId);

    Page<ItemRequest> findAllByItemRequester_IdNotOrderByCreatedDesc(int itemRequesterId, Pageable pageable);

}
