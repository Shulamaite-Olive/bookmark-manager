package com.bookmanager.bookmark_manager.model;

import lombok.Data;

import java.time.Instant;
@Data
public class Bookmark {
    private Long id;
    private String title;
    private String url;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
