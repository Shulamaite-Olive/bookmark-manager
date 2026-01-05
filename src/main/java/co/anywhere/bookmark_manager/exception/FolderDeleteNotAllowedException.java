package co.anywhere.bookmark_manager.exception;

public class FolderDeleteNotAllowedException extends RuntimeException {
    public FolderDeleteNotAllowedException() {
        super("Cannot delete folder with assigned bookmarks");
    }
}