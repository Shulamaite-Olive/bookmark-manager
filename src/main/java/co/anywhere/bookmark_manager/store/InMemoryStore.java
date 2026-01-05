package co.anywhere.bookmark_manager.store;

import co.anywhere.bookmark_manager.model.Bookmark;
import co.anywhere.bookmark_manager.model.Folder;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStore {

    public static final Map<Long, Bookmark> BookmarkStore = new HashMap<>();
    public static final Map<String, Folder> FolderStore = new HashMap<>();

    private InMemoryStore() {

    }
}
