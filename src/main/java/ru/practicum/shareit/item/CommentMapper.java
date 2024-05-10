package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentDtoIn commentDtoIn, Item item, User author) {
        return new Comment(
                commentDtoIn.getId() != null ? commentDtoIn.getId() : 0,
                commentDtoIn.getText(),
                item, author, LocalDateTime.now()
        );
    }

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return new CommentDtoOut(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
