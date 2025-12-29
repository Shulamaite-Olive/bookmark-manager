package co.anywhere.bookmark_manager.exception;

public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(Long id) {
        super("Bookmark with id " + id + " not found");
    }
}
