package co.anywhere.bookmark_manager.controller;

import co.anywhere.bookmark_manager.dto.FolderRequestDto;
import co.anywhere.bookmark_manager.dto.FolderResponseDto;
import co.anywhere.bookmark_manager.exception.FolderDeleteNotAllowedException;
import co.anywhere.bookmark_manager.exception.FolderNotFoundException;
import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.model.Folder;
import co.anywhere.bookmark_manager.store.BookmarkStore;
import co.anywhere.bookmark_manager.store.FolderStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderStore folderStore;
    private final BookmarkStore bookmarkStore;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<FolderResponseDto> create(
            @Valid @RequestBody FolderRequestDto folderRequest) {

        if (folderStore.existsByName(folderRequest.getName())) {
            throw new IllegalStateException("Folder name already exists");
        }

        Folder folder = new Folder();
        folder.setId(idGenerator.getAndIncrement());
        folder.setName(folderRequest.getName());

        folderStore.save(folder);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FolderResponseDto(folder.getId(), folder.getName()));
    }
    @GetMapping
    public List<FolderResponseDto> list() {
        return folderStore.findAll().stream()
                .map(f -> new FolderResponseDto(f.getId(), f.getName()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponseDto> getByName(@PathVariable Long id) {
        Folder folder = folderStore.findById(id);
        if (folder == null) {
            throw new FolderNotFoundException(id);
        }
        return ResponseEntity.ok(new FolderResponseDto(folder.getId(), folder.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolderResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FolderRequestDto folderRequest) {

        Folder folder = folderStore.findById(id);
        if (folder == null) {
            throw new FolderNotFoundException(id);
        }

        folderStore.delete(id);
        folder.setName(folderRequest.getName());
        folderStore.save(folder);

        return ResponseEntity.ok(toResponse(folder));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (bookmarkStore.existsInFolder(id)) {
            throw new FolderDeleteNotAllowedException();
        }

        Folder removed = folderStore.delete(id);
        if (removed == null) {
            throw new FolderNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }

    private FolderResponseDto toResponse(Folder folder) {
        return new FolderResponseDto(folder.getId(),folder.getName());
    }
}
