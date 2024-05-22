package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentDtoIn;
import ru.practicum.shareit.item.model.CommentDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
                comment.getComment(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
