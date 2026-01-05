package co.anywhere.bookmark_manager.exception;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String folderName) {
        super("Folder not found");
    }
}