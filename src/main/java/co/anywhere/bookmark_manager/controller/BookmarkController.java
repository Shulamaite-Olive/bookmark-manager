package co.anywhere.bookmark_manager.controller;

import co.anywhere.bookmark_manager.dto.BookmarkRequestDto;
import co.anywhere.bookmark_manager.dto.BookmarkResponseDto;
import co.anywhere.bookmark_manager.exception.BookmarkNotFoundException;
import co.anywhere.bookmark_manager.exception.FolderNotFoundException;
import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.store.BookmarkStore;
import co.anywhere.bookmark_manager.store.FolderStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkStore bookmarkStore;
    private final FolderStore folderStore;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<BookmarkResponseDto> create(
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {
        if (bookmarkRequest.getFolderId() != null &&
                folderStore.findById(bookmarkRequest.getFolderId()) == null) {
            throw new FolderNotFoundException(bookmarkRequest.getFolderId());
        }
        Bookmark bookmark = new Bookmark();
        bookmark.setId(idGenerator.getAndIncrement());
        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setCreatedAt(Instant.now());
        bookmark.setUpdatedAt(Instant.now());
        bookmark.setFolderId(bookmarkRequest.getFolderId());

        bookmarkStore.save(bookmark);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(bookmark));
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> list() {
        return ResponseEntity.ok(
                bookmarkStore.findAll().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> getById(@PathVariable Long id) {
        Bookmark bookmark = bookmarkStore.findById(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }
        return ResponseEntity.ok(toResponse(bookmark));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BookmarkRequestDto bookmarkRequest) {

        Bookmark bookmark = bookmarkStore.findById(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }

        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        bookmark.setDescription(bookmarkRequest.getDescription());
        bookmark.setUpdatedAt(Instant.now());
        bookmark.setFolderId(bookmarkRequest.getFolderId());

        bookmarkStore.save(bookmark);

        return ResponseEntity.ok(toResponse(bookmark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Bookmark removed = bookmarkStore.delete(id);

        if (removed == null) {
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
        response.setFolderId(bookmark.getFolderId());
        return response;
    }
}
