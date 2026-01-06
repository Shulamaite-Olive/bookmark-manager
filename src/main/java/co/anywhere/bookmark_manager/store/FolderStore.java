package co.anywhere.bookmark_manager.store;

import co.anywhere.bookmark_manager.model.Folder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FolderStore {

    private final Map<Long, Folder> store = new HashMap<>();

    public void save(Folder folder) {
        store.put(folder.getId(), folder);
    }

    public Folder findById(Long id) {
        return store.get(id);
    }

    public Collection<Folder> findAll() {
        return store.values();
    }

    public Folder delete(Long id) {
        return store.remove(id);
    }

    public boolean existsByName(String name) {
        return store.values().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(name));
    }
}