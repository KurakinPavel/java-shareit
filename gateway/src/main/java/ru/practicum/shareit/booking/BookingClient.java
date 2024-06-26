package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.BookingValidationException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(int userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", (long) userId, parameters);
    }

    public ResponseEntity<Object> bookItem(int userId, BookItemRequestDto requestDto) {
        if (requestDto.getStart() == null || requestDto.getEnd() == null || requestDto.getStart().isAfter(requestDto.getEnd())
                || requestDto.getStart().equals(requestDto.getEnd())) {
            throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        }
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(int userId, int bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> confirm(int ownerId, int bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", String.valueOf(approved)
        );
        System.out.println(parameters.get("approved"));
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters);
    }

    public ResponseEntity<Object> getBookingsOfOwnerItems(int userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", (long) userId, parameters);
    }
}
