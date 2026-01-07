package co.anywhere.bookmark_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FolderRequestDto {
    @NotBlank(message = "Folder name requires")
    @Size(max = 255, message = "Folder name should be 255 characters")
    private String name;
}
