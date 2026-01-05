package co.anywhere.bookmark_manager.controller;

import co.anywhere.bookmark_manager.dto.BookmarkRequestDto;
import co.anywhere.bookmark_manager.dto.BookmarkResponseDto;
import co.anywhere.bookmark_manager.exception.BookmarkNotFoundException;
import co.anywhere.bookmark_manager.exception.FolderNotFoundException;
import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.model.Folder;
import co.anywhere.bookmark_manager.store.InMemoryStore;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<BookmarkResponseDto> create(
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {
        if (bookmarkRequest.getFolderName() != null) {
            Folder folder = InMemoryStore.FolderStore.get(bookmarkRequest.getFolderName());
            if (folder == null) {
                throw new FolderNotFoundException(bookmarkRequest.getFolderName());
            }
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setId(idGenerator.getAndIncrement());
        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setCreatedAt(Instant.now());
        bookmark.setUpdatedAt(Instant.now());
        bookmark.setFolderName(bookmarkRequest.getFolderName());

        InMemoryStore.BookmarkStore.put(bookmark.getId(), bookmark);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(bookmark));
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> list() {
        return ResponseEntity.ok(
                InMemoryStore.BookmarkStore.values().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> getById(@PathVariable Long id) {
        Bookmark bookmark = InMemoryStore.BookmarkStore.get(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }
        return ResponseEntity.ok(toResponse(bookmark));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {

        Bookmark bookmark = InMemoryStore.BookmarkStore.get(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }

        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setUpdatedAt(Instant.now());
        bookmark.setFolderName(bookmarkRequest.getFolderName());

        return ResponseEntity.ok(toResponse(bookmark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (InMemoryStore.BookmarkStore.remove(id) == null) {
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
        response.setFolderName(bookmark.getFolderName());
        return response;
    }
}
