package co.anywhere.bookmark_manager.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class BookmarkResponseDto {

    private Long id;
    private String title;
    private String url;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String folderName;
}
