package co.anywhere.bookmark_manager.controller;

import co.anywhere.bookmark_manager.dto.BookmarkResponseDto;
import co.anywhere.bookmark_manager.exception.BookmarkNotFoundException;
import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.dto.BookmarkRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final Map<Long, Bookmark> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<BookmarkResponseDto> create(
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {

        Bookmark bookmark = new Bookmark();
        bookmark.setId(idGenerator.getAndIncrement());
        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setCreatedAt(Instant.now());
        bookmark.setUpdatedAt(Instant.now());

        store.put(bookmark.getId(), bookmark);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(bookmark));
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> list() {
        return ResponseEntity.ok(
                store.values().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> getById(@PathVariable Long id) {
        Bookmark bookmark = store.get(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }
        return ResponseEntity.ok(toResponse(bookmark));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {

        Bookmark bookmark = store.get(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }

        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setUpdatedAt(Instant.now());

        return ResponseEntity.ok(toResponse(bookmark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (store.remove(id) == null) {
            throw new BookmarkNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    private BookmarkResponseDto toResponse(Bookmark bookmark) {
        BookmarkResponseDto response = new BookmarkResponseDto();
        response.setId(bookmark.getId());
        response.setTitle(bookmark.getTitle());
        response.setUrl(bookmark.getUrl());
        response.setDescription(bookmark.getDescription());
        response.setCreatedAt(bookmark.getCreatedAt());
        response.setUpdatedAt(bookmark.getUpdatedAt());
        return response;
    }
}
