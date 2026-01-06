package co.anywhere.bookmark_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class BookmarkRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "URL is required")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "URL must be a valid HTTP or HTTPS URL"
    )
    private String url;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    private Long folderId;
}
