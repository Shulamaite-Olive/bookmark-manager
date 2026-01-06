package co.anywhere.bookmark_manager.exception;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(Long id) {
        super("Folder not found");
    }
}