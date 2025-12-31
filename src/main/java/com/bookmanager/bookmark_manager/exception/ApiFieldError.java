package com.bookmanager.bookmark_manager.exception;

public record ApiFieldError(
        String field,
        String message,
        String code
) {}
