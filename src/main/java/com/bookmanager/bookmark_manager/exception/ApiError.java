package com.bookmanager.bookmark_manager.exception;

import java.util.List;

public record ApiError(
        String message,
        String code,
        List<ApiFieldError> errors
) {}