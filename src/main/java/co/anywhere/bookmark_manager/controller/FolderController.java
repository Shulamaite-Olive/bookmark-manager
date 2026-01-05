package co.anywhere.bookmark_manager.controller;

import co.anywhere.bookmark_manager.dto.FolderRequestDto;
import co.anywhere.bookmark_manager.dto.FolderResponseDto;
import co.anywhere.bookmark_manager.exception.FolderDeleteNotAllowedException;
import co.anywhere.bookmark_manager.exception.FolderNotFoundException;
import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.model.Folder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.anywhere.bookmark_manager.store.InMemoryStore.FolderStore;
import static co.anywhere.bookmark_manager.store.InMemoryStore.BookmarkStore;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    @PostMapping
    public ResponseEntity<FolderResponseDto> create(
            @Valid @RequestBody FolderRequestDto FolderRequest) {

        if (FolderStore.containsKey(FolderRequest.getName())) {
            throw new IllegalStateException("Folder name already exists");
        }

        Folder folder = new Folder();
        folder.setName(FolderRequest.getName());
        FolderStore.put(folder.getName(), folder);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(folder));
    }
    @GetMapping
    public ResponseEntity<List<FolderResponseDto>> list() {
        return ResponseEntity.ok(
                FolderStore.values().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }
    @GetMapping("/{name}")
    public ResponseEntity<FolderResponseDto> getByName(@PathVariable String name) {
        Folder folder = FolderStore.get(name);
        if (folder == null) {
            throw new FolderNotFoundException(name);
        }
        return ResponseEntity.ok(toResponse(folder));
    }

    @PutMapping("/{name}")
    public ResponseEntity<FolderResponseDto> update(
            @PathVariable String name,
            @Valid @RequestBody FolderRequestDto FolderRequest) {

        Folder folder = FolderStore.get(name);
        if (folder == null) {
            throw new FolderNotFoundException(name);
        }

        FolderStore.remove(name);
        folder.setName(FolderRequest.getName());
        FolderStore.put(folder.getName(), folder);

        return ResponseEntity.ok(toResponse(folder));
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {

        boolean hasBookmarks = BookmarkStore.values().stream()
                .anyMatch(b -> name.equals(b.getFolderName()));
        if (hasBookmarks) {
            throw new FolderDeleteNotAllowedException();
        }
        if (FolderStore.remove(name) == null) {
            throw new FolderNotFoundException(name);
        }
        return ResponseEntity.noContent().build();
    }

    private FolderResponseDto toResponse(Folder folder) {
        return new FolderResponseDto(folder.getName());
    }
}
