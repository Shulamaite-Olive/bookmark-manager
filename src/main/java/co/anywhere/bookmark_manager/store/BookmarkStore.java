package co.anywhere.bookmark_manager.store;

import co.anywhere.bookmark_manager.model.Bookmark;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class BookmarkStore {

    private final Map<Long, Bookmark> store = new HashMap<>();

    public void save(Bookmark bookmark) {
        store.put(bookmark.getId(), bookmark);
    }

    public Bookmark findById(Long id) {
        return store.get(id);
    }

    public Collection<Bookmark> findAll() {
        return store.values();
    }

    public Bookmark delete(Long id) {
        return store.remove(id);
    }
    public boolean existsInFolder(Long folderId) {
        return store.values().stream()
                .anyMatch(bookmark ->
                        folderId.equals(bookmark.getFolderId()));
    }
}
